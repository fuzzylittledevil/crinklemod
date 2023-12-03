package ninja.crinkle.mod.client.ui.themes;

import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.client.textures.Textures;
import ninja.crinkle.mod.client.textures.generators.BoxThemeTextureGenerator;
import org.jetbrains.annotations.NotNull;

public record BoxTheme(String name, ResourceLocation texture, ResourceLocation inverted, ResourceLocation inactive,
                       int cornerWidth, int cornerHeight, int edgeWidth, int edgeHeight, int edgeSize) {

    public static final BoxTheme BUTTON = new BoxTheme("button_background",
            new ResourceLocation(CrinkleMod.MODID, "gui/button_background"),
            new ResourceLocation(CrinkleMod.MODID, "gui/button_background_inverted"),
            new ResourceLocation(CrinkleMod.MODID, "gui/button_background_inactive"),
            2, 2, 2, 2, 1);

    public static final BoxTheme PANEL = new BoxTheme("panel_background",
            new ResourceLocation(CrinkleMod.MODID, "gui/panel_background"),
            null, null, 4, 4, 4, 4, 1);

    public @NotNull ResourceLocation generateTexture(int width, int height, TextureType type) {
        BoxThemeTextureGenerator.Data data = new BoxThemeTextureGenerator.Data(this, type, width, height);
        return Textures.getInstance().getTexture(getTextureByType(type), data);
    }

    private ResourceLocation getTextureByType(@NotNull TextureType type) {
        return switch (type) {
            case INVERTED -> inverted();
            case INACTIVE -> inactive();
            default -> texture();
        };
    }

    public enum Size {
        SMALL,
        MEDIUM,
        LARGE
    }

    public enum TextureType {
        NORMAL,
        INVERTED,
        INACTIVE
    }
}
