package ninja.crinkle.mod.events.handlers;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.client.renderers.DunnyRenderer;
import ninja.crinkle.mod.client.ui.tooltips.GradientBarClientTooltip;
import ninja.crinkle.mod.tooltips.GradientBarTooltip;

import static ninja.crinkle.mod.blocks.CrinkleBlocks.DUNNY_BLOCK_ENTITY;


@Mod.EventBusSubscriber(modid = CrinkleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CrinkleModBusEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(DUNNY_BLOCK_ENTITY.get(), DunnyRenderer::new);
    }

    @SubscribeEvent
    public static void registerClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(GradientBarTooltip.class, GradientBarClientTooltip::new);
    }
}
