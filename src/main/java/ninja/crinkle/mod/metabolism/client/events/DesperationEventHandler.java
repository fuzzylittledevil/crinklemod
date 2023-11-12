package ninja.crinkle.mod.metabolism.client.events;

import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.crinkle.mod.metabolism.common.capabilities.MetabolismImpl;

/**
 * An event handler that is used to inform the player when a player has a change in desperation.
 *
 * @see DesperationEvent
 * @see ninja.crinkle.mod.CrinkleMod#EVENT_BUS
 * @see MetabolismImpl
 */
public class DesperationEventHandler {
    @SubscribeEvent
    public void onBladderDesperation(DesperationEvent.Bladder event) {
        event.getPlayer().displayClientMessage(Component.literal("You feel the pressure in your bladder grow!"), true);
    }

    @SubscribeEvent
    public void onBowelsDesperation(DesperationEvent.Bowels event) {
        event.getPlayer().displayClientMessage(Component.literal("You feel the need to poop grow!"), true);
    }
}
