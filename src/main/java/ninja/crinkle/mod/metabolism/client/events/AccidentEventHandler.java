package ninja.crinkle.mod.metabolism.client.events;

import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.crinkle.mod.metabolism.common.capabilities.MetabolismImpl;

/**
 * An event handler that is used to display messages when a player has an accident.
 *
 * @see AccidentEvent
 * @see ninja.crinkle.mod.CrinkleMod#EVENT_BUS
 * @see MetabolismImpl
 */
public class AccidentEventHandler {
    @SubscribeEvent
    public void onBladderAccident(AccidentEvent.Bladder event) {
        String amount = event.getAmount() < 50 ? "a little" : "a lot";
        event.getPlayer().displayClientMessage(Component.literal("You wet yourself " + amount + "!"), true);
    }

    @SubscribeEvent
    public void onBowelsAccident(AccidentEvent.Bowels event) {
        String amount = event.getAmount() < 50 ? "a little" : "a lot";
        event.getPlayer().displayClientMessage(Component.literal("You pooped yourself " + amount + "!"), true);
    }
}
