package ninja.crinkle.mod.client.textures;

import com.mojang.blaze3d.platform.NativeImage;
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

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Textures {
    private static final Map<String, TextureGenerator<?>> textureGenerators = Map.of(
            "gui/panel_background", new BoxThemeTextureGenerator(),
            "gui/button_background", new BoxThemeTextureGenerator(),
            "gui/button_background_inverted", new BoxThemeTextureGenerator(),
            "gui/button_background_inactive", new BoxThemeTextureGenerator(),
            "gui/checkbox_background", new BoxThemeTextureGenerator(),
            "gui/checkbox_background_inverted", new BoxThemeTextureGenerator(),
            "gui/checkbox_background_inactive", new BoxThemeTextureGenerator(),
            "armor/diaper_plain",
            new DiaperTextureGenerator(
                    Undergarment::getLiquidsPercent,
                    DiaperTextureGenerator.WET_COLORS,
                    Set.of(Color.WHITE),
                    DiaperTextureGenerator.Part.FRONT_TOP,
                    DiaperTextureGenerator.Part.FRONT_BOTTOM,
                    DiaperTextureGenerator.Part.BOTTOM)
                    .andThen(new DiaperTextureGenerator(
                            Undergarment::getSolidsPercent,
                            DiaperTextureGenerator.MESS_COLORS,
                            Set.of(Color.WHITE),
                            DiaperTextureGenerator.Part.BACK_TOP,
                            DiaperTextureGenerator.Part.BACK_BOTTOM)
                    ),
            "armor/diaper_little_pawz",
            new DiaperTextureGenerator(
                    Undergarment::getLiquidsPercent,
                    DiaperTextureGenerator.WET_COLORS,
                    Set.of(Color.WHITE,
                            Color.of(0xFFDFDFE0),
                            Color.of(0xFFBDBDBD)),
                    DiaperTextureGenerator.Part.FRONT_TOP,
                    DiaperTextureGenerator.Part.FRONT_BOTTOM,
                    DiaperTextureGenerator.Part.BOTTOM)
                    .andThen(new DiaperTextureGenerator(
                            Undergarment::getSolidsPercent,
                            DiaperTextureGenerator.MESS_COLORS,
                            Set.of(Color.WHITE,
                                    Color.of(0xFFDFDFE0),
                                    Color.of(0xFFBDBDBD)),
                            DiaperTextureGenerator.Part.BACK_TOP,
                            DiaperTextureGenerator.Part.BACK_BOTTOM)
                    ));
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

//    public void releaseAll() {
//        dynamicTextures.values().forEach(ClientUtil.getMinecraft().getTextureManager()::release);
//        dynamicTextures.clear();
//    }
//
//    public void release(String pName) {
//        ResourceLocation location = dynamicTextures.remove(pName);
//        if (location != null) {
//            ClientUtil.getMinecraft().getTextureManager().release(location);
//        }
//    }

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
