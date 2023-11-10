package ninja.crinkle.mod.metabolism.server;

import net.minecraftforge.common.MinecraftForge;
import ninja.crinkle.mod.metabolism.common.network.MetabolismChannel;
import ninja.crinkle.mod.metabolism.server.events.ServerEventHandler;

public class ServerRegistration {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        MetabolismChannel.register();
    }
}
