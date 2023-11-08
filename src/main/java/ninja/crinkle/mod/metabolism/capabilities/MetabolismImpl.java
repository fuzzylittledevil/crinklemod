package ninja.crinkle.mod.metabolism.capabilities;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.metabolism.config.ConsumableConfig;
import ninja.crinkle.mod.metabolism.config.MetabolismConfig;
import ninja.crinkle.mod.metabolism.events.AccidentEvent;
import ninja.crinkle.mod.metabolism.events.DesperationEvent;
import ninja.crinkle.mod.metabolism.network.MetabolismChannel;
import ninja.crinkle.mod.util.MathUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;

import static ninja.crinkle.mod.metabolism.capabilities.Metabolism.*;

/**
 * Metabolism capability implementation.
 * This class is used to store the metabolism of a player in a compound NBT tag.
 * @author Galen
 * @see IMetabolism
 */
public class MetabolismImpl implements IMetabolism {
    private static final Logger LOGGER = LogUtils.getLogger();
    private double liquids = 0;
    private double solids = 0;
    private double bladder = 0;
    private double bowels = 0;
    private double bladderDesperation = 0;
    private double bowelsDesperation = 0;
    private boolean isDirty = false;


    /**
     * Modify the amount of liquids in the player's stomach.
     * @param value The amount of liquids to add or remove. Negative values remove liquids.
     */
    private void modifyLiquids(double value) {
        setLiquids(getLiquids() + value);
    }

    /**
     * Modify the amount of solids in the player's stomach.
     * @param value The amount of solids to add or remove. Negative values remove solids.
     */
    private void modifySolids(double value) {
        setSolids(getSolids() + value);
    }

    /**
     * Modify the amount of liquids in the player's bladder.
     * @param value The amount of liquids to add or remove. Negative values remove liquids.
     */
    private void modifyBladder(double value) {
        setBladder(getBladder() + value);
    }

    /**
     * Modify the amount of solids in the player's bowels.
     * @param value The amount of solids to add or remove. Negative values remove solids.
     */
    private void modifyBowels(double value) {
        setBowels(getBowels() + value);
    }

    /**
     * Tick the digestion of the player.
     * This method is called every tick to simulate the digestion of the player.
     * This method removes liquids and solids from the stomach and adds them to the bladder and bowels respectively.
     * @see IMetabolism#tick(Player)
     */
    private void tickDigestion() {
        if (getSolids() > 0) {
            modifySolids(-MetabolismConfig.solidsRate);
            modifyBowels(MetabolismConfig.solidsRate);
        }
        if (getLiquids() > 0) {
            modifyLiquids(-MetabolismConfig.liquidsRate);
            modifyBladder(MetabolismConfig.liquidsRate);
        }
    }

    /**
     * Tick the continence of the player.
     * This method is called every tick to simulate the continence of the player.
     * This method increases the desperation of the player's bladder and bowels based on how full they are.
     * @see IMetabolism#tick(Player)
     */
    private void tickContinence() {
        if (getBladder() > 0) {
            double fullPct = getBladder() / MetabolismConfig.bladderCapacity;
            setBladderDesperation(fullPct * (1 - MetabolismConfig.continence));
        }
        if (getBowels() > 0) {
            double fullPct = getBowels() / MetabolismConfig.bowelsCapacity;
            setBowelsDesperation(fullPct * (1 - MetabolismConfig.continence));
        }
    }

    /**
     * Check for a bladder accident.
     * This method is called every tick to check if the player has had a bladder accident.
     * An event is posted if the player has an accident.
     * @see IMetabolism#tick(Player)
     * @see AccidentEvent.Bladder
     * @param player The player to check for an accident.
     */
    private void checkForBladderAccident(Player player) {
        // TODO: This isn't working right
        double accidentChance = getBladderDesperation() * getBladder() / MetabolismConfig.bladderCapacity;
        accidentChance *= 1 - MetabolismConfig.continence;
        if (Math.random() < accidentChance) {
            double accidentAmount = getBladder() * (1 - MetabolismConfig.continence);
            modifyBladder(-accidentAmount);
            CrinkleMod.EVENT_BUS.post(new AccidentEvent.Bladder(player, accidentAmount));
        }
    }

    /**
     * Check for a bowel accident.
     * This method is called every tick to check if the player has had a bowel accident.
     * An event is posted if the player has an accident.
     * @see IMetabolism#tick(Player)
     * @see AccidentEvent.Bowels
     * @param player The player to check for an accident.
     */
    private void checkForBowelAccident(Player player) {
        // TODO: This isn't working right
        double accidentChance = getBowelsDesperation() * getBowels() / MetabolismConfig.bowelsCapacity;
        accidentChance *= 1 - MetabolismConfig.continence;
        if (Math.random() < accidentChance) {
            double accidentAmount = getBowels() * (1 - MetabolismConfig.continence);
            modifyBowels(-accidentAmount);
            CrinkleMod.EVENT_BUS.post(new AccidentEvent.Bowels(player, accidentAmount));
        }
    }

    /**
     * Check for a bladder desperation event.
     * This method is called every tick to check if the player has a bladder desperation event.
     * An event is posted if the player has a desperation event.
     * @see IMetabolism#tick(Player)
     * @see DesperationEvent.Bladder
     * @param player The player to check for a desperation event.
     */
    private void checkForBladderDesperationEvent(Player player) {
        if (getBladderDesperation() > 0.5 && Math.random() < getBladderDesperation()) {
            CrinkleMod.EVENT_BUS.post(new DesperationEvent.Bladder(player, getBladderDesperation()));
        }
    }

    /**
     * Check for a bowels desperation event.
     * This method is called every tick to check if the player has a bowels desperation event.
     * An event is posted if the player has a desperation event.
     * @see IMetabolism#tick(Player)
     * @see DesperationEvent.Bowels
     * @param player The player to check for a desperation event.
     */
    private void checkForBowelsDesperationEvent(Player player) {
        if (getBowelsDesperation() > 0.5 && Math.random() < getBladderDesperation()) {
            CrinkleMod.EVENT_BUS.post(new DesperationEvent.Bowels(player, getBowelsDesperation()));
        }
    }

    /**
     * Consume an item.
     * This method is called when an item is consumed by a player.
     * This method first checks to see if we have an override for a given item, if not then it uses the item's food properties.
     * @param item The item to consume
     * @param entity The entity consuming the item
     */
    @Override
    public void consume(@NotNull ItemStack item, @NotNull LivingEntity entity) {
        if (ConsumableConfig.consumables.containsKey(item.getItem())) {
            modifyLiquids(ConsumableConfig.consumables.get(item.getItem()).liquids);
            modifySolids(ConsumableConfig.consumables.get(item.getItem()).solids);
        } else {
            Optional.ofNullable(item.getFoodProperties(entity)).ifPresent(foodProperties -> {
                if (item.isEdible()) {
                    modifySolids(foodProperties.getNutrition());
                    modifyLiquids(foodProperties.getNutrition() / 4f);
                }
            });
        }
    }

    /**
     * Reset metabolism data.
     * @param player The player to reset
     */
    @Override
    public void reset(Player player) {
        setLiquids(0, false);
        setSolids(0, false);
        setBladder(0, false);
        setBowels(0, false);
        setBladderDesperation(0, false);
        setBowelsDesperation(0, false);
        sync(player);
    }

    /**
     * Tick the metabolism of a player.
     * The metabolism is ticked every tick to simulate the digestion and continence of the player.
     * <br><br><Strong>NOTE:</Strong> This method should only be called on the client.
     * If the metabolism has changed, then a sync is sent to the server.
     * @param player The player to tick
     */
    @Override
    public void tick(Player player) {
        tickDigestion();
        tickContinence();
        checkForBladderAccident(player);
        checkForBowelAccident(player);
        checkForBladderDesperationEvent(player);
        checkForBowelsDesperationEvent(player);
        if (isDirty) {
            LOGGER.debug("Sending sync to server");
            sync(player);
            isDirty = false;
        }
    }

    /**
     * Sync the metabolism of a player to the client or server.
     * @param entity The player to sync
     */
    @Override
    public void sync(Player entity) {
        if (entity instanceof ServerPlayer player) {
            MetabolismChannel.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new MetabolismChannel.UpdateMessage(this));
        } else {
            MetabolismChannel.INSTANCE.sendToServer(new MetabolismChannel.UpdateMessage(this));
        }
    }

    /**
     * Serialize the metabolism to NBT.
     * @return The serialized metabolism
     */
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble(NBT_KEY_LIQUIDS, getLiquids());
        tag.putDouble(NBT_KEY_SOLIDS, getSolids());
        tag.putDouble(NBT_KEY_BLADDER, getBladder());
        tag.putDouble(NBT_KEY_BOWELS, getBowels());
        tag.putDouble(NBT_KEY_BLADDER_DESPERATION, getBladderDesperation());
        tag.putDouble(NBT_KEY_BOWELS_DESPERATION, getBowelsDesperation());
        return tag;
    }

    /**
     * Deserialize the metabolism from NBT.
     * @param nbt The NBT to deserialize
     */
    @Override
    public void deserializeNBT(@NotNull CompoundTag nbt) {
        setLiquids(nbt.getDouble(NBT_KEY_LIQUIDS));
        setSolids(nbt.getDouble(NBT_KEY_SOLIDS));
        setBladder(nbt.getDouble(NBT_KEY_BLADDER));
        setBowels(nbt.getDouble(NBT_KEY_BOWELS));
        setBladderDesperation(nbt.getDouble(NBT_KEY_BLADDER_DESPERATION));
        setBowelsDesperation(nbt.getDouble(NBT_KEY_BOWELS_DESPERATION));
    }

    /**
     * Get the amount of liquids in the player's stomach.
     * @return The amount of liquids in the player's stomach.
     */
    @Override
    public double getLiquids() {
        return liquids;
    }

    /**
     * Set the amount of liquids in the player's stomach.
     * Sets the sync flag to true.
     * @see IMetabolism#setLiquids(double, boolean)
     * @param liquids The amount of liquids in the player's stomach
     */
    @Override
    public void setLiquids(double liquids) {
        setLiquids(liquids, true);
    }

    /**
     * Set the amount of liquids in the player's stomach.
     * The value is clamped between {@link Metabolism#MIN_LIQUIDS} and {@link Metabolism#MAX_LIQUIDS}.
     * @param liquids The amount of liquids in the player's stomach
     * @param sync Whether to sync the value to the client or server
     */
    @Override
    public void setLiquids(double liquids, boolean sync) {
        this.liquids = MathUtil.clamp(liquids, MIN_LIQUIDS, MAX_LIQUIDS);
        this.isDirty = sync;
    }

    /**
     * Get the amount of solids in the player's stomach.
     * @return The amount of solids in the player's stomach.
     */
    @Override
    public double getSolids() {
        return solids;
    }

    /**
     * Set the amount of solids in the player's stomach.
     * Sets the sync flag to true.
     * @see IMetabolism#setSolids(double, boolean)
     * @param solids The amount of solids in the player's stomach
     */
    @Override
    public void setSolids(double solids) {
        setSolids(solids, true);
    }

    /**
     * Set the amount of solids in the player's stomach.
     * The value is clamped between {@link Metabolism#MIN_SOLIDS} and {@link Metabolism#MAX_SOLIDS}.
     * @param solids The amount of solids in the player's stomach
     * @param sync Whether to sync the value to the client or server
     */
    @Override
    public void setSolids(double solids, boolean sync) {
        this.solids = MathUtil.clamp(solids, MIN_SOLIDS, MAX_SOLIDS);
        this.isDirty = sync;
    }

    /**
     * Get the amount of fluid in the player's bladder.
     * @return The amount of fluid in the player's bladder.
     */
    @Override
    public double getBladder() {
        return bladder;
    }

    /**
     * Set the amount of fluid in the player's bladder.
     * Sets the sync flag to true.
     * @see IMetabolism#setBladder(double, boolean)
     * @param bladder The amount of fluid in the player's bladder
     */
    @Override
    public void setBladder(double bladder) {
        setBladder(bladder, true);
    }

    /**
     * Set the amount of fluid in the player's bladder.
     * The value is clamped between 0 and {@link MetabolismConfig#bladderCapacity}.
     * @param bladder The amount of fluid in the player's bladder
     * @param sync Whether to sync the value to the client or server
     */
    @Override
    public void setBladder(double bladder, boolean sync) {
        this.bladder = MathUtil.clamp(bladder, 0, MetabolismConfig.bladderCapacity);
        this.isDirty = sync;
    }

    /**
     * Get the amount of solids in the player's bowels.
     * @return The amount of solids in the player's bowels.
     */
    @Override
    public double getBowels() {
        return bowels;
    }

    /**
     * Set the amount of solids in the player's bowels.
     * Sets the sync flag to true.
     * @see IMetabolism#setBowels(double, boolean)
     * @param bowels The amount of solids in the player's bowels
     */
    @Override
    public void setBowels(double bowels) {
        setBowels(bowels, true);
    }

    /**
     * Set the amount of solids in the player's bowels.
     * The value is clamped between 0 and {@link MetabolismConfig#bowelsCapacity}.
     * @param bowels The amount of solids in the player's bowels
     * @param sync Whether to sync the value to the client or server
     */
    @Override
    public void setBowels(double bowels, boolean sync) {
        this.bowels = MathUtil.clamp(bowels, 0, MetabolismConfig.bowelsCapacity);
        this.isDirty = sync;
    }

    /**
     * Get the desperation of the player's bladder.
     * @return The desperation of the player's bladder.
     */
    @Override
    public double getBladderDesperation() {
        return bladderDesperation;
    }

    /**
     * Set the desperation of the player's bladder.
     * Sets the sync flag to true.
     * @see IMetabolism#setBladderDesperation(double, boolean)
     * @param bladderDesperation The desperation of the player's bladder
     */
    @Override
    public void setBladderDesperation(double bladderDesperation) {
        setBladderDesperation(bladderDesperation, true);
    }

    /**
     * Set the desperation of the player's bladder.
     * The value is clamped between 0 and 1.
     * @param bladderDesperation The desperation of the player's bladder
     * @param sync Whether to sync the value to the client or server
     */
    @Override
    public void setBladderDesperation(double bladderDesperation, boolean sync) {
        this.bladderDesperation = MathUtil.clamp(bladderDesperation, 0, 1);
        this.isDirty = sync;
    }

    /**
     * Get the desperation of the player's bowels.
     * @return The desperation of the player's bowels.
     */
    @Override
    public double getBowelsDesperation() {
        return bowelsDesperation;
    }

    /**
     * Set the desperation of the player's bowels.
     * Sets the sync flag to true.
     * @see IMetabolism#setBowelsDesperation(double, boolean)
     * @param bowelsDesperation The desperation of the player's bowels
     */
    @Override
    public void setBowelsDesperation(double bowelsDesperation) {
        setBowelsDesperation(bowelsDesperation, true);
    }

    /**
     * Set the desperation of the player's bowels.
     * The value is clamped between 0 and 1.
     * @param bowelsDesperation The desperation of the player's bowels
     * @param sync Whether to sync the value to the client or server
     */
    @Override
    public void setBowelsDesperation(double bowelsDesperation, boolean sync) {
        this.bowelsDesperation = MathUtil.clamp(bowelsDesperation, 0, 1);
        this.isDirty = sync;
    }
}
