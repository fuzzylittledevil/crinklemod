package ninja.crinkle.mod.client.ui.themes;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.util.ClientUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class Theme {
    public static final Theme DEFAULT = new Theme(
            Map.of(
                    BorderThemeSize.SMALL, BorderThemeData.GRAY_SMALL,
                    BorderThemeSize.MEDIUM, BorderThemeData.GRAY_MEDIUM,
                    BorderThemeSize.LARGE, BorderThemeData.GRAY_LARGE
            ),
            Color.of(0xFFD7AEFF),
            Color.WHITE,
            Color.of("#AA7FD6")
    );
    private final Map<BorderThemeSize, BorderThemeData> borderThemes;
    private final Map<BorderThemeData, ResourceLocation> invertedTextures;
    private final Color backgroundColor;
    private final Color foregroundColor;
    private final Color secondaryColor;

    public Theme(Map<BorderThemeSize, BorderThemeData> borderThemes, Color backgroundColor, Color foregroundColor, Color secondaryColor) {
        this.borderThemes = borderThemes;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.secondaryColor = secondaryColor;
        this.invertedTextures = borderThemes.values().stream().distinct().collect(
                java.util.stream.Collectors.toMap(
                        borderTheme -> borderTheme,
                        borderTheme -> invertTexture(borderTheme.texture())
                )
        );
    }

    public BorderThemeData getBorderTheme(BorderThemeSize size) {
        return borderThemes.get(size);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public ResourceLocation getInvertedTexture(BorderThemeData borderTheme) {
        return invertedTextures.get(borderTheme);
    }

    private static ResourceLocation invertTexture(ResourceLocation texture) {
        Minecraft minecraft = ClientUtil.getMinecraft();
        TextureManager textureManager = minecraft.getTextureManager();
        try(InputStream is = minecraft.getResourceManager().open(texture)) {
            NativeImage image = NativeImage.read(is);
            // Invert the texture colors
            for(int x = 0; x < image.getWidth(); x++) {
                for(int y = 0; y < image.getHeight(); y++) {
                    int color = image.getPixelRGBA(x, y);
                    int alpha = color & 0xFF000000;
                    int rgb = color & 0x00FFFFFF;
                    int inverted = alpha | (~rgb & 0x00FFFFFF);
                    image.setPixelRGBA(x, y, inverted);
                }
            }
            // Create a dynamic texture
            ResourceLocation invertedTexture = new ResourceLocation(texture.getNamespace(), texture.getPath() + "_inverted");
            textureManager.register(invertedTexture, new DynamicTexture(image));
            return invertedTexture;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
