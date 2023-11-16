package ninja.crinkle.mod.metabolism.client;

import net.minecraftforge.common.MinecraftForge;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.metabolism.client.events.DesperationEventHandler;

public class MetabolismClientRegistration {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new MetabolismClientEventHandler());
        CrinkleMod.EVENT_BUS.register(new DesperationEventHandler());
    }
}
