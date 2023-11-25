package ninja.crinkle.mod.client.ui.themes;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.util.ClientUtil;

import java.io.IOException;
import java.io.InputStream;

public record BorderThemeData(ResourceLocation texture, int cornerWidth,
                              int cornerHeight, int edgeWidth, int edgeHeight, int uWidth, int vHeight,
                              int uOffset, int vOffset, int textureWidth, int textureHeight) {
    public static BorderThemeData GRAY_SMALL = new BorderThemeData(
            new ResourceLocation(CrinkleMod.MODID, "textures/gui/themes/gray.png"),
            1, 1, 1, 1, 3, 3, 9, 5,
            14, 9
    );
    public static BorderThemeData GRAY_MEDIUM = new BorderThemeData(
            new ResourceLocation(CrinkleMod.MODID, "textures/gui/themes/gray.png"),
            2, 2, 2, 2, 5, 5, 9, 0,
            14, 9
    );

    public static BorderThemeData GRAY_LARGE = new BorderThemeData(
            new ResourceLocation(CrinkleMod.MODID, "textures/gui/themes/gray.png"),
            4, 4, 4, 4, 9, 9, 0, 0,
            14, 9
    );
}
