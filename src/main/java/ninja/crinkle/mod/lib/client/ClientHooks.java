package ninja.crinkle.mod.lib.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.DistExecutor;
import ninja.crinkle.mod.metabolism.common.network.MetabolismChannel;
import ninja.crinkle.mod.metabolism.common.network.messages.MetabolismFetchMessage;

public class ClientHooks {
    public static Minecraft getMinecraft() {
        return Minecraft.getInstance();
    }
    public static void fetchMetabolism() {
        MetabolismChannel.INSTANCE.sendToServer(new MetabolismFetchMessage());
    }

    public static Player getPlayer() {
        return getMinecraft().player;
    }
}
