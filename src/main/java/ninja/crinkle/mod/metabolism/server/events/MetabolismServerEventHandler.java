package ninja.crinkle.mod.metabolism.server.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.command.ConfigCommand;
import ninja.crinkle.mod.metabolism.common.Metabolism;
import ninja.crinkle.mod.metabolism.server.commands.MetabolismCommand;

/**
 * An event handler that is used to handle forge and Minecraft events.
 *
 * @author Galen
 * @see net.minecraftforge.eventbus.api.Event
 */
public class MetabolismServerEventHandler {
    /**
     * Hook on when commands are registered. This is used to register the metabolism command.
     *
     * @param event The event
     * @see RegisterCommandsEvent
     */
    @SubscribeEvent
    public void onCommandsRegister(RegisterCommandsEvent event) {
        new MetabolismCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Metabolism.of(player).syncClient();
        }
    }
}
