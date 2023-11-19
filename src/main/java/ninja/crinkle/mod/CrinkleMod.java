package ninja.crinkle.mod;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ninja.crinkle.mod.metabolism.client.MetabolismClientRegistration;
import ninja.crinkle.mod.metabolism.common.MetabolismCommonRegistration;
import ninja.crinkle.mod.metabolism.server.MetabolismServerRegistration;
import ninja.crinkle.mod.misc.blocks.CrinkleBlocks;
import ninja.crinkle.mod.misc.datagen.MiscDataGeneration;
import ninja.crinkle.mod.misc.items.CrinkleItems;
import ninja.crinkle.mod.misc.items.CrinkleTabs;
import ninja.crinkle.mod.misc.menus.CrinkleMenus;
import ninja.crinkle.mod.undergarment.client.UndergarmentClientRegistration;
import ninja.crinkle.mod.undergarment.common.UndergarmentCommonRegistration;
import ninja.crinkle.mod.undergarment.server.UndergarmentServerRegistration;
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
        MetabolismCommonRegistration.register();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> MetabolismClientRegistration::register);
        MetabolismServerRegistration.register();

        UndergarmentCommonRegistration.register();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> UndergarmentClientRegistration::register);
        UndergarmentServerRegistration.register();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CrinkleBlocks.register(modEventBus);
        CrinkleItems.register(modEventBus);
        CrinkleTabs.register(modEventBus);
        CrinkleMenus.register(modEventBus);

        modEventBus.addListener(MiscDataGeneration::generate);

        GeckoLib.initialize();
    }
}
