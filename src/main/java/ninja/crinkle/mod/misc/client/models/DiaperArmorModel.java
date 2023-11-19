package ninja.crinkle.mod.misc.client.models;

import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.misc.items.custom.DiaperArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class DiaperArmorModel extends GeoModel<DiaperArmorItem> {
    @Override
    public ResourceLocation getModelResource(DiaperArmorItem animatable) {
        return new ResourceLocation(CrinkleMod.MODID, "geo/armor/diaper.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DiaperArmorItem animatable) {
        return new ResourceLocation(CrinkleMod.MODID, "textures/armor/diaper.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DiaperArmorItem animatable) {
        return new ResourceLocation(CrinkleMod.MODID, "animations/armor/diaper.animation.json");
    }
}
