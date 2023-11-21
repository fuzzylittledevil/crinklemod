package ninja.crinkle.mod.events.handlers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.events.AccidentEvent;
import ninja.crinkle.mod.events.LeakEvent;
import ninja.crinkle.mod.undergarment.Undergarment;


public class UndergarmentEventHandler {
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
            if (event.getAmount() + undergarment.getLiquids() > undergarment.getMaxLiquids()) {
                int amount = undergarment.getLiquids() - undergarment.getMaxLiquids();
                undergarment.setLiquids(undergarment.getMaxLiquids());
                CrinkleMod.EVENT_BUS.post(new LeakEvent.Liquids(event.getPlayer(), amount, undergarment.getItemStack()));
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
                CrinkleMod.EVENT_BUS.post(new LeakEvent.Solids(event.getPlayer(), amount, undergarment.getItemStack()));
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
