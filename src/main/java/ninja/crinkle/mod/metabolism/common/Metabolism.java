package ninja.crinkle.mod.metabolism.common;

import com.mojang.logging.LogUtils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.PacketDistributor;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.lib.client.ClientHooks;
import ninja.crinkle.mod.lib.client.events.AccidentEvent;
import ninja.crinkle.mod.lib.client.events.DesperationEvent;
import ninja.crinkle.mod.metabolism.common.capabilities.IMetabolism;
import ninja.crinkle.mod.metabolism.common.capabilities.MetabolismCapabilities;
import ninja.crinkle.mod.metabolism.common.config.ConsumableConfig;
import ninja.crinkle.mod.metabolism.common.network.MetabolismChannel;
import ninja.crinkle.mod.metabolism.common.network.messages.MetabolismUpdateMessage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;

public class Metabolism {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final double BLADDER_SMALL_ACCIDENT_THRESHOLD = 0.3;
    private static final double BOWEL_SMALL_ACCIDENT_THRESHOLD = 0.3;
    private static final double BLADDER_LARGE_ACCIDENT_THRESHOLD = 0.5;
    private static final double BOWEL_LARGE_ACCIDENT_THRESHOLD = 0.5;
    private static final double BLADDER_LARGE_ACCIDENT_AMOUNT = 0.9;
    private static final double BOWEL_LARGE_ACCIDENT_AMOUNT = 0.9;

    private boolean recentBladderAccident = false;
    private boolean recentBowelAccident = false;
    private boolean isDirty = false;

    private final Player player;

    private Metabolism(Player player) {
        this.player = player;
    }

    public static Metabolism of(Player player) {
        return new Metabolism(player);
    }

    /**
     * Get the player's metabolism capability.
     */
    private Optional<IMetabolism> getMetabolism() {
        Optional<IMetabolism> metabolism = player.getCapability(MetabolismCapabilities.METABOLISM).resolve();
        if (metabolism.isEmpty())
            LOGGER.warn("Player {} does not have a metabolism capability", player.getDisplayName().getString());
        return metabolism;
    }

    public int getSolids() {
        return getMetabolism().map(IMetabolism::getSolids).orElse(0);
    }

    public int getLiquids() {
        return getMetabolism().map(IMetabolism::getLiquids).orElse(-1);
    }

    public int getBladder() {
        return getMetabolism().map(IMetabolism::getBladder).orElse(0);
    }

    public double getBladderFullness() {
        int capacity = getBladderCapacity();
        return capacity == 0.0 ? 0.0 : (double) getBladder() / getBladderCapacity();
    }

    public int getBowels() {
        return getMetabolism().map(IMetabolism::getBowels).orElse(0);
    }

    public double getBowelFullness() {
        int capacity = getBowelCapacity();
        return capacity == 0.0 ? 0.0 : (double) getBowels() / getBowelCapacity();
    }

    public double getBladderDesperation() {
        return getBladderFullness() * (1 / getBladderContinence());
    }

    public double getBowelDesperation() {
        return getBowelFullness() * (1 / getBowelContinence());
    }

    public int getBladderCapacity() {
        return getMetabolism().map(IMetabolism::getBladderCapacity).orElse(0);
    }

    public int getBowelCapacity() {
        return getMetabolism().map(IMetabolism::getBowelCapacity).orElse(0);
    }

    public int getSolidsRate() {
        return getMetabolism().map(IMetabolism::getSolidsRate).orElse(0);
    }

    public int getLiquidsRate() {
        return getMetabolism().map(IMetabolism::getLiquidsRate).orElse(0);
    }

    public double getBladderContinence() {
        return Math.max(0.1, getMetabolism().map(IMetabolism::getBladderContinence).orElse(0d));
    }

    public double getBowelContinence() {
        return Math.max(0.1, getMetabolism().map(IMetabolism::getBowelContinence).orElse(0d));
    }

    public int getMaxLiquids() {
        return getMetabolism().map(IMetabolism::getMaxLiquids).orElse(0);
    }

    public int getMaxSolids() {
        return getMetabolism().map(IMetabolism::getMaxSolids).orElse(0);
    }

    public void setSolids(int value) {
        getMetabolism().ifPresent(m -> m.setSolids(value));
        isDirty = true;
    }

    public void setLiquids(int value) {
        getMetabolism().ifPresent(m -> m.setLiquids(value));
        isDirty = true;
    }

    public void setBladder(int value) {
        getMetabolism().ifPresent(m -> m.setBladder(value));
        isDirty = true;
    }

    public void setBowels(int value) {
        getMetabolism().ifPresent(m -> m.setBowels(value));
        isDirty = true;
    }

    public void setMaxLiquids(int value) {
        getMetabolism().ifPresent(m -> m.setMaxLiquids(value));
        isDirty = true;
    }

    public void setMaxSolids(int value) {
        getMetabolism().ifPresent(m -> m.setMaxSolids(value));
        isDirty = true;
    }

    public void setLiquidsRate(int value) {
        getMetabolism().ifPresent(m -> m.setLiquidsRate(value));
        isDirty = true;
    }

    public void setSolidsRate(int value) {
        getMetabolism().ifPresent(m -> m.setSolidsRate(value));
        isDirty = true;
    }

    public void setBladderCapacity(int value) {
        getMetabolism().ifPresent(m -> m.setBladderCapacity(value));
        isDirty = true;
    }

    public void setBowelCapacity(int value) {
        getMetabolism().ifPresent(m -> m.setBowelCapacity(value));
        isDirty = true;
    }

    public void setBladderContinence(double value) {
        getMetabolism().ifPresent(m -> m.setBladderContinence(value));
        isDirty = true;
    }

    public void setBowelContinence(double value) {
        getMetabolism().ifPresent(m -> m.setBowelContinence(value));
        isDirty = true;
    }

    /**
     * Modify the amount of liquids in the player's stomach.
     *
     * @param value The amount of liquids to add or remove. Negative values remove liquids.
     */
    public void modifyLiquids(int value) {
        setLiquids(getLiquids() + value);
    }

    /**
     * Modify the amount of solids in the player's stomach.
     *
     * @param value The amount of solids to add or remove. Negative values remove solids.
     */
    public void modifySolids(int value) {
        setSolids(getSolids() + value);
    }

    /**
     * Modify the amount of liquids in the player's bladder.
     *
     * @param value The amount of liquids to add or remove. Negative values remove liquids.
     */
    public void modifyBladder(int value) {
        setBladder(getBladder() + value);
    }

    /**
     * Modify the amount of solids in the player's bowels.
     *
     * @param value The amount of solids to add or remove. Negative values remove solids.
     */
    public void modifyBowels(int value) {
        setBowels(getBowels() + value);
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

    private void checkForBladderSmallAccident() {
        if (getBladderFullness() < BLADDER_SMALL_ACCIDENT_THRESHOLD) return;
        if (recentBladderAccident) return;
        double chance = (1 - getBladderContinence()) * getBladderFullness();
        if (Math.random() > chance) {
            recentBladderAccident = true;
            int amount = (int) (getBladder() * (1 - getBladderContinence()));
            LOGGER.debug("Small bladder accident: amount={} chance={} desperation={} continence={}", amount, chance, getBladderDesperation(), getBladderContinence());
            modifyBladder(-amount);
            CrinkleMod.EVENT_BUS.post(new AccidentEvent.Bladder(player, amount));
        }
    }

    private void checkForBowelSmallAccident() {
        if (getBowelFullness() < BOWEL_SMALL_ACCIDENT_THRESHOLD) return;
        if (recentBowelAccident) return;
        double chance = (1 - getBowelContinence()) * getBowelFullness();
        if (Math.random() > chance) {
            recentBowelAccident = true;
            int amount = (int) (getBowels() * (1 - getBowelContinence()));
            LOGGER.debug("Small bowel accident: amount={} chance={} desperation={} continence={}", amount, chance, getBowelDesperation(), getBowelContinence());
            modifyBowels(-amount);
            CrinkleMod.EVENT_BUS.post(new AccidentEvent.Bowels(player, amount));
        }
    }

    private void checkForBladderLargeAccident() {
        if (getBladderFullness() < BLADDER_LARGE_ACCIDENT_THRESHOLD) return;
        if (recentBladderAccident) return;
        double chance = (1 - getBladderContinence()) * getBladderFullness();
        if (Math.random() > chance) {
            recentBladderAccident = true;
            int amount = (int) (getBladder() * BLADDER_LARGE_ACCIDENT_AMOUNT);
            LOGGER.debug("Large bladder accident: amount={} chance={} desperation={} continence={}", amount, chance, getBladderDesperation(), getBladderContinence());
            modifyBladder(-amount);
            CrinkleMod.EVENT_BUS.post(new AccidentEvent.Bladder(player, amount));
        }
    }

    private void checkForBowelLargeAccident() {
        if (getBowelFullness() < BOWEL_LARGE_ACCIDENT_THRESHOLD) return;
        if (recentBowelAccident) return;
        double chance = (1 - getBowelContinence()) * getBowelFullness();
        if (Math.random() > chance) {
            recentBowelAccident = true;
            int amount = (int) (getBowels() * BOWEL_LARGE_ACCIDENT_AMOUNT);
            LOGGER.debug("Large bowel accident: amount={} chance={} desperation={} continence={}", amount, chance, getBowelDesperation(), getBowelContinence());
            modifyBowels(-amount);
            CrinkleMod.EVENT_BUS.post(new AccidentEvent.Bowels(player, amount));
        }
    }

    /**
     * Check for a bladder desperation event.
     * This method is called every tick to check if the player has a bladder desperation event.
     * An event is posted if the player has a desperation event.
     *
     * @see DesperationEvent.Bladder
     */
    private void checkForBladderDesperationEvent() {
        if (getBladderDesperation() > 0.5 && Math.random() < getBladderDesperation()) {
            CrinkleMod.EVENT_BUS.post(new DesperationEvent.Bladder(player, getBladderDesperation()));
        }
    }

    /**
     * Check for a bowels desperation event.
     * This method is called every tick to check if the player has a bowels desperation event.
     * An event is posted if the player has a desperation event.
     *
     * @see DesperationEvent.Bowels
     */
    private void checkForBowelsDesperationEvent() {
        if (getBowelDesperation() > 0.5 && Math.random() < getBowelDesperation()) {
            CrinkleMod.EVENT_BUS.post(new DesperationEvent.Bowels(player, getBowelDesperation()));
        }
    }

    public void syncServer(boolean force) {
        if (isDirty || force) {
            LOGGER.debug("Sending metabolism sync to server");
            getMetabolism().ifPresent(m -> MetabolismChannel.INSTANCE.sendToServer(new MetabolismUpdateMessage(m)));
            isDirty = false;
        }
    }

    public void syncClient() {
        if (player instanceof ServerPlayer serverPlayer) {
            LOGGER.debug("Sending metabolism sync to client");
            getMetabolism().ifPresent(m -> MetabolismChannel.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new MetabolismUpdateMessage(m)));
        }
    }

    /**
     * Consume an item.
     * This method is called when an item is consumed by a player.
     * This method first checks to see if we have an override for a given item, if not then it uses the item's food properties.
     *
     * @param item The item to consume
     */
    public void consume(@NotNull ItemStack item) {
        if (ConsumableConfig.consumables.containsKey(item.getItem())) {
            modifyLiquids(ConsumableConfig.consumables.get(item.getItem()).liquids);
            modifySolids(ConsumableConfig.consumables.get(item.getItem()).solids);
        } else {
            Optional.ofNullable(item.getFoodProperties(player)).ifPresent(foodProperties -> {
                modifySolids(foodProperties.getNutrition());
                modifyLiquids((int) (foodProperties.getNutrition() / 4f));
            });
        }
        syncServer(true);
    }

    /**
     * Tick the metabolism of a player.
     * The metabolism is ticked every tick to simulate the digestion and continence of the player.
     * If the metabolism has changed, then a sync is sent to the server.
     */
    public void tick() {
        recentBladderAccident = false;
        recentBowelAccident = false;
        tickDigestion();
        checkForBladderSmallAccident();
        checkForBowelSmallAccident();
        checkForBladderLargeAccident();
        checkForBowelLargeAccident();
        // checkForBladderDesperationEvent();
        // checkForBowelsDesperationEvent();
        syncServer(false);
    }

    public static void fetch() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientHooks::fetchMetabolism);
    }
}
