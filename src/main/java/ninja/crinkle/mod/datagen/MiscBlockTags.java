package ninja.crinkle.mod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import ninja.crinkle.mod.blocks.CrinkleBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MiscBlockTags extends BlockTagsProvider {
    public MiscBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId,
                         @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider pProvider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(CrinkleBlocks.DUNNY_BLOCK.get());
        tag(BlockTags.NEEDS_STONE_TOOL).add(CrinkleBlocks.DUNNY_BLOCK.get());
    }
}
