package ninja.crinkle.mod.client.ui.themes;

import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.textures.Textures;
import ninja.crinkle.mod.client.ui.widgets.properties.Border;
import org.jetbrains.annotations.NotNull;

public record BoxTheme(String name, ResourceLocation texture, ResourceLocation inverted, ResourceLocation inactive,
                       int cornerWidth, int cornerHeight, int edgeWidth, int edgeHeight, int edgeSize,
                       Border border) {

    public static final BoxTheme NONE = new BoxTheme("none", new ResourceLocation(CrinkleMod.MODID, "gui/none"),
            null, null, 0, 0, 0, 0, 0, Border.ZERO);

    public static final BoxTheme BUTTON = new BoxTheme("button_background",
            new ResourceLocation(CrinkleMod.MODID, "gui/button_background"),
            new ResourceLocation(CrinkleMod.MODID, "gui/button_background_inverted"),
            new ResourceLocation(CrinkleMod.MODID, "gui/button_background_inactive"),
            1, 1, 1, 1, 1,
            new Border(1, 1, 1, 1, Color.BLACK));

    public static final BoxTheme PANEL = new BoxTheme("panel_background",
            new ResourceLocation(CrinkleMod.MODID, "gui/panel_background"),
            null, null, 4, 4, 4, 4, 1,
            Border.ZERO);

    public static final BoxTheme CHECKBOX = new BoxTheme("checkbox_background",
            new ResourceLocation(CrinkleMod.MODID, "gui/checkbox_background"),
            new ResourceLocation(CrinkleMod.MODID, "gui/checkbox_background_inverted"),
            new ResourceLocation(CrinkleMod.MODID, "gui/checkbox_background_inactive"),
            1, 1, 1, 1, 1,
            Border.ZERO);

    public static final BoxTheme SLIDER_TRACK = new BoxTheme("slider_track",
            new ResourceLocation(CrinkleMod.MODID, "gui/slider_track"),
            new ResourceLocation(CrinkleMod.MODID, "gui/slider_track_inverted"),
            new ResourceLocation(CrinkleMod.MODID, "gui/slider_track_inactive"),
            2, 2, 2, 2, 1,
            Border.ZERO);

    public static final BoxTheme SLIDER_THUMB = new BoxTheme("slider_thumb",
            new ResourceLocation(CrinkleMod.MODID, "gui/slider_thumb"),
            new ResourceLocation(CrinkleMod.MODID, "gui/slider_thumb_inverted"),
            new ResourceLocation(CrinkleMod.MODID, "gui/slider_thumb_inactive"),
            2, 2, 2, 2, 1,
            Border.ZERO);

    private ResourceLocation getTextureByType(@NotNull TextureType type) {
        return switch (type) {
            case INVERTED -> inverted();
            case INACTIVE -> inactive();
            default -> texture();
        };
    }

    public enum Type {
        NONE,
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
