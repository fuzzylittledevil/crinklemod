package ninja.crinkle.mod.misc.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import ninja.crinkle.mod.misc.client.ui.DunnyScreen;
import ninja.crinkle.mod.misc.menus.CrinkleMenus;

import static ninja.crinkle.mod.CrinkleMod.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(CrinkleMenus.DUNNY_CONTAINER.get(), DunnyScreen::new);
        });
    }
}
