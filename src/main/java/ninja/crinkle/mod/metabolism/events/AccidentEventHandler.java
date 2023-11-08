package ninja.crinkle.mod.metabolism.events;

import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * An event handler that is used to display messages when a player has an accident.
 * @see AccidentEvent
 * @see ninja.crinkle.mod.CrinkleMod#EVENT_BUS
 * @see ninja.crinkle.mod.metabolism.capabilities.MetabolismImpl
 */
public class AccidentEventHandler {
    @SubscribeEvent
    public void onBladderAccident(AccidentEvent.Bladder event) {
        event.getPlayer().displayClientMessage(Component.literal("You wet yourself!"), true);
    }

    @SubscribeEvent
    public void onBowelsAccident(AccidentEvent.Bowels event) {
        event.getPlayer().displayClientMessage(Component.literal("You pooped yourself!"), true);
    }
}
