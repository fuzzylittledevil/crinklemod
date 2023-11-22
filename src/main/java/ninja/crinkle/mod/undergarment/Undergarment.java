package ninja.crinkle.mod.undergarment;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import ninja.crinkle.mod.capabilities.IUndergarment;
import ninja.crinkle.mod.capabilities.UndergarmentImpl;
import ninja.crinkle.mod.config.UndergarmentConfig;
import ninja.crinkle.mod.tooltips.GradientBarTooltip;
import ninja.crinkle.mod.util.ColorUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class Undergarment {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final int LIQUIDS_COLOR = 0xffffef00;
    public static final int SOLIDS_COLOR = 0xFF836953;
    private final ItemStack itemStack;
    private final IUndergarment capability;

    private Undergarment(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
        IUndergarment u = new UndergarmentImpl(itemStack.getItem());
        u.deserializeNBT(itemStack.getOrCreateTagElement("undergarment"));
        this.capability = u;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Undergarment of(ItemStack item) {
        return new Undergarment(item);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public static ItemStack getWornUndergarment(@NotNull Player player) {
        for (final ItemStack i : player.getArmorSlots()) {
            if (!LivingEntity.getEquipmentSlotForItem(i).equals(EquipmentSlot.LEGS)) {
                continue;
            }
            if (!UndergarmentConfig.undergarments.containsKey(i.getItem())) {
                continue;
            }
            return i;
        }
        LOGGER.debug("No undergarment found for player: {}", player);
        return ItemStack.EMPTY;
    }

    public static boolean hasUndergarmentData(@NotNull ItemStack itemStack) {
        return UndergarmentConfig.undergarments.containsKey(itemStack.getItem());
    }

    public int getMaxLiquids() {
        return capability.getMaxLiquids();
    }

    public void setMaxLiquids(int value) {
        capability.setMaxLiquids(value);
        capability.save(itemStack);
    }

    public int getMaxSolids() {
        return capability.getMaxSolids();
    }

    public void setMaxSolids(int value) {
        capability.setMaxSolids(value);
        capability.save(itemStack);
    }

    public int getLiquids() {
        return capability.getLiquids();
    }

    public void setLiquids(int value) {
        capability.setLiquids(value);
        capability.save(itemStack);
    }

    public int getSolids() {
        return capability.getSolids();
    }

    public void setSolids(int value) {
        capability.setSolids(value);
        capability.save(itemStack);
    }

    public void modifyLiquids(int amount) {
        capability.setLiquids(capability.getLiquids() + amount);
        capability.save(itemStack);
    }

    public void modifySolids(int amount) {
        capability.setSolids(capability.getSolids() + amount);
        capability.save(itemStack);
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
