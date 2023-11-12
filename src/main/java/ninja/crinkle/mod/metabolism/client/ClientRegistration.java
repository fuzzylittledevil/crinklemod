package ninja.crinkle.mod.metabolism.client;

import net.minecraftforge.common.MinecraftForge;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.metabolism.client.events.AccidentEventHandler;
import ninja.crinkle.mod.metabolism.client.events.ClientEventHandler;
import ninja.crinkle.mod.metabolism.client.events.DesperationEventHandler;

public class ClientRegistration {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        CrinkleMod.EVENT_BUS.register(new AccidentEventHandler());
        CrinkleMod.EVENT_BUS.register(new DesperationEventHandler());
    }
}
