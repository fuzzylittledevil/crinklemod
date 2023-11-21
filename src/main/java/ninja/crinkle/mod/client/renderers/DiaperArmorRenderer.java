package ninja.crinkle.mod.client.renderers;

import net.minecraft.world.entity.EquipmentSlot;
import ninja.crinkle.mod.client.models.DiaperArmorModel;
import ninja.crinkle.mod.items.custom.DiaperArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

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
