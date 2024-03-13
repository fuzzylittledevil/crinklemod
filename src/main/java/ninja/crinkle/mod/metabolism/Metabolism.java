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
import ninja.crinkle.mod.events.DesperationEvent;
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
                && isEnabled()
                && !Undergarment.getWornUndergarment(player).isEmpty()
                && (getNumberOneChance() > 0 || getNumberTwoChance() > 0);
    }

    private int getDesperationLevel(int rolls, int safeRolls, double chance) {
        int dangerRolls = rolls - safeRolls;
        if (dangerRolls < 0) return -1;
        return MathUtil.clamp((int) Math.round(dangerRolls * (1 + chance)), 0, 4);
    }

    public int getNumberOneDesperationLevel() {
        return getDesperationLevel(getNumberOneRolls(), getNumberOneSafeRolls(), getNumberOneChance());
    }

    public int getNumberTwoDesperationLevel() {
        return getDesperationLevel(getNumberTwoRolls(), getNumberTwoSafeRolls(), getNumberTwoChance());
    }

    public void tick(int tickCount) {
        if (!isMetabolismEnabled() || tickCount % 20 != 0 || player.getFoodData().getFoodLevel() == 0
                || tickCount / 20 % getTimer() != 0)
            return;

        getMetabolism().ifPresent(m -> {
            if (m.getNumberOneChance() > 0) {
                m.setNumberOneRolls(m.getNumberOneRolls() + 1);
                if (m.getNumberOneRolls() >= getNumberOneSafeRolls()) {
                    double roll = Math.random();
                    int desperationLevel = getNumberOneDesperationLevel() == -1 ? 0 : getNumberOneDesperationLevel();
                    if (m.getNumberOneRolls() >= getNumberOneSafeRolls()
                            && (roll - (double) (desperationLevel * 2) / 100) < m.getNumberOneChance()) {
                        CrinkleMod.EVENT_BUS.post(
                                new AccidentEvent.Bladder(player, 1, AccidentEvent.Side.SERVER));
                        m.setNumberOneRolls(0);
                    } else {
                        CrinkleMod.EVENT_BUS.post(
                                new DesperationEvent.Bladder(player, getNumberOneDesperationLevel(), DesperationEvent.Side.SERVER));
                    }
                }
            }

            if (m.getNumberTwoChance() > 0) {
                m.setNumberTwoRolls(m.getNumberTwoRolls() + 1);
                if (m.getNumberTwoRolls() >= getNumberTwoSafeRolls()) {
                    double roll = Math.random();
                    int desperationLevel = getNumberTwoDesperationLevel() == -1 ? 0 : getNumberTwoDesperationLevel();
                    if (m.getNumberTwoRolls() >= getNumberTwoSafeRolls()
                            && (roll - (double) (desperationLevel * 2) / 100) < m.getNumberTwoChance()) {
                        CrinkleMod.EVENT_BUS.post(
                                new AccidentEvent.Bowels(player, 1, AccidentEvent.Side.SERVER));
                        m.setNumberTwoRolls(0);
                    } else {
                        CrinkleMod.EVENT_BUS.post(
                                new DesperationEvent.Bowels(player, getNumberTwoDesperationLevel(), DesperationEvent.Side.SERVER));
                    }
                }
            }
        });
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

    public boolean isEnabled() {
        return getMetabolism().map(IMetabolism::isEnabled).orElse(false);
    }

    public void setEnabled(boolean enabled) {
        getMetabolism().ifPresent(m -> m.setEnabled(enabled));
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
        getMetabolism().ifPresent(m -> {
            CrinkleMod.EVENT_BUS.post(
                    new AccidentEvent.Bladder(player, 1, side));
        });
    }

    public void voidNumberTwo() {
        CrinkleEvent.Side side = player instanceof ServerPlayer ? CrinkleEvent.Side.SERVER : CrinkleEvent.Side.CLIENT;
        getMetabolism().ifPresent(m -> {
            CrinkleMod.EVENT_BUS.post(
                    new AccidentEvent.Bowels(player, 1, side));
        });
    }

    public enum DesperationLevel {
        NONE(Component.translatable("desperation.crinklemod.none")),
        LOW(Component.translatable("desperation.crinklemod.low")),
        MEDIUM_LOW(Component.translatable("desperation.crinklemod.medium_low")),
        MEDIUM(Component.translatable("desperation.crinklemod.medium")),
        MEDIUM_HIGH(Component.translatable("desperation.crinklemod.medium_high")),
        HIGH(Component.translatable("desperation.crinklemod.high"));

        private final Component label;

        DesperationLevel(Component label) {
            this.label = label;
        }

        public Component getLabel() {
            return label;
        }

        public static DesperationLevel of(int level) {
            return switch (level) {
                case -1 -> NONE;
                case 0 -> LOW;
                case 1 -> MEDIUM_LOW;
                case 2 -> MEDIUM;
                case 3 -> MEDIUM_HIGH;
                case 4 -> HIGH;
                default -> throw new IllegalArgumentException("Invalid desperation level: " + level);
            };
        }
    }
}
