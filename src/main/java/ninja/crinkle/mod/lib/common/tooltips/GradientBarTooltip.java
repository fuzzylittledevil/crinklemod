package ninja.crinkle.mod.lib.common.tooltips;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class GradientBarTooltip implements TooltipComponent {
    private final Component label;
    private final double value;
    private final double max;
    private final int startColor;
    private final int endColor;
    private final int fillColor;
    private final int height;
    private final int width;
    private final int labelWidth;

    public GradientBarTooltip(Component label, double value, double max, int startColor, int endColor, int fillColor, int height, int width, int labelWidth) {
        this.label = label;
        this.value = value;
        this.max = max;
        this.startColor = startColor;
        this.endColor = endColor;
        this.fillColor = fillColor;
        this.height = height;
        this.width = width;
        this.labelWidth = labelWidth;
    }

    public double getValue() {
        return value;
    }

    public double getMax() {
        return max;
    }

    public int getStartColor() {
        return startColor;
    }

    public int getEndColor() {
        return endColor;
    }

    public int getFillColor() {
        return fillColor;
    }

    public Component getLabel() {
        return label;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getLabelWidth() {
        return labelWidth;
    }
}
