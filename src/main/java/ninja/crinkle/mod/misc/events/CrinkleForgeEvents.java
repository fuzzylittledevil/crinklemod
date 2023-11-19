package ninja.crinkle.mod.misc.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.lib.client.tooltips.GradientBarClientTooltip;
import ninja.crinkle.mod.lib.common.tooltips.GradientBarTooltip;
import ninja.crinkle.mod.misc.client.renderers.DunnyRenderer;

import static ninja.crinkle.mod.misc.blocks.CrinkleBlocks.DUNNY_BLOCK_ENTITY;


@Mod.EventBusSubscriber(modid = CrinkleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CrinkleForgeEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(DUNNY_BLOCK_ENTITY.get(), DunnyRenderer::new);
    }

    @SubscribeEvent
    public static void registerClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(GradientBarTooltip.class, GradientBarClientTooltip::new);
    }
}
