package ninja.crinkle.mod.client.icons;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.client.textures.SpriteLoaderType;
import ninja.crinkle.mod.client.textures.Textures;

public enum Icons {
    BACK(new ResourceLocation(CrinkleMod.MODID, "gui/icons/back")),
    BLADDER(new ResourceLocation(CrinkleMod.MODID, "gui/icons/bladder")),
    DOWN(new ResourceLocation(CrinkleMod.MODID, "gui/icons/down")),
    GEAR(new ResourceLocation(CrinkleMod.MODID, "gui/icons/gear")),
    MESSINESS(new ResourceLocation(CrinkleMod.MODID, "gui/icons/messiness")),
    RESET(new ResourceLocation(CrinkleMod.MODID, "gui/icons/reset")),
    SAVE(new ResourceLocation(CrinkleMod.MODID, "gui/icons/save")),
    SIPPY_CUP(new ResourceLocation(CrinkleMod.MODID, "gui/icons/sippycup")),
    STEM_GLASS(new ResourceLocation(CrinkleMod.MODID, "gui/icons/stemglass")),
    TUMMY(new ResourceLocation(CrinkleMod.MODID, "gui/icons/tummy")),
    WARNING1(new ResourceLocation(CrinkleMod.MODID, "gui/icons/warning1")),
    WARNING2(new ResourceLocation(CrinkleMod.MODID, "gui/icons/warning2")),
    WARNING3(new ResourceLocation(CrinkleMod.MODID, "gui/icons/warning3")),
    WETNESS(new ResourceLocation(CrinkleMod.MODID, "gui/icons/wetness")),
    WRENCH(new ResourceLocation(CrinkleMod.MODID, "gui/icons/wrench"))
    ;


    private final ResourceLocation location;

    Icons(ResourceLocation location) {
        this.location = location;
    }

    public ResourceLocation location() {
        return location;
    }

    public TextureAtlasSprite getSprite() {
        return Textures.getInstance().getSpriteLoader(SpriteLoaderType.GUI).getSprite(location());
    }
}
