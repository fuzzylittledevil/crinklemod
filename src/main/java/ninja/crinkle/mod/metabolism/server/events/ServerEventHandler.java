package ninja.crinkle.mod.metabolism.server.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.command.ConfigCommand;
import ninja.crinkle.mod.metabolism.common.capabilities.Capabilities;
import ninja.crinkle.mod.metabolism.common.network.MetabolismChannel;
import ninja.crinkle.mod.metabolism.common.network.messages.UpdateMessage;
import ninja.crinkle.mod.metabolism.server.commands.MetabolismCommand;

/**
 * An event handler that is used to handle forge and Minecraft events.
 * @author Galen
 * @see net.minecraftforge.eventbus.api.Event
 */
public class ServerEventHandler {
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
     * Hook on when a player logs in. This is used to sync the metabolism of a player to the client.
     * @see PlayerEvent.PlayerLoggedInEvent
     * @param event The event
     */
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(Capabilities.METABOLISM).ifPresent(m ->
                    MetabolismChannel.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new UpdateMessage(m))
            );
        }
    }
}
