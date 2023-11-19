package ninja.crinkle.mod.misc.client.renderers;

import net.minecraft.world.entity.EquipmentSlot;
import ninja.crinkle.mod.misc.client.models.DiaperArmorModel;
import ninja.crinkle.mod.misc.items.custom.DiaperArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class DiaperArmorRenderer extends GeoArmorRenderer<DiaperArmorItem> {
    public DiaperArmorRenderer() {
        super(new DiaperArmorModel());

    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        super.applyBoneVisibilityBySlot(currentSlot);
        setBoneVisible(body, true);
    }
}
