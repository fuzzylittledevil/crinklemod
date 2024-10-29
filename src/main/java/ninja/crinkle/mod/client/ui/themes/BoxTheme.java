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

    public static final BoxTheme CHECKBOX = new BoxTheme("checkbox_background",
            new ResourceLocation(CrinkleMod.MODID, "gui/checkbox_background"),
            new ResourceLocation(CrinkleMod.MODID, "gui/checkbox_background_inverted"),
            new ResourceLocation(CrinkleMod.MODID, "gui/checkbox_background_inactive"),
            2, 2, 2, 2, 1);

    public static final BoxTheme SLIDER_TRACK = new BoxTheme("slider_track",
            new ResourceLocation(CrinkleMod.MODID, "gui/slider_track"),
            new ResourceLocation(CrinkleMod.MODID, "gui/slider_track_inverted"),
            new ResourceLocation(CrinkleMod.MODID, "gui/slider_track_inactive"),
            2, 2, 2, 2, 1);

    public static final BoxTheme SLIDER_THUMB = new BoxTheme("slider_thumb",
            new ResourceLocation(CrinkleMod.MODID, "gui/slider_thumb"),
            new ResourceLocation(CrinkleMod.MODID, "gui/slider_thumb_inverted"),
            new ResourceLocation(CrinkleMod.MODID, "gui/slider_thumb_inactive"),
            2, 2, 2, 2, 1);

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

    public enum Type {
        CHECKBOX,
        BUTTON,
        PANEL,
        SLIDER_TRACK,
        SLIDER_THUMB
    }

    public enum TextureType {
        NORMAL,
        INVERTED,
        INACTIVE
    }
}
