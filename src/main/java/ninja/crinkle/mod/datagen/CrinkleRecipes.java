package ninja.crinkle.mod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import ninja.crinkle.mod.items.CrinkleItems;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CrinkleRecipes extends RecipeProvider {
    public CrinkleRecipes(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CrinkleItems.DIAPER_LITTLE_PAWZ.get())
                .unlockedBy("has_diaper", has(CrinkleItems.DIAPER_PLAIN.get()))
                .requires(CrinkleItems.DIAPER_PLAIN.get())
                .requires(Items.LIME_DYE)
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CrinkleItems.DIAPER_PLAIN.get())
                .unlockedBy("has_paper", has(Items.PAPER))
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .pattern("ppp")
                .pattern(" w ")
                .pattern("ppp")
                .define('p', Items.PAPER)
                .define('w', ItemTags.WOOL)
                .save(consumer);
    }
}
