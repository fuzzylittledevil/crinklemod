package ninja.crinkle.mod.client.icons;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.client.textures.SpriteLoaderType;
import ninja.crinkle.mod.client.textures.Textures;

public enum Icons {
    RESET(new ResourceLocation(CrinkleMod.MODID, "gui/icons/reset")),
    WRENCH(new ResourceLocation(CrinkleMod.MODID, "gui/icons/wrench")),
    GEAR(new ResourceLocation(CrinkleMod.MODID, "gui/icons/gear")),
    DOWN_ARROW(new ResourceLocation(CrinkleMod.MODID, "gui/icons/down"));


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
