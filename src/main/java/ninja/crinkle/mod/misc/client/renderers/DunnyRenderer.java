package ninja.crinkle.mod.misc.client.renderers;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.misc.blocks.entities.DunnyBlockEntity;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DunnyRenderer extends GeoBlockRenderer<DunnyBlockEntity> {
    public DunnyRenderer(BlockEntityRendererProvider.Context context) {
        super(new DefaultedBlockGeoModel<>(new ResourceLocation(CrinkleMod.MODID, "dunny")));
    }
}
