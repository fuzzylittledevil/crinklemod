package ninja.crinkle.mod.undergarment.server;

import net.minecraftforge.common.MinecraftForge;
import ninja.crinkle.mod.undergarment.server.events.UndergarmentServerEventHandler;

public class UndergarmentServerRegistration {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new UndergarmentServerEventHandler());
    }
}
