package ninja.crinkle.mod.metabolism.client.events;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.crinkle.mod.metabolism.client.MetabolismManager;
import org.jetbrains.annotations.NotNull;

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
    public void onLivingEntityUseItem(LivingEntityUseItemEvent.@NotNull Finish event) {
        if (!event.getEntity().level().isClientSide) return;
        if (!event.getItem().isEdible()) return;
        MetabolismManager.INSTANCE.consume(event.getItem());
    }

    /**
     * Hook on when a player ticks. This is used to tick the metabolism of a player.
     * It only ticks the metabolism every 120 ticks, or 6 seconds.
     * @see TickEvent.PlayerTickEvent
     * @param event The event
     */
    @SubscribeEvent
    public void onPlayerTick(TickEvent.@NotNull PlayerTickEvent event) {
        if (!event.side.isClient()) return;
        if (event.phase == TickEvent.Phase.END && event.player.tickCount % 120 == 0) {
            MetabolismManager.INSTANCE.tick();
        }
    }
}
