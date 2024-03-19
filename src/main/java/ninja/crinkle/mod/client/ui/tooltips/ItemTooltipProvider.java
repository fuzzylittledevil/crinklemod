package ninja.crinkle.mod.client.ui.tooltips;

import com.mojang.datafixers.util.Either;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ItemTooltipProvider {
    List<Either<FormattedText, TooltipComponent>> getTooltip(ItemStack stack);
}
