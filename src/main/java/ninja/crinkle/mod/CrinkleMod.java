package ninja.crinkle.mod;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ninja.crinkle.mod.blocks.CrinkleBlocks;
import ninja.crinkle.mod.capabilities.MetabolismProvider;
import ninja.crinkle.mod.config.ConsumableConfig;
import ninja.crinkle.mod.config.MetabolismConfig;
import ninja.crinkle.mod.config.UndergarmentConfig;
import ninja.crinkle.mod.datagen.MiscDataGeneration;
import ninja.crinkle.mod.events.handlers.UndergarmentEventHandler;
import ninja.crinkle.mod.items.CrinkleItems;
import ninja.crinkle.mod.items.CrinkleTabs;
import ninja.crinkle.mod.menus.CrinkleMenus;
import ninja.crinkle.mod.network.CrinkleChannel;
import software.bernie.geckolib.GeckoLib;

/**
 * The main class of the mod.
 */
@Mod(CrinkleMod.MODID)
public class CrinkleMod {
    /**
     * The mod ID of the mod.
     * This must match the mod ID in the root gradle.properties file as well as the directory: <pre>src/main/resources/assets/[mod_id]</pre>
     */
    public static final String MODID = "crinklemod";
    /**
     * The event bus that is used to register internal events.
     */
    public static final IEventBus EVENT_BUS = BusBuilder.builder().build();

    public CrinkleMod() {
        // Metabolism
        MetabolismConfig.register();
        ConsumableConfig.register();
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, MetabolismProvider::attach);
        CrinkleChannel.register();

        // Undergarments
        UndergarmentConfig.register();

        // Common
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CrinkleBlocks.register(modEventBus);
        CrinkleItems.register(modEventBus);
        CrinkleTabs.register(modEventBus);
        CrinkleMenus.register(modEventBus);
        CrinkleMod.EVENT_BUS.register(new UndergarmentEventHandler());

        // DataGen
        modEventBus.addListener(MiscDataGeneration::generate);

        GeckoLib.initialize();
    }
}
