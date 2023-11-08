package ninja.crinkle.mod.metabolism.network;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.metabolism.capabilities.Metabolism;
import ninja.crinkle.mod.metabolism.capabilities.MetabolismImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * A network channel that is used to sync the metabolism of a player to the client or server.
 * @see UpdateMessage
 * @see net.minecraftforge.network.simple.SimpleChannel
 */
public class MetabolismChannel {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(CrinkleMod.MODID, Metabolism.NAME))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void register() {
        int id = 0;
        INSTANCE.messageBuilder(UpdateMessage.class, ++id)
                .decoder(UpdateMessage::decoder)
                .encoder(UpdateMessage::encoder)
                .consumerMainThread(UpdateMessage::messageConsumer)
                .add();
    }

    /**
     * A message that is used to sync the metabolism of a player to the client or server.
     * @see MetabolismImpl
     */
    public static class UpdateMessage {
        private final double liquids;
        private final double solids;
        private final double bladder;
        private final double bowels;
        private final double bladderDesperation;
        private final double bowelsDesperation;

        /**
         * Create a new update message from a metabolism
         * @see MetabolismImpl
         * @param metabolism The metabolism to create the message from
         */
        public UpdateMessage(@NotNull MetabolismImpl metabolism) {
            this.liquids = metabolism.getLiquids();
            this.solids = metabolism.getSolids();
            this.bladder = metabolism.getBladder();
            this.bowels = metabolism.getBowels();
            this.bladderDesperation = metabolism.getBladderDesperation();
            this.bowelsDesperation = metabolism.getBowelsDesperation();
        }

        /**
         * Create a new update message from a buffer
         * @see FriendlyByteBuf
         * @param buffer The buffer to create the message from
         */
        public UpdateMessage(@NotNull FriendlyByteBuf buffer) {
            this.liquids = buffer.readDouble();
            this.solids = buffer.readDouble();
            this.bladder = buffer.readDouble();
            this.bowels = buffer.readDouble();
            this.bladderDesperation = buffer.readDouble();
            this.bowelsDesperation = buffer.readDouble();
        }

        /**
         * Decode a message from a buffer
         * @param buffer The buffer to decode the message from
         * @return The decoded message
         */
        @Contract("_ -> new")
        public static @NotNull UpdateMessage decoder(FriendlyByteBuf buffer) {
            return new UpdateMessage(buffer);
        }

        /**
         * Encode a message to a buffer
         * @implSpec The order of the encoded values must match the order of the decoded values found in the constructor
         * @param buffer The buffer to encode the message to
         */
        public void encoder(@NotNull FriendlyByteBuf buffer) {
            buffer.writeDouble(liquids);
            buffer.writeDouble(solids);
            buffer.writeDouble(bladder);
            buffer.writeDouble(bowels);
            buffer.writeDouble(bladderDesperation);
            buffer.writeDouble(bowelsDesperation);
        }

        /**
         * Consume a message and update the player's metabolism capability
         * @param ctx The context of the message
         */
        public void messageConsumer(@NotNull Supplier<NetworkEvent.Context> ctx) {
            LOGGER.debug("Received metabolism update message");
            Player player = Optional.ofNullable((Player) ctx.get().getSender()).orElse(Minecraft.getInstance().player);
            Optional.ofNullable(player).ifPresent(p -> {
                        LOGGER.debug("Updating player");
                        p.getCapability(Metabolism.INSTANCE).ifPresent(m -> {
                            LOGGER.debug("Updating metabolism");
                            m.setLiquids(liquids, false);
                            m.setSolids(solids, false);
                            m.setBladder(bladder, false);
                            m.setBowels(bowels, false);
                            m.setBladderDesperation(bladderDesperation, false);
                            m.setBowelsDesperation(bowelsDesperation, false);
                        });
                    }
            );
        }
    }
}
