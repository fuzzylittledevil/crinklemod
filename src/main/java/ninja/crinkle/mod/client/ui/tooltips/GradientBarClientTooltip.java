package ninja.crinkle.mod.client.ui.tooltips;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.tooltips.GradientBarTooltip;
import ninja.crinkle.mod.util.RenderUtil;
import org.jetbrains.annotations.NotNull;

public class GradientBarClientTooltip implements ClientTooltipComponent {
    private final GradientBarTooltip tooltip;

    public GradientBarClientTooltip(GradientBarTooltip tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public int getHeight() {
        return tooltip.height();
    }

    @Override
    public int getWidth(@NotNull Font pFont) {
        return tooltip.width() + tooltip.labelWidth() + 2;
    }

    @Override
    public void renderImage(@NotNull Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        Component label = Component.literal(pFont.plainSubstrByWidth(tooltip.label().getString(), tooltip.labelWidth()));
        pGuiGraphics.drawString(pFont, label, pX, pY, 0xFFFFFF);
        RenderUtil.drawGradient(pGuiGraphics, pX + tooltip.labelWidth() + 2, pY, tooltip.width(), tooltip.height(),
                tooltip.startColor(), tooltip.endColor(), tooltip.fillColor(), tooltip.value(), tooltip.max());
    }
}
