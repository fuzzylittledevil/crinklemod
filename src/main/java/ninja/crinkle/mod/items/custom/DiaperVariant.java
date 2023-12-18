package ninja.crinkle.mod.items.custom;

import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.CrinkleMod;

public enum DiaperVariant {
    PLAIN(new ResourceLocation(CrinkleMod.MODID, "armor/diaper_plain")),
    LITTLE_PAWZ(new ResourceLocation(CrinkleMod.MODID, "armor/diaper_little_pawz")),
    ;
    private final ResourceLocation texture;

    DiaperVariant(ResourceLocation texture) {
        this.texture = texture;
    }

    public ResourceLocation getTexture() {
        return texture;
    }
}
