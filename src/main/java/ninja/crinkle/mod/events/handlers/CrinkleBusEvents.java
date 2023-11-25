package ninja.crinkle.mod.events.handlers;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.PacketDistributor;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.events.AccidentEvent;
import ninja.crinkle.mod.events.LeakEvent;
import ninja.crinkle.mod.network.CrinkleChannel;
import ninja.crinkle.mod.network.messages.AccidentEventMessage;
import ninja.crinkle.mod.undergarment.Undergarment;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;


public class CrinkleBusEvents {
    private static final Logger LOGGER = LogUtils.getLogger();
    @SubscribeEvent
    public void propagateBladderAccident(AccidentEvent.Bladder event) {
        AccidentEvent.Side currentSide = event.getPlayer() instanceof ServerPlayer ?
                AccidentEvent.Side.SERVER : AccidentEvent.Side.CLIENT;
        if(currentSide != event.getSide()) {
            return;
        }
        switch(event.getSide()) {
            case CLIENT -> CrinkleChannel.INSTANCE.sendToServer(
                            new AccidentEventMessage(AccidentEventMessage.AccidentType.BLADDER, event.getAmount()));
            case SERVER -> CrinkleChannel.INSTANCE.send(PacketDistributor.PLAYER.with(
                    () -> (ServerPlayer) event.getPlayer()),
                    new AccidentEventMessage(AccidentEventMessage.AccidentType.BLADDER, event.getAmount()));
        }
    }

    @SubscribeEvent
    public void propagateBowelsAccident(AccidentEvent.Bowels event) {
        AccidentEvent.Side currentSide = event.getPlayer() instanceof ServerPlayer ?
                AccidentEvent.Side.SERVER : AccidentEvent.Side.CLIENT;
        if(currentSide != event.getSide()) {
            return;
        }
        switch(event.getSide()) {
            case CLIENT -> CrinkleChannel.INSTANCE.sendToServer(
                            new AccidentEventMessage(AccidentEventMessage.AccidentType.BOWELS, event.getAmount()));
            case SERVER -> CrinkleChannel.INSTANCE.send(PacketDistributor.PLAYER.with(
                    () -> (ServerPlayer) event.getPlayer()),
                    new AccidentEventMessage(AccidentEventMessage.AccidentType.BOWELS, event.getAmount()));
        }
    }

    @SubscribeEvent
    public void onBladderAccident(AccidentEvent.Bladder event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            ItemStack itemStack = Undergarment.getWornUndergarment(event.getPlayer());
            if (itemStack.isEmpty()) {
                //TODO: add message for no pants
                return;
            }
            Style style = Style.EMPTY.withColor(ChatFormatting.YELLOW);
            event.getPlayer().sendSystemMessage(
                    Component.translatable("event.crinklemod.undergarment.bladder.accident.text").withStyle(style));
            Undergarment undergarment = Undergarment.of(itemStack);
            LOGGER.debug("Player {} had a bladder accident with {} amount of liquids", event.getPlayer().getName().getString(), event.getAmount());
            if (event.getAmount() + undergarment.getLiquids() > undergarment.getMaxLiquids()) {
                int amount = undergarment.getLiquids() - undergarment.getMaxLiquids();
                undergarment.setLiquids(undergarment.getMaxLiquids());
                CrinkleMod.EVENT_BUS.post(new LeakEvent.Liquids(event.getPlayer(), amount, undergarment.getItemStack(),
                        AccidentEvent.Side.SERVER));
            } else {
                undergarment.modifyLiquids(event.getAmount());
            }
        }
    }

    @SubscribeEvent
    public void onBowelsAccident(AccidentEvent.Bowels event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            ItemStack itemStack = Undergarment.getWornUndergarment(event.getPlayer());
            if (itemStack.isEmpty()) {
                //TODO: add message for no pants
                return;
            }
            Style style = Style.EMPTY.withColor(ChatFormatting.DARK_GREEN);
            event.getPlayer().sendSystemMessage(
                    Component.translatable("event.crinklemod.undergarment.bowel.accident.text").withStyle(style));
            Undergarment undergarment = Undergarment.of(itemStack);
            if (event.getAmount() + undergarment.getSolids() > undergarment.getMaxSolids()) {
                int amount = undergarment.getSolids() - undergarment.getMaxSolids();
                undergarment.setSolids(undergarment.getMaxSolids());
                CrinkleMod.EVENT_BUS.post(new LeakEvent.Solids(event.getPlayer(), amount, undergarment.getItemStack(),
                        AccidentEvent.Side.SERVER));
            } else {
                undergarment.modifySolids(event.getAmount());
            }
        }
    }

    @SubscribeEvent
    public void onLiquidsLeakAccident(LeakEvent.Liquids event) {
        Style style = Style.EMPTY.withColor(ChatFormatting.YELLOW).applyFormat(ChatFormatting.BOLD);
        event.getPlayer().sendSystemMessage(
                Component.translatable("event.crinklemod.undergarment.liquids.leak.text").withStyle(style));
    }

    @SubscribeEvent
    public void onSolidsLeakAccident(LeakEvent.Solids event) {
        Style style = Style.EMPTY.withColor(ChatFormatting.DARK_GREEN).applyFormat(ChatFormatting.BOLD);
        event.getPlayer().sendSystemMessage(
                Component.translatable("event.crinklemod.undergarment.solids.leak.text").withStyle(style));
    }
}
