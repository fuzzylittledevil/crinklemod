package ninja.crinkle.mod.metabolism.client;

import net.minecraft.client.Minecraft;

public class ClientHooks {
    public static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }
}
