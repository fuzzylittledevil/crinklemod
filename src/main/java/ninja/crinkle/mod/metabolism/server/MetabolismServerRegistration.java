package ninja.crinkle.mod.metabolism.server;

import net.minecraftforge.common.MinecraftForge;
import ninja.crinkle.mod.metabolism.common.network.MetabolismChannel;
import ninja.crinkle.mod.metabolism.server.events.MetabolismServerEventHandler;

public class MetabolismServerRegistration {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new MetabolismServerEventHandler());
        MetabolismChannel.register();
    }
}
