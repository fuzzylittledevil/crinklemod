package ninja.crinkle.mod.metabolism;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.network.PacketDistributor;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.api.ServerUpdater;
import ninja.crinkle.mod.capabilities.IMetabolism;
import ninja.crinkle.mod.capabilities.MetabolismCapabilities;
import ninja.crinkle.mod.events.AccidentEvent;
import ninja.crinkle.mod.events.CrinkleEvent;
import ninja.crinkle.mod.network.CrinkleChannel;
import ninja.crinkle.mod.network.messages.MetabolismUpdateMessage;
import ninja.crinkle.mod.undergarment.Undergarment;
import ninja.crinkle.mod.util.MathUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;

public class Metabolism implements ServerUpdater {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Player player;

    private Metabolism(Player player) {
        this.player = player;
    }

    private enum MetabolismDataType {
        NUMBER_ONE,
        NUMBER_TWO;

        public void setRolls(Player player, int rolls) {
            switch (this) {
                case NUMBER_ONE -> Metabolism.of(player).setNumberOneRolls(rolls);
                case NUMBER_TWO -> Metabolism.of(player).setNumberTwoRolls(rolls);
            }
        }
    }

    private static class MetabolismData {
        MetabolismDataType type;
        Player player;
        boolean enabled;
        int rolls;
        int safeRolls;
        double chance;
        CrinkleEvent.Type accidentType = CrinkleEvent.Type.NONE;

        protected DesperationLevel getDesperationLevel() {
            int dangerRolls = rolls - safeRolls;
            if (dangerRolls < 0) return DesperationLevel.NONE;
            return DesperationLevel.of(MathUtil.clamp((int) Math.round(dangerRolls * (1 + chance)), 0, 4));
        }

        private void triggerAccident(boolean additionalAccident) {
            accidentType = switch (type) {
                case NUMBER_ONE -> CrinkleEvent.Type.BLADDER;
                case NUMBER_TWO -> CrinkleEvent.Type.BOWEL;
            };
            if (additionalAccident) {
                rollAdditionalAccident();
            }
            rolls = 0;
            updateMetabolism();
        }

        protected void tick() {
            if (!enabled) return;
            rolls++;
            updateMetabolism();
            if (rolls < safeRolls) return;
            double roll = Math.random();
            int desperationLevel = getDesperationLevel() == DesperationLevel.NONE ? 0 : getDesperationLevel().getLevel();
            if (rolls >= safeRolls && (roll - (double) (desperationLevel * 2) / 100) < chance) {
                triggerAccident(true);
            }
        }

        private void rollAdditionalAccident() {
            MetabolismData data = switch (type) {
                case NUMBER_ONE -> MetabolismData.of(player, MetabolismDataType.NUMBER_TWO);
                case NUMBER_TWO -> MetabolismData.of(player, MetabolismDataType.NUMBER_ONE);
            };
            double roll = Math.random();
            if (roll < data.chance) {
                data.triggerAccident(false);
                if (data.accidentType != CrinkleEvent.Type.NONE) {
                    accidentType = CrinkleEvent.Type.BOTH;
                }
            }
        }

        private void updateMetabolism() {
            type.setRolls(player, rolls);
        }

        protected static MetabolismData of(Player player, MetabolismDataType type) {
            Metabolism metabolism = Metabolism.of(player);
            MetabolismData data = new MetabolismData();
            data.player = player;
            data.type = type;
            if (type == MetabolismDataType.NUMBER_ONE) {
                data.enabled = metabolism.isNumberOneEnabled();
                data.rolls = metabolism.getNumberOneRolls();
                data.safeRolls = metabolism.getNumberOneSafeRolls();
                data.chance = metabolism.getNumberOneChance();
            } else {
                data.enabled = metabolism.isNumberTwoEnabled();
                data.rolls = metabolism.getNumberTwoRolls();
                data.safeRolls = metabolism.getNumberTwoSafeRolls();
                data.chance = metabolism.getNumberTwoChance();
            }
            return data;
        }
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Metabolism of(ICapabilityProvider provider) {
        if (provider instanceof Player player)
            return new Metabolism(player);
        throw new IllegalArgumentException("Cannot create metabolism for non-player entity");
    }

    /**
     * Get the player's metabolism capability.
     */
    private @NotNull Optional<IMetabolism> getMetabolism() {
        Optional<IMetabolism> metabolism = player.getCapability(MetabolismCapabilities.METABOLISM).resolve();
        if (metabolism.isEmpty())
            LOGGER.warn("Player {} does not have a metabolism capability", player.getDisplayName().getString());
        return metabolism;
    }

    public void syncServer() {
        if (player instanceof ServerPlayer) return;
        LOGGER.debug("Sending metabolism sync to server");
        getMetabolism().ifPresent(m -> CrinkleChannel.INSTANCE.sendToServer(new MetabolismUpdateMessage(m)));
    }

    public void syncClient() {
        if (player instanceof ServerPlayer serverPlayer) {
            LOGGER.debug("Sending metabolism sync to client");
            getMetabolism().ifPresent(m ->
                    CrinkleChannel.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer),
                            new MetabolismUpdateMessage(m)));
        }
    }

    private boolean isMetabolismEnabled() {
        return getMetabolism().isPresent() && getTimer() > 0
                && (isNumberOneEnabled() || isNumberTwoEnabled())
                && !Undergarment.getWornUndergarment(player).isEmpty();
    }

    private DesperationLevel getDesperationLevel(int rolls, int safeRolls, double chance) {
        int dangerRolls = rolls - safeRolls;
        if (dangerRolls < 0) return DesperationLevel.NONE;
        return DesperationLevel.of(MathUtil.clamp((int) Math.round(dangerRolls * (1 + chance)), 0, 4));
    }

    public DesperationLevel getNumberOneDesperationLevel() {
        return getDesperationLevel(getNumberOneRolls(), getNumberOneSafeRolls(), getNumberOneChance());
    }

    public DesperationLevel getNumberTwoDesperationLevel() {
        return getDesperationLevel(getNumberTwoRolls(), getNumberTwoSafeRolls(), getNumberTwoChance());
    }

    public boolean isNumberOneDesperate() {
        return getNumberOneDesperationLevel().compareTo(DesperationLevel.LOW) > 0;
    }

    public boolean isNumberTwoDesperate() {
        return getNumberTwoDesperationLevel().compareTo(DesperationLevel.LOW) > 0;
    }

    public void tick(int tickCount) {
        if (!isMetabolismEnabled() || tickCount % 20 != 0 || player.getFoodData().getFoodLevel() == 0
                || tickCount / 20 % getTimer() != 0)
            return;
        MetabolismData one = MetabolismData.of(player, MetabolismDataType.NUMBER_ONE);
        MetabolismData two = MetabolismData.of(player, MetabolismDataType.NUMBER_TWO);
        one.tick();
        if (one.accidentType != CrinkleEvent.Type.BOTH) {
            two.tick();
        }

        CrinkleEvent.Type eventType;
        if (two.accidentType != CrinkleEvent.Type.NONE && one.accidentType != CrinkleEvent.Type.NONE) {
            eventType = CrinkleEvent.Type.BOTH;
        } else if (two.accidentType != CrinkleEvent.Type.NONE) {
            eventType = two.accidentType;
        } else {
            eventType = one.accidentType;
        }

        if (eventType != CrinkleEvent.Type.NONE) {
            CrinkleMod.EVENT_BUS.post(new AccidentEvent(player, 1, CrinkleEvent.Side.SERVER, eventType));
        }
        syncClient();
    }

    public int getTimer() {
        return getMetabolism().map(IMetabolism::getTimer).orElse(0);
    }

    public void setTimer(int timer) {
        getMetabolism().ifPresent(m -> m.setTimer(timer));
        syncClient();
    }

    public int getNumberOneRolls() {
        return getMetabolism().map(IMetabolism::getNumberOneRolls).orElse(0);
    }

    public void setNumberOneRolls(int rolls) {
        getMetabolism().ifPresent(m -> m.setNumberOneRolls(rolls));
        syncClient();
    }

    public int getNumberOneSafeRolls() {
        return getMetabolism().map(IMetabolism::getNumberOneSafeRolls).orElse(0);
    }

    public void setNumberOneSafeRolls(int safeRolls) {
        getMetabolism().ifPresent(m -> m.setNumberOneSafeRolls(safeRolls));
        syncClient();
    }

    public double getNumberOneChance() {
        return getMetabolism().map(IMetabolism::getNumberOneChance).orElse(0.0);
    }

    public void setNumberOneChance(double numberOneChance) {
        getMetabolism().ifPresent(m -> m.setNumberOneChance(numberOneChance));
        syncClient();
    }

    public double getNumberTwoChance() {
        return getMetabolism().map(IMetabolism::getNumberTwoChance).orElse(0.0);
    }

    public void setNumberTwoChance(double numberTwoChance) {
        getMetabolism().ifPresent(m -> m.setNumberTwoChance(numberTwoChance));
        syncClient();
    }

    public boolean isNumberOneEnabled() {
        return getMetabolism().map(IMetabolism::isNumberOneEnabled).orElse(false);
    }

    public void setNumberOneEnabled(boolean enabled) {
        getMetabolism().ifPresent(m -> m.setNumberOneEnabled(enabled));
        syncClient();
    }

    public boolean isNumberTwoEnabled() {
        return getMetabolism().map(IMetabolism::isNumberTwoEnabled).orElse(false);
    }

    public void setNumberTwoEnabled(boolean enabled) {
        getMetabolism().ifPresent(m -> m.setNumberTwoEnabled(enabled));
        syncClient();
    }

    public int getNumberTwoRolls() {
        return getMetabolism().map(IMetabolism::getNumberTwoRolls).orElse(0);
    }

    public void setNumberTwoRolls(int numberTwoRolls) {
        getMetabolism().ifPresent(m -> m.setNumberTwoRolls(numberTwoRolls));
        syncClient();
    }

    public int getNumberTwoSafeRolls() {
        return getMetabolism().map(IMetabolism::getNumberTwoSafeRolls).orElse(0);
    }

    public void setNumberTwoSafeRolls(int numberTwoSafeRolls) {
        getMetabolism().ifPresent(m -> m.setNumberTwoSafeRolls(numberTwoSafeRolls));
        syncClient();
    }

    public void voidNumberOne() {
        CrinkleEvent.Side side = player instanceof ServerPlayer ? CrinkleEvent.Side.SERVER : CrinkleEvent.Side.CLIENT;
        getMetabolism().ifPresent(m -> CrinkleMod.EVENT_BUS.post(new AccidentEvent(player, 1, side, CrinkleEvent.Type.BLADDER)));
    }

    public void voidNumberTwo() {
        CrinkleEvent.Side side = player instanceof ServerPlayer ? CrinkleEvent.Side.SERVER : CrinkleEvent.Side.CLIENT;
        getMetabolism().ifPresent(m -> CrinkleMod.EVENT_BUS.post(new AccidentEvent(player, 1, side, CrinkleEvent.Type.BOWEL)));
    }

    public int getIndicatorPositionX() {
        return getMetabolism().map(IMetabolism::getIndicatorPositionX).orElse(0);
    }

    public int getIndicatorPositionY() {
        return getMetabolism().map(IMetabolism::getIndicatorPositionY).orElse(0);
    }

    public void setIndicatorPositionX(int x) {
        getMetabolism().ifPresent(m -> m.setIndicatorPositionX(x));
        syncClient();
    }

    public void setIndicatorPositionY(int y) {
        getMetabolism().ifPresent(m -> m.setIndicatorPositionY(y));
        syncClient();
    }

    public enum DesperationLevel implements Comparable<DesperationLevel>  {
        NONE(Component.translatable("desperation.crinklemod.none"), -1),
        LOW(Component.translatable("desperation.crinklemod.low"), 0),
        MEDIUM_LOW(Component.translatable("desperation.crinklemod.medium_low"), 1),
        MEDIUM(Component.translatable("desperation.crinklemod.medium"), 2),
        MEDIUM_HIGH(Component.translatable("desperation.crinklemod.medium_high"), 3),
        HIGH(Component.translatable("desperation.crinklemod.high"), 4);

        private final Component label;
        private final int level;

        DesperationLevel(Component label, int level) {
            this.label = label;
            this.level = level;
        }

        public Component getLabel() {
            return label;
        }

        public int getLevel() {
            return level;
        }

        public static DesperationLevel of(int level) {
            for (DesperationLevel desperationLevel : values())
                if (desperationLevel.level == level)
                    return desperationLevel;
            throw new IllegalArgumentException("Invalid desperation level: " + level);
        }

        public static DesperationLevel max(DesperationLevel a, DesperationLevel b) {
            return a.level > b.level ? a : b;
        }
    }
}
