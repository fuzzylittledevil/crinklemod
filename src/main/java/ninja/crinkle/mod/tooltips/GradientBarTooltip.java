package ninja.crinkle.mod.tooltips;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record GradientBarTooltip(Component label, double value, double max, int startColor, int endColor, int fillColor,
                                 int height, int width, int labelWidth) implements TooltipComponent {
}
