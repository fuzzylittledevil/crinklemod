package ninja.crinkle.mod.undergarment.common;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import ninja.crinkle.mod.lib.common.tooltips.GradientBarTooltip;
import ninja.crinkle.mod.lib.common.util.ColorUtil;
import ninja.crinkle.mod.undergarment.common.capabilities.IUndergarment;
import ninja.crinkle.mod.undergarment.common.capabilities.UndergarmentCapabilities;
import ninja.crinkle.mod.undergarment.common.config.UndergarmentConfig;
import ninja.crinkle.mod.undergarment.common.network.UndergarmentChannel;
import ninja.crinkle.mod.undergarment.common.network.messages.UndergarmentUpdateMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;

public class Undergarment {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int LIQUIDS_COLOR = 0xffffef00;
    private static final int SOLIDS_COLOR = 0xFF836953;
    private final Player player;
    private ItemStack itemStack = ItemStack.EMPTY;
    private boolean isDirty;

    private Undergarment(Player player) {
        this.player = player;
        this.itemStack = getItemStack();
    }

    private Undergarment(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
        this.player = null;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Undergarment of(Player player) {
        return new Undergarment(player);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Undergarment of(ItemStack item) {
        return new Undergarment(item);
    }

    public ItemStack getItemStack() {
        if (player == null) {
            return itemStack;
        }
        for (final ItemStack i : player.getArmorSlots()) {
            if (!LivingEntity.getEquipmentSlotForItem(i).equals(EquipmentSlot.LEGS)) {
                continue;
            }
            if (!UndergarmentConfig.undergarments.containsKey(i.getItem())) {
                continue;
            }
            return i;
        }
        return ItemStack.EMPTY;
    }

    public Optional<IUndergarment> get() {
        ItemStack i = getItemStack();
        if (i.isEmpty()) {
            return Optional.empty();
        }
        return i.getCapability(UndergarmentCapabilities.UNDERGARMENT).resolve();
    }

    public int getMaxLiquids() {
        return get().map(IUndergarment::getMaxLiquids).orElse(0);
    }

    public void setMaxLiquids(int value) {
        get().ifPresent(i -> i.setMaxLiquids(value));
        isDirty = true;
    }

    public int getMaxSolids() {
        return get().map(IUndergarment::getMaxSolids).orElse(0);
    }

    public void setMaxSolids(int value) {
        get().ifPresent(i -> i.setMaxSolids(value));
        isDirty = true;
    }

    public int getLiquids() {
        return get().map(IUndergarment::getLiquids).orElse(0);
    }

    public void setLiquids(int value) {
        get().ifPresent(i -> i.setLiquids(value));
        isDirty = true;
    }

    public int getSolids() {
        return get().map(IUndergarment::getSolids).orElse(0);
    }

    public void setSolids(int value) {
        get().ifPresent(i -> i.setSolids(value));
        isDirty = true;
    }

    public void modifyLiquids(int amount) {
        get().ifPresent(i -> i.setLiquids(i.getLiquids() + amount));
        isDirty = true;
    }

    public void modifySolids(int amount) {
        get().ifPresent(i -> i.setSolids(i.getSolids() + amount));
        isDirty = true;
    }

    public void syncServer(boolean force) {
        if (isDirty || force) {
            LOGGER.debug("Sending undergarment sync to server");
            get().ifPresent(u -> UndergarmentChannel.INSTANCE.sendToServer(new UndergarmentUpdateMessage(u)));
            isDirty = false;
        }
    }

    public void syncClient() {
        if (player instanceof ServerPlayer serverPlayer) {
            LOGGER.debug("Sending undergarment sync to client");
            get().ifPresent(u ->
                    UndergarmentChannel.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer),
                            new UndergarmentUpdateMessage(u)));
        }
    }

    public void tick() {
        ItemStack stack = getItemStack();
        if (stack.isEmpty()) {
            return;
        }
        syncServer(false);
    }

    public GradientBarTooltip getLiquidsTooltip() {
        return new GradientBarTooltip(UndergarmentSettings.LIQUIDS.label(), getLiquids(), getMaxLiquids(),
                        LIQUIDS_COLOR, ColorUtil.darken(LIQUIDS_COLOR, 0.5f),
                        ColorUtil.darken(LIQUIDS_COLOR, 0.25f), 9, 60, 40);
    }

    public GradientBarTooltip getSolidsTooltip() {
        return new GradientBarTooltip(UndergarmentSettings.SOLIDS.label(), getSolids(), getMaxSolids(),
                SOLIDS_COLOR, ColorUtil.darken(SOLIDS_COLOR, 0.5f),
                ColorUtil.darken(SOLIDS_COLOR, 0.25f), 9, 60, 40);
    }
}
