package ninja.crinkle.mod.datagen;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PickedUpItemTrigger;
import net.minecraft.advancements.critereon.TradeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.items.CrinkleItems;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static ninja.crinkle.mod.CrinkleMod.loc;

public class CrinkleAdvancements implements ForgeAdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.@NotNull Provider registries, @NotNull Consumer<Advancement> writer,
                         @NotNull ExistingFileHelper existingFileHelper) {

        Advancement.Builder.advancement()
                .addCriterion("has_diaper", InventoryChangeTrigger.TriggerInstance.hasItems(CrinkleItems.DIAPER.get()))
                .display(new DisplayInfo(CrinkleItems.DIAPER.get().getDefaultInstance(),
                        Component.translatable("advancements.crinklemod.wearing_diaper.title"),
                        Component.translatable("advancements.crinklemod.wearing_diaper.description"),
                        new ResourceLocation("minecraft:textures/block/white_wool.png"),
                        FrameType.CHALLENGE,
                        true,
                        true,
                        true))
                .save(writer, loc("wearing_diaper"), existingFileHelper);
    }
}
