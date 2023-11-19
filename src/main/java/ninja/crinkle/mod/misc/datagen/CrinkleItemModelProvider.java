package ninja.crinkle.mod.misc.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.misc.blocks.CrinkleBlocks;
import ninja.crinkle.mod.misc.items.CrinkleItems;

public class CrinkleItemModelProvider extends ItemModelProvider {
    public CrinkleItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CrinkleMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleArmorItem(CrinkleItems.DIAPER);
    }

    private void simpleArmorItem(RegistryObject<Item> armor) {
        withExistingParent(armor.getId().getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(CrinkleMod.MODID, "item/" + armor.getId().getPath()));
    }
}
