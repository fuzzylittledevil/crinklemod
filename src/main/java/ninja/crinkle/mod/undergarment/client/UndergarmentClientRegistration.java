package ninja.crinkle.mod.undergarment.client;

import net.minecraftforge.common.MinecraftForge;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.undergarment.client.events.UndergarmentEventHandler;

public class UndergarmentClientRegistration {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new UndergarmentClientEventHandler());
        CrinkleMod.EVENT_BUS.register(new UndergarmentEventHandler());
    }
}
