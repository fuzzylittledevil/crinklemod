package ninja.crinkle.mod.client.icons;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.client.textures.SpriteLoaderType;
import ninja.crinkle.mod.client.textures.Textures;

public enum Icons {
    // ICONS! EVERY LAST ONE!
    BACK(new ResourceLocation(CrinkleMod.MODID, "gui/icons/back")),
    CHECKMARK(new ResourceLocation(CrinkleMod.MODID, "gui/icons/checkmark")),
    DOWN(new ResourceLocation(CrinkleMod.MODID, "gui/icons/down")),
    GEAR(new ResourceLocation(CrinkleMod.MODID, "gui/icons/gear")),
    RESET(new ResourceLocation(CrinkleMod.MODID, "gui/icons/reset")),
    SAVE(new ResourceLocation(CrinkleMod.MODID, "gui/icons/save")),
    WARNING1(new ResourceLocation(CrinkleMod.MODID, "gui/icons/warning1")),
    WARNING2(new ResourceLocation(CrinkleMod.MODID, "gui/icons/warning2")),
    WARNING3(new ResourceLocation(CrinkleMod.MODID, "gui/icons/warning3")),
    WRENCH(new ResourceLocation(CrinkleMod.MODID, "gui/icons/wrench")),
    WETNESS_OUTLINED(new ResourceLocation(CrinkleMod.MODID, "gui/icons/wetness_outlined")),
    WETNESS_DANGER(new ResourceLocation(CrinkleMod.MODID, "gui/icons/wetness_danger")),
    MESSINESS_OUTLINED(new ResourceLocation(CrinkleMod.MODID, "gui/icons/messiness_outlined")),
    MESSINESS_DANGER(new ResourceLocation(CrinkleMod.MODID, "gui/icons/messiness_danger")),
    ;


    private final ResourceLocation location;

    Icons(ResourceLocation location) {
        this.location = location;
    }

    public ResourceLocation location() {
        return location;
    }

    @SuppressWarnings("resource")
    public int width() {
        return getSprite().contents().width();
    }

    @SuppressWarnings("resource")
    public int height() {
        return getSprite().contents().height();
    }

    public TextureAtlasSprite getSprite() {
        return Textures.getInstance().getSpriteLoader(SpriteLoaderType.GUI).getSprite(location());
    }
}
