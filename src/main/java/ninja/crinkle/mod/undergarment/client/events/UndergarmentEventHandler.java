package ninja.crinkle.mod.undergarment.client.events;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.lib.client.events.AccidentEvent;
import ninja.crinkle.mod.lib.client.events.LeakEvent;
import ninja.crinkle.mod.undergarment.common.Undergarment;


public class UndergarmentEventHandler {
    @SubscribeEvent
    public void onBladderAccident(AccidentEvent.Bladder event) {
        Style style = Style.EMPTY.withColor(ChatFormatting.YELLOW);
        event.getPlayer().sendSystemMessage(
                Component.translatable("event.crinklemod.undergarment.bladder.accident.text"));
        Undergarment undergarment = Undergarment.of(event.getPlayer());
        undergarment.setLiquids(undergarment.getLiquids() + event.getAmount());
        if (event.getAmount() + undergarment.getLiquids() > undergarment.getMaxLiquids()) {
            int amount = undergarment.getLiquids() - undergarment.getMaxLiquids();
            undergarment.setLiquids(undergarment.getMaxLiquids());
            CrinkleMod.EVENT_BUS.post(new LeakEvent.Liquids(event.getPlayer(), amount, undergarment.getItemStack()));
        } else {
            undergarment.modifyLiquids(event.getAmount());
        }
    }

    @SubscribeEvent
    public void onBowelsAccident(AccidentEvent.Bowels event) {
        Style style = Style.EMPTY.withColor(ChatFormatting.DARK_GREEN);
        event.getPlayer().sendSystemMessage(
                Component.translatable("event.crinklemod.undergarment.bowel.accident.text").withStyle(style));
        Undergarment undergarment = Undergarment.of(event.getPlayer());
        if (event.getAmount() + undergarment.getSolids() > undergarment.getMaxSolids()) {
            int amount = undergarment.getSolids() - undergarment.getMaxSolids();
            undergarment.setSolids(undergarment.getMaxSolids());
            CrinkleMod.EVENT_BUS.post(new LeakEvent.Solids(event.getPlayer(), amount, undergarment.getItemStack()));
        } else {
            undergarment.modifySolids(event.getAmount());
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
