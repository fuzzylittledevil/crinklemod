package ninja.crinkle.mod.metabolism.client.events;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.crinkle.mod.metabolism.client.MetabolismManager;

/**
 * An event handler that is used to handle forge and Minecraft events.
 * @author Galen
 * @see net.minecraftforge.eventbus.api.Event
 */
public class ClientEventHandler {
    /**
     * Hook on when a living entity uses an item. This is used to consume items that are used as food or drink.
     * @see LivingEntityUseItemEvent.Finish
     * @param event The event
     */
    @SubscribeEvent
    public void onLivingEntityUseItem(LivingEntityUseItemEvent.Finish event) {
        if (!event.getItem().isEdible()) return;
        MetabolismManager.INSTANCE.consume(event.getItem());
    }

    /**
     * Hook on when a player ticks. This is used to tick the metabolism of a player.
     * It only ticks the metabolism every 20 ticks, or 1 second.
     * @see TickEvent.PlayerTickEvent
     * @param event The event
     */
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player.tickCount % 20 == 0) {
            MetabolismManager.INSTANCE.tick();
        }
    }
}
