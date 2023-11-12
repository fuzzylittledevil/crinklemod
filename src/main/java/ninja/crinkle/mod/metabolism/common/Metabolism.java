package ninja.crinkle.mod.metabolism.common;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.metabolism.client.events.AccidentEvent;
import ninja.crinkle.mod.metabolism.client.events.DesperationEvent;
import ninja.crinkle.mod.metabolism.common.capabilities.Capabilities;
import ninja.crinkle.mod.metabolism.common.capabilities.IMetabolism;
import ninja.crinkle.mod.metabolism.common.config.ConsumableConfig;
import ninja.crinkle.mod.metabolism.common.config.MetabolismConfig;
import ninja.crinkle.mod.metabolism.common.network.MetabolismChannel;
import ninja.crinkle.mod.metabolism.common.network.messages.UpdateMessage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;

public class Metabolism {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final double BLADDER_SMALL_ACCIDENT_THRESHOLD = 0.5;
    private static final double BOWEL_SMALL_ACCIDENT_THRESHOLD = 0.5;
    private static final double BLADDER_LARGE_ACCIDENT_THRESHOLD = 0.75;
    private static final double BOWEL_LARGE_ACCIDENT_THRESHOLD = 0.75;
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
        return player.getCapability(Capabilities.METABOLISM).resolve();
    }

    public double getSolids() {
        return getMetabolism().map(IMetabolism::getSolids).orElse(0d);
    }

    public double getLiquids() {
        return getMetabolism().map(IMetabolism::getLiquids).orElse(0d);
    }

    public double getBladder() {
        return getMetabolism().map(IMetabolism::getBladder).orElse(0d);
    }

    public double getBladderFullness() {
        return getBladder() / getBladderCapacity();
    }

    public double getBowels() {
        return getMetabolism().map(IMetabolism::getBowels).orElse(0d);
    }

    public double getBowelFullness() {
        return getBowels() / getBowelCapacity();
    }

    public double getBladderDesperation() {
        return getBladderFullness() * (1 / getBladderContinence());
    }

    public double getBowelDesperation() {
        return getBowelFullness() * (1 / getBowelContinence());
    }

    public double getBladderCapacity() {
        return getMetabolism().map(IMetabolism::getBladderCapacity).orElse(0d);
    }

    public double getBowelCapacity() {
        return getMetabolism().map(IMetabolism::getBowelCapacity).orElse(0d);
    }

    public double getSolidsRate() {
        return getMetabolism().map(IMetabolism::getSolidsRate).orElse(0d);
    }

    public double getLiquidsRate() {
        return getMetabolism().map(IMetabolism::getLiquidsRate).orElse(0d);
    }

    public double getBladderContinence() {
        return Math.max(0.1, getMetabolism().map(IMetabolism::getBladderContinence).orElse(0d));
    }

    public double getBowelContinence() {
        return Math.max(0.1, getMetabolism().map(IMetabolism::getBowelContinence).orElse(0d));
    }

    public double getMaxLiquids() {
        return getMetabolism().map(IMetabolism::getMaxLiquids).orElse(0d);
    }

    public double getMaxSolids() {
        return getMetabolism().map(IMetabolism::getMaxSolids).orElse(0d);
    }

    public void setSolids(double value) {
        getMetabolism().ifPresent(m -> m.setSolids(value));
        isDirty = true;
    }

    public void setLiquids(double value) {
        getMetabolism().ifPresent(m -> m.setLiquids(value));
        isDirty = true;
    }

    public void setBladder(double value) {
        getMetabolism().ifPresent(m -> m.setBladder(value));
        isDirty = true;
    }

    public void setBowels(double value) {
        getMetabolism().ifPresent(m -> m.setBowels(value));
        isDirty = true;
    }

    public void setMaxLiquids(double value) {
        getMetabolism().ifPresent(m -> m.setMaxLiquids(value));
        isDirty = true;
    }

    public void setMaxSolids(double value) {
        getMetabolism().ifPresent(m -> m.setMaxSolids(value));
        isDirty = true;
    }

    public void setLiquidsRate(double value) {
        getMetabolism().ifPresent(m -> m.setLiquidsRate(value));
        isDirty = true;
    }

    public void setSolidsRate(double value) {
        getMetabolism().ifPresent(m -> m.setSolidsRate(value));
        isDirty = true;
    }

    public void setBladderCapacity(double value) {
        getMetabolism().ifPresent(m -> m.setBladderCapacity(value));
        isDirty = true;
    }

    public void setBowelCapacity(double value) {
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
    public void modifyLiquids(double value) {
        setLiquids(getLiquids() + value);
    }

    /**
     * Modify the amount of solids in the player's stomach.
     *
     * @param value The amount of solids to add or remove. Negative values remove solids.
     */
    public void modifySolids(double value) {
        setSolids(getSolids() + value);
    }

    /**
     * Modify the amount of liquids in the player's bladder.
     *
     * @param value The amount of liquids to add or remove. Negative values remove liquids.
     */
    public void modifyBladder(double value) {
        setBladder(getBladder() + value);
    }

    /**
     * Modify the amount of solids in the player's bowels.
     *
     * @param value The amount of solids to add or remove. Negative values remove solids.
     */
    public void modifyBowels(double value) {
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
            LOGGER.debug("Small bladder accident: chance={} desperation={} continence={}", chance, getBladderDesperation(), getBladderContinence());
            recentBladderAccident = true;
            double amount = getBladder() * (1 - getBladderContinence());
            modifyBladder(-amount);
            CrinkleMod.EVENT_BUS.post(new AccidentEvent.Bladder(Minecraft.getInstance().player, amount));
        }
    }

    private void checkForBowelSmallAccident() {
        if (getBowelFullness() < BOWEL_SMALL_ACCIDENT_THRESHOLD) return;
        if (recentBowelAccident) return;
        double chance = (1 - getBowelContinence()) * getBowelFullness();
        if (Math.random() > chance) {
            LOGGER.debug("Small bowel accident: chance={} desperation={} continence={}", chance, getBowelDesperation(), getBowelContinence());
            recentBowelAccident = true;
            double amount = getBowels() * (1 - getBowelContinence());
            modifyBowels(-amount);
            CrinkleMod.EVENT_BUS.post(new AccidentEvent.Bowels(Minecraft.getInstance().player, amount));
        }
    }

    private void checkForBladderLargeAccident() {
        if (getBladderFullness() < BLADDER_LARGE_ACCIDENT_THRESHOLD) return;
        if (recentBladderAccident) return;
        double chance = (1 - getBladderContinence()) * getBladderFullness();
        if (Math.random() > chance) {
            LOGGER.debug("Large bladder accident: chance={} desperation={} continence={}", chance, getBladderDesperation(), getBladderContinence());
            recentBladderAccident = true;
            double amount = getBladder() * BLADDER_LARGE_ACCIDENT_AMOUNT;
            modifyBladder(-amount);
            CrinkleMod.EVENT_BUS.post(new AccidentEvent.Bladder(Minecraft.getInstance().player, amount));
        }
    }

    private void checkForBowelLargeAccident() {
        if (getBowelFullness() < BOWEL_LARGE_ACCIDENT_THRESHOLD) return;
        if (recentBowelAccident) return;
        double chance = (1 - getBowelContinence()) * getBowelFullness();
        if (Math.random() > chance) {
            LOGGER.debug("Large bowel accident: chance={} desperation={} continence={}", chance, getBowelDesperation(), getBowelContinence());
            recentBowelAccident = true;
            double amount = getBowels() * BOWEL_LARGE_ACCIDENT_AMOUNT;
            modifyBowels(-amount);
            CrinkleMod.EVENT_BUS.post(new AccidentEvent.Bowels(Minecraft.getInstance().player, amount));
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
            CrinkleMod.EVENT_BUS.post(new DesperationEvent.Bladder(Minecraft.getInstance().player, getBladderDesperation()));
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
            CrinkleMod.EVENT_BUS.post(new DesperationEvent.Bowels(Minecraft.getInstance().player, getBowelDesperation()));
        }
    }

    public void sync(boolean force) {
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
     *
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
        recentBladderAccident = false;
        recentBowelAccident = false;
        tickDigestion();
        checkForBladderSmallAccident();
        checkForBowelSmallAccident();
        checkForBladderLargeAccident();
        checkForBowelLargeAccident();
        // checkForBladderDesperationEvent();
        // checkForBowelsDesperationEvent();
        sync(false);
    }
}
