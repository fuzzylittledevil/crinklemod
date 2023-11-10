package ninja.crinkle.mod.metabolism.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.metabolism.common.network.messages.UpdateMessage;

/**
 * A network channel that is used to sync the metabolism of a player to the client or server.
 * @see UpdateMessage
 * @see net.minecraftforge.network.simple.SimpleChannel
 */
public class MetabolismChannel {
    private static final String PROTOCOL_VERSION = "1";
    private static final String NAME = "metabolism";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(CrinkleMod.MODID, NAME))
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
}
