package ninja.crinkle.mod.undergarment.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.undergarment.common.network.messages.UndergarmentUpdateMessage;

public class UndergarmentChannel {
    private static final String PROTOCOL_VERSION = "1";
    private static final String NAME = "undergarment";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(CrinkleMod.MODID, NAME))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void register() {
        int id = 0;
        INSTANCE.messageBuilder(UndergarmentUpdateMessage.class, ++id)
                .decoder(UndergarmentUpdateMessage::decoder)
                .encoder(UndergarmentUpdateMessage::encoder)
                .consumerMainThread(UndergarmentUpdateMessage::messageConsumer)
                .add();
    }
}
