package ninja.crinkle.mod.lib.client.tooltips;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.lib.common.tooltips.GradientBarTooltip;
import ninja.crinkle.mod.lib.common.util.RenderUtil;
import org.davidmoten.text.utils.WordWrap;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class GradientBarClientTooltip implements ClientTooltipComponent {
    private final GradientBarTooltip tooltip;

    public GradientBarClientTooltip(GradientBarTooltip tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public int getHeight() {
        return tooltip.getHeight();
    }

    @Override
    public int getWidth(@NotNull Font pFont) {
        return tooltip.getWidth() + tooltip.getLabelWidth() + 2;
    }

    @Override
    public void renderImage(@NotNull Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        Component label = Component.literal(pFont.plainSubstrByWidth(tooltip.getLabel().getString(), tooltip.getLabelWidth()));
        pGuiGraphics.drawString(pFont, label, pX, pY, 0xFFFFFF);
        RenderUtil.drawGradient(pGuiGraphics, pX + tooltip.getLabelWidth() + 2, pY, tooltip.getWidth(), tooltip.getHeight(),
                tooltip.getStartColor(), tooltip.getEndColor(), tooltip.getFillColor(), tooltip.getValue(), tooltip.getMax());
    }
}
