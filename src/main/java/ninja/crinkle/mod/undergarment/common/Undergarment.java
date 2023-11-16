package ninja.crinkle.mod.undergarment.common;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.lib.client.events.LeakEvent;
import ninja.crinkle.mod.metabolism.common.network.MetabolismChannel;
import ninja.crinkle.mod.metabolism.common.network.messages.MetabolismUpdateMessage;
import ninja.crinkle.mod.undergarment.common.capabilities.IUndergarment;
import ninja.crinkle.mod.undergarment.common.capabilities.UndergarmentCapabilities;
import ninja.crinkle.mod.undergarment.common.config.UndergarmentConfig;
import ninja.crinkle.mod.undergarment.common.network.UndergarmentChannel;
import ninja.crinkle.mod.undergarment.common.network.messages.UndergarmentUpdateMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.Optional;

public class Undergarment {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Player player;
    private boolean isDirty;

    private Undergarment(Player player) {
        this.player = player;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Undergarment of(Player player) {
        return new Undergarment(player);
    }

    public ItemStack getItemStack() {
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

    public Optional<IUndergarment> getUndergarment() {
        ItemStack i = getItemStack();
        if (i.isEmpty()) {
            return Optional.empty();
        }
        return i.getCapability(UndergarmentCapabilities.UNDERGARMENT).resolve();
    }

    public boolean isWearingUndergarment() {
        return getUndergarment().isPresent();
    }

    public int getMaxLiquids() {
        return getUndergarment().map(IUndergarment::getMaxLiquids).orElse(0);
    }

    public void setMaxLiquids(int value) {
        getUndergarment().ifPresent(i -> i.setMaxLiquids(value));
        isDirty = true;
    }

    public int getMaxSolids() {
        return getUndergarment().map(IUndergarment::getMaxSolids).orElse(0);
    }

    public void setMaxSolids(int value) {
        getUndergarment().ifPresent(i -> i.setMaxSolids(value));
        isDirty = true;
    }

    public int getLiquids() {
        return getUndergarment().map(IUndergarment::getLiquids).orElse(0);
    }

    public void setLiquids(int value) {
        getUndergarment().ifPresent(i -> i.setLiquids(value));
        isDirty = true;
    }

    public int getSolids() {
        return getUndergarment().map(IUndergarment::getSolids).orElse(0);
    }

    public void setSolids(int value) {
        getUndergarment().ifPresent(i -> i.setSolids(value));
        isDirty = true;
    }

    public void modifyLiquids(int amount) {
        getUndergarment().ifPresent(i -> i.setLiquids(i.getLiquids() + amount));
        isDirty = true;
    }

    public void modifySolids(int amount) {
        getUndergarment().ifPresent(i -> i.setSolids(i.getSolids() + amount));
        isDirty = true;
    }

    public void syncServer(boolean force) {
        if (isDirty || force) {
            LOGGER.debug("Sending undergarment sync to server");
            getUndergarment().ifPresent(u -> UndergarmentChannel.INSTANCE.sendToServer(new UndergarmentUpdateMessage(u)));
            isDirty = false;
        }
    }

    public void syncClient() {
        if (player instanceof ServerPlayer serverPlayer) {
            LOGGER.debug("Sending undergarment sync to client");
            getUndergarment().ifPresent(u ->
                    UndergarmentChannel.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer),
                            new UndergarmentUpdateMessage(u)));
        }
    }

    public void tick() {
        ItemStack stack = getItemStack();
        if(stack.isEmpty()) {
            return;
        }syncServer(false);
    }
}
