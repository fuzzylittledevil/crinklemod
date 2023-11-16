package ninja.crinkle.mod.metabolism.common.network.messages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import ninja.crinkle.mod.metabolism.common.Metabolism;
import ninja.crinkle.mod.metabolism.common.capabilities.IMetabolism;
import ninja.crinkle.mod.metabolism.common.capabilities.MetabolismImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * A message that is used to fetch the metabolism of a player from the server.
 *
 * @see MetabolismImpl
 */
public class MetabolismFetchMessage {
    /**
     * Create a new fetch message
     *
     * @see IMetabolism
     */
    public MetabolismFetchMessage() {}

    public static void encoder(MetabolismFetchMessage msg, FriendlyByteBuf buf) {}
    public static MetabolismFetchMessage decoder(FriendlyByteBuf buf) { return new MetabolismFetchMessage(); }

    public void messageConsumer(@NotNull Supplier<NetworkEvent.Context> ctx) {
        Optional.ofNullable((Player) ctx.get().getSender()).ifPresent(p -> Metabolism.of(p).syncClient());
    }
}