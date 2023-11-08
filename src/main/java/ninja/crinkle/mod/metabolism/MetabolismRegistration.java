package ninja.crinkle.mod.metabolism;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.metabolism.capabilities.Metabolism;
import ninja.crinkle.mod.metabolism.config.ConsumableConfig;
import ninja.crinkle.mod.metabolism.config.MetabolismConfig;
import ninja.crinkle.mod.metabolism.events.AccidentEventHandler;
import ninja.crinkle.mod.metabolism.events.DesperationEventHandler;
import ninja.crinkle.mod.metabolism.events.ForgeEventHandler;
import ninja.crinkle.mod.metabolism.network.MetabolismChannel;

/**
 * A class that is used to register all metabolism related things.
 */
public class MetabolismRegistration {
    /**
     * Register all metabolism related things
     */
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, Metabolism::attach);
        CrinkleMod.EVENT_BUS.register(new AccidentEventHandler());
        CrinkleMod.EVENT_BUS.register(new DesperationEventHandler());
        MetabolismConfig.register();
        ConsumableConfig.register();
        MetabolismChannel.register();
    }
}
