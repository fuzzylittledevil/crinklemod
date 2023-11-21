package ninja.crinkle.mod.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import ninja.crinkle.mod.CrinkleMod;

public class CrinkleTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CrinkleMod.MODID);

    public static final RegistryObject<CreativeModeTab> CRINKLE_TAB = CREATIVE_MODE_TABS.register("crinkle_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(CrinkleItems.DUNNY_BLOCK_ITEM.get()))
                    .displayItems((p, o) -> {
                        o.accept(CrinkleItems.DUNNY_BLOCK_ITEM.get());
                        o.accept(CrinkleItems.DIAPER.get());
                    })
                    .title(Component.translatable("tab.crinklemod.crinkle"))
                    .build());

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}
