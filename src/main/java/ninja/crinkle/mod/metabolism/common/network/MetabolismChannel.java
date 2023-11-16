package ninja.crinkle.mod.metabolism.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.metabolism.common.network.messages.MetabolismFetchMessage;
import ninja.crinkle.mod.metabolism.common.network.messages.MetabolismUpdateMessage;

/**
 * A network channel that is used to sync the metabolism of a player to the client or server.
 *
 * @see MetabolismUpdateMessage
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
        INSTANCE.messageBuilder(MetabolismUpdateMessage.class, ++id)
                .decoder(MetabolismUpdateMessage::decoder)
                .encoder(MetabolismUpdateMessage::encoder)
                .consumerMainThread(MetabolismUpdateMessage::messageConsumer)
                .add();
        INSTANCE.messageBuilder(MetabolismFetchMessage.class, ++id)
                .encoder(MetabolismFetchMessage::encoder)
                .decoder(MetabolismFetchMessage::decoder)
                .consumerNetworkThread(MetabolismFetchMessage::messageConsumer)
                .add();
    }
}
