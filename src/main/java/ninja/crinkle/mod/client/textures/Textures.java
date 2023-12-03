package ninja.crinkle.mod.client.textures;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.textures.generators.BoxThemeTextureGenerator;
import ninja.crinkle.mod.client.textures.generators.DiaperTextureGenerator;
import ninja.crinkle.mod.client.textures.generators.TextureData;
import ninja.crinkle.mod.client.textures.generators.TextureGenerator;
import ninja.crinkle.mod.undergarment.Undergarment;
import ninja.crinkle.mod.util.ClientUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Textures {
    private static final Map<String, TextureGenerator<?>> textureGenerators = Map.of(
            "gui/panel_background", new BoxThemeTextureGenerator(),
            "gui/button_background", new BoxThemeTextureGenerator(),
            "gui/button_background_inverted", new BoxThemeTextureGenerator(),
            "gui/button_background_inactive", new BoxThemeTextureGenerator(),
            "armor/diaper_plain",
            new DiaperTextureGenerator(
                    Undergarment::getLiquidsPercent,
                    Color.of(Undergarment.LIQUIDS_COLOR),
                    DiaperTextureGenerator.Part.FRONT_TOP,
                    DiaperTextureGenerator.Part.FRONT_BOTTOM,
                    DiaperTextureGenerator.Part.BOTTOM)
                    .andThen(new DiaperTextureGenerator(
                            Undergarment::getSolidsPercent,
                            Color.of(Undergarment.SOLIDS_COLOR),
                            DiaperTextureGenerator.Part.BACK_TOP,
                            DiaperTextureGenerator.Part.BACK_BOTTOM)
                    )
    );
    private static Textures INSTANCE;
    private final Map<SpriteLoaderType, CrinkleSpriteLoader> loaders = new EnumMap<>(SpriteLoaderType.class);
    private final Map<String, ResourceLocation> dynamicTextures = new HashMap<>();

    Textures() {
    }

    public static Textures getInstance() {
        if (INSTANCE == null) {
            Minecraft minecraft = Minecraft.getInstance();
            TextureManager manager = minecraft.getTextureManager();
            INSTANCE = new Textures();
            for (SpriteLoaderType type : SpriteLoaderType.values()) {
                INSTANCE.loaders.put(type, new CrinkleSpriteLoader(manager, type));
            }
        }
        return INSTANCE;
    }

    public void releaseAll() {
        dynamicTextures.values().forEach(ClientUtil.getMinecraft().getTextureManager()::release);
        dynamicTextures.clear();
    }

    public void release(String pName) {
        ResourceLocation location = dynamicTextures.remove(pName);
        if (location != null) {
            ClientUtil.getMinecraft().getTextureManager().release(location);
        }
    }

    public CrinkleSpriteLoader getSpriteLoader(SpriteLoaderType pType) {
        return loaders.get(pType);
    }

    public ResourceLocation registerTexture(String pName, NativeImage pImage) {
        Minecraft minecraft = ClientUtil.getMinecraft();
        dynamicTextures.put(pName, minecraft.getTextureManager().register(pName, new DynamicTexture(pImage)));
        return dynamicTextures.get(pName);
    }

    @SuppressWarnings("resource")
    public ResourceLocation getTexture(ResourceLocation pOriginal, @NotNull TextureData pData) {
        if (dynamicTextures.containsKey(pData.getName()))
            return dynamicTextures.get(pData.getName());
        TextureGenerator<?> generator = textureGenerators.get(pOriginal.getPath());
        if (generator == null) {
            return pOriginal;
        }
        SpriteLoaderType type = SpriteLoaderType.fromResourceLocation(pOriginal);
        CrinkleSpriteLoader spriteLoader = loaders.get(type);
        TextureAtlasSprite sprite = spriteLoader.getSprite(pOriginal);
        NativeImage image = new NativeImage(sprite.contents().width(), sprite.contents().height(), true);
        image.copyFrom(sprite.contents().getOriginalImage());
        return registerTexture(pData.getName(), generator.apply(image, pData));
    }
}
