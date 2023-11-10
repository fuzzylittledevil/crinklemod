package ninja.crinkle.mod.metabolism.common;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import ninja.crinkle.mod.metabolism.common.capabilities.MetabolismProvider;
import ninja.crinkle.mod.metabolism.common.config.ConsumableConfig;
import ninja.crinkle.mod.metabolism.common.config.MetabolismConfig;
import ninja.crinkle.mod.metabolism.common.network.MetabolismChannel;

public class CommonRegistration {
    public static void register() {
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, MetabolismProvider::attach);
        MetabolismConfig.register();
        ConsumableConfig.register();
        MetabolismChannel.register();
    }
}
