package ninja.crinkle.mod.metabolism.client;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.metabolism.client.events.AccidentEvent;
import ninja.crinkle.mod.metabolism.client.events.DesperationEvent;
import ninja.crinkle.mod.metabolism.common.capabilities.Capabilities;
import ninja.crinkle.mod.metabolism.common.capabilities.IMetabolism;
import ninja.crinkle.mod.metabolism.common.config.ConsumableConfig;
import ninja.crinkle.mod.metabolism.common.network.MetabolismChannel;
import ninja.crinkle.mod.metabolism.common.network.messages.UpdateMessage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;

class PlayerProvider {
    public static Player getPlayer() {
        return Minecraft.getInstance().player;
    }
}

public class MetabolismManager {
    public static final MetabolismManager INSTANCE = new MetabolismManager();
    private static final Logger LOGGER = LogUtils.getLogger();

    private boolean isDirty = false;

    private MetabolismManager() {
    }

    /**
     * Get the player's metabolism capability.
     */
    private Optional<IMetabolism> getMetabolism() {
        Player player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> PlayerProvider::getPlayer);
        if (player == null) {
            return Optional.empty();
        }
        return player.getCapability(Capabilities.METABOLISM).resolve();
    }

    private double getSolids() {
        return getMetabolism().map(IMetabolism::getSolids).orElse(0d);
    }

    private double getLiquids() {
        return getMetabolism().map(IMetabolism::getLiquids).orElse(0d);
    }

    private double getBladder() {
        return getMetabolism().map(IMetabolism::getBladder).orElse(0d);
    }

    private double getBowels() {
        return getMetabolism().map(IMetabolism::getBowels).orElse(0d);
    }

    private double getBladderDesperation() {
        return getMetabolism().map(IMetabolism::getBladderDesperation).orElse(0d);
    }

    private double getBowelsDesperation() {
        return getMetabolism().map(IMetabolism::getBowelDesperation).orElse(0d);
    }

    private double getBladderCapacity() {
        return getMetabolism().map(IMetabolism::getBladderCapacity).orElse(0d);
    }

    private double getBowelCapacity() {
        return getMetabolism().map(IMetabolism::getBowelCapacity).orElse(0d);
    }

    private double getSolidsRate() {
        return getMetabolism().map(IMetabolism::getSolidsRate).orElse(0d);
    }

    private double getLiquidsRate() {
        return getMetabolism().map(IMetabolism::getLiquidsRate).orElse(0d);
    }

    private double getBladderContinence() {
        return getMetabolism().map(IMetabolism::getBladderContinence).orElse(0d);
    }

    private double getBowelContinence() {
        return getMetabolism().map(IMetabolism::getBowelContinence).orElse(0d);
    }

    private void setSolids(double value) {
        getMetabolism().ifPresent(m -> m.setSolids(value));
        isDirty = true;
    }

    private void setLiquids(double value) {
        getMetabolism().ifPresent(m -> m.setLiquids(value));
        isDirty = true;
    }

    private void setBladder(double value) {
        getMetabolism().ifPresent(m -> m.setBladder(value));
        isDirty = true;
    }

    private void setBowels(double value) {
        getMetabolism().ifPresent(m -> m.setBowels(value));
        isDirty = true;
    }

    private void setBladderDesperation(double value) {
        getMetabolism().ifPresent(m -> m.setBladderDesperation(value));
        isDirty = true;
    }

    private void setBowelsDesperation(double value) {
        getMetabolism().ifPresent(m -> m.setBowelDesperation(value));
        isDirty = true;
    }

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
     * Modify the desperation of the player's bladder.
     * @param value The amount of desperation to add or remove. Negative values remove desperation.
     */
    private void modifyBladderDesperation(double value) {
        setBladderDesperation(getBladderDesperation() + value);
    }

    /**
     * Modify the desperation of the player's bowels.
     * @param value The amount of desperation to add or remove. Negative values remove desperation.
     */
    private void modifyBowelsDesperation(double value) {
        setBowelsDesperation(getBowelsDesperation() + value);
    }

    /**
     * Tick the digestion of the player.
     * This method is called every tick to simulate the digestion of the player.
     * This method removes liquids and solids from the stomach and adds them to the bladder and bowels respectively.
     */
    private void tickDigestion() {
        if (getSolids() > 0) {
            setSolids(getSolids() - getSolidsRate());
            setBowels(getBowels() + getSolidsRate());
        }
        if (getLiquids() > 0) {
            setLiquids(getLiquids() - getLiquidsRate());
            setBladder(getBladder() + getLiquidsRate());
        }
    }

    /**
     * Tick the continence of the player.
     * This method is called every tick to simulate the continence of the player.
     * This method increases the desperation of the player's bladder and bowels based on how full they are.
     */
    private void tickContinence() {
        if (getBladder() > 0) {
            double fullPct = getBladder() / getBladderCapacity();
            modifyBladderDesperation(fullPct * (1 - getBladderContinence()));
        }
        if (getBowels() > 0) {
            double fullPct = getBowels() / getBowelCapacity();
            modifyBowelsDesperation(fullPct * (1 - getBowelContinence()));
        }
    }

    /**
     * Check for a bladder accident.
     * This method is called every tick to check if the player has had a bladder accident.
     * An event is posted if the player has an accident.
     * @see AccidentEvent.Bladder
     */
    private void checkForBladderAccident() {
        double accidentChance = (getBladderDesperation() * (1 - (getBladder() / getBladderCapacity()))) * (1 - getBladderContinence());
        if (Math.random() < accidentChance) {
            double accidentAmount = getBladder() * (1 - getBladderContinence());
            modifyBladder(-accidentAmount);
            CrinkleMod.EVENT_BUS.post(new AccidentEvent.Bladder(Minecraft.getInstance().player, accidentAmount));
            setBladderDesperation(0);
        }
    }

    /**
     * Check for a bowel accident.
     * This method is called every tick to check if the player has had a bowel accident.
     * An event is posted if the player has an accident.
     * @see AccidentEvent.Bowels
     */
    private void checkForBowelAccident() {
        double accidentChance = (getBowelsDesperation() * (1 - (getBowels() / getBowelCapacity()))) * (1 - getBowelContinence());
        if (Math.random() < accidentChance) {
            double accidentAmount = getBowels() * (1 - getBowelContinence());
            modifyBowels(-accidentAmount);
            CrinkleMod.EVENT_BUS.post(new AccidentEvent.Bowels(Minecraft.getInstance().player, accidentAmount));
            setBowelsDesperation(0);
        }
    }

    /**
     * Check for a bladder desperation event.
     * This method is called every tick to check if the player has a bladder desperation event.
     * An event is posted if the player has a desperation event.
     * @see DesperationEvent.Bladder
     */
    private void checkForBladderDesperationEvent() {
        if (getBladderDesperation() > 0.5 && Math.random() < getBladderDesperation()) {
            CrinkleMod.EVENT_BUS.post(new DesperationEvent.Bladder(Minecraft.getInstance().player, getBladderDesperation()));
        }
    }

    /**
     * Check for a bowels desperation event.
     * This method is called every tick to check if the player has a bowels desperation event.
     * An event is posted if the player has a desperation event.
     * @see DesperationEvent.Bowels
     */
    private void checkForBowelsDesperationEvent() {
        if (getBowelsDesperation() > 0.5 && Math.random() < getBowelsDesperation()) {
            CrinkleMod.EVENT_BUS.post(new DesperationEvent.Bowels(Minecraft.getInstance().player, getBowelsDesperation()));
        }
    }

    private void sync(boolean force) {
        if (isDirty || force) {
            LOGGER.debug("Sending sync to server");
            getMetabolism().ifPresent(m -> MetabolismChannel.INSTANCE.sendToServer(new UpdateMessage(m)));
            isDirty = false;
        }
    }

    /**
     * Consume an item.
     * This method is called when an item is consumed by a player.
     * This method first checks to see if we have an override for a given item, if not then it uses the item's food properties.
     * @param item The item to consume
     */
    public void consume(@NotNull ItemStack item) {
        if (!item.isEdible()) return;
        if (ConsumableConfig.consumables.containsKey(item.getItem())) {
            modifyLiquids(ConsumableConfig.consumables.get(item.getItem()).liquids);
            modifySolids(ConsumableConfig.consumables.get(item.getItem()).solids);
        } else {
            Optional.ofNullable(item.getFoodProperties(Minecraft.getInstance().player)).ifPresent(foodProperties -> {
                modifySolids(foodProperties.getNutrition());
                modifyLiquids(foodProperties.getNutrition() / 4f);
            });
        }
        sync(true);
    }

    /**
     * Tick the metabolism of a player.
     * The metabolism is ticked every tick to simulate the digestion and continence of the player.
     * If the metabolism has changed, then a sync is sent to the server.
     */
    public void tick() {
        tickDigestion();
        tickContinence();
        checkForBladderAccident();
        checkForBowelAccident();
        checkForBladderDesperationEvent();
        checkForBowelsDesperationEvent();
        sync(false);
    }
}
