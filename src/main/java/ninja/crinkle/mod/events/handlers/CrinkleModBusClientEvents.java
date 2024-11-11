package ninja.crinkle.mod.events.handlers;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.client.gui.textures.ThemeAtlas;
import ninja.crinkle.mod.client.gui.themes.loader.ThemeReloadListener;
import ninja.crinkle.mod.client.renderers.DunnyRenderer;
import ninja.crinkle.mod.client.textures.SpriteLoaderType;
import ninja.crinkle.mod.client.textures.Textures;
import ninja.crinkle.mod.client.ui.tooltips.GradientBarClientTooltip;
import ninja.crinkle.mod.tooltips.GradientBarTooltip;
import ninja.crinkle.mod.util.ClientUtil;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static ninja.crinkle.mod.blocks.CrinkleBlocks.DUNNY_BLOCK_ENTITY;


@Mod.EventBusSubscriber(modid = CrinkleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CrinkleModBusClientEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(ThemeAtlas.getAtlas());
        event.registerReloadListener(new ThemeReloadListener());
        Arrays.stream(SpriteLoaderType.values())
                .map(type -> Textures.getInstance().getSpriteLoader(type)).forEach(event::registerReloadListener);

    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(DUNNY_BLOCK_ENTITY.get(), DunnyRenderer::new);
    }

    @SubscribeEvent
    public static void registerClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(GradientBarTooltip.class, GradientBarClientTooltip::new);
    }

    @SubscribeEvent
    public static void generateTextures(TextureStitchEvent event) {
        ResourceLocation location = event.getAtlas().location();
        if (!location.getNamespace().equals(CrinkleMod.MODID)) {
            return;
        }
//        try {
//            event.getAtlas().dumpContents(location, Path.of("/Users","jonathan","projects","crinkle-mod","debug"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        // Log the texture locations for debugging purposes
        event.getAtlas().getTextureLocations().forEach(l -> {
            LOGGER.debug("{} -> {}", event.getAtlas().location(), l.toString());
        });

        // Register the textures with the ThemeAtlas.
        // It will automatically skip textures that are not part of the theme system.
        event.getAtlas().getTextureLocations().forEach(ThemeAtlas::register);
    }
}
