package ninja.crinkle.mod.metabolism.events;

import com.mojang.logging.LogUtils;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.command.ConfigCommand;
import ninja.crinkle.mod.metabolism.capabilities.IMetabolism;
import ninja.crinkle.mod.metabolism.capabilities.Metabolism;
import ninja.crinkle.mod.metabolism.capabilities.MetabolismImpl;
import ninja.crinkle.mod.metabolism.commands.MetabolismCommand;
import org.slf4j.Logger;

/**
 * An event handler that is used to handle forge and Minecraft events.
 * @author Galen
 * @see net.minecraftforge.eventbus.api.Event
 */
public class ForgeEventHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Hook on when a living entity uses an item. This is used to consume items that are used as food or drink.
     * @see LivingEntityUseItemEvent.Finish
     * @param event The event
     */
    @SubscribeEvent
    public void onLivingEntityUseItem(LivingEntityUseItemEvent.Finish event) {
        if (!event.getEntity().isControlledByLocalInstance() || !event.getItem().isEdible()) return;
        IMetabolism metabolism = event.getEntity().getCapability(Metabolism.INSTANCE).orElse(new MetabolismImpl());
        metabolism.consume(event.getItem(), event.getEntity());
    }

    /**
     * Hook on when commands are registered. This is used to register the metabolism command.
     * @see RegisterCommandsEvent
     * @param event The event
     */
    @SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent event) {
        new MetabolismCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    /**
     * Hook on when a player ticks. This is used to tick the metabolism of a player.
     * It only ticks the metabolism every 20 ticks, or 1 second.
     * @see TickEvent.PlayerTickEvent
     * @param event The event
     */
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.side.isClient()) return;
        if (event.phase == TickEvent.Phase.END && event.player.tickCount % 20 == 0) {
            event.player.getCapability(Metabolism.INSTANCE).ifPresent(metabolism -> metabolism.tick(event.player));
        }
    }

    /**
     * Hook on when a player logs in. This is used to sync the metabolism of a player to the client or server.
     * @see PlayerEvent.PlayerLoggedInEvent
     * @param event The event
     */
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().isLocalPlayer()) return;
        event.getEntity().getCapability(Metabolism.INSTANCE).ifPresent(metabolism -> metabolism.sync(event.getEntity()));
    }
}
