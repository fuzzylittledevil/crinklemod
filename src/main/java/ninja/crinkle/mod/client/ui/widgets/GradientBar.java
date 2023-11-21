package ninja.crinkle.mod.client.ui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.ui.elements.Text;
import ninja.crinkle.mod.util.RenderUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class GradientBar extends AbstractWidget {
    private final Supplier<Number> valueSupplier;
    private final Supplier<Number> maxValueSupplier;
    private final int colorFrom;
    private final int colorTo;
    private final int backgroundColor;
    private final Text text;
    private final Text hoverText;

    protected GradientBar(int pX, int pY, int pWidth, int pHeight, Component pMessage, Supplier<Number> pValueSupplier,
                          Supplier<Number> pMaxValueSupplier, int colorFrom, int colorTo, int backgroundColor,
                          Text text, Text hoverText, Tooltip tooltip) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.valueSupplier = pValueSupplier;
        this.maxValueSupplier = pMaxValueSupplier;
        this.text = text;
        this.hoverText = hoverText;
        this.colorFrom = colorFrom;
        this.colorTo = colorTo;
        this.backgroundColor = backgroundColor;
        this.setTooltip(tooltip);
    }

    public static GradientBar.@NotNull Builder builder(Component message) {
        return new GradientBar.Builder(message);
    }

    public static class Builder {
        private int x;
        private int y;
        private int width;
        private int height;
        private int colorFrom;
        private int colorTo;
        private int backgroundColor;
        private final Component message;
        private Supplier<Number> valueSupplier;
        private Supplier<Number> maxValueSupplier;
        private Text text;
        private Text hoverText;
        private Tooltip tooltip;

        public Builder(Component message) {
            this.message = message;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder pos(int x, int y) {
            return x(x)
                    .y(y);
        }

        public Builder bounds(int x, int y, int width, int height) {
            return pos(x, y)
                    .width(width)
                    .height(height);
        }

        public Builder colorFrom(int colorFrom) {
            this.colorFrom = colorFrom;
            return this;
        }

        public Builder colorTo(int colorTo) {
            this.colorTo = colorTo;
            return this;
        }

        public Builder backgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder gradientColor(int colorFrom, int colorTo, int backgroundColor) {
            return colorFrom(colorFrom)
                    .colorTo(colorTo)
                    .backgroundColor(backgroundColor);
        }

        public Builder text(Text text) {
            this.text = text;
            return this;
        }

        public Builder hoverText(Text hoverText) {
            this.hoverText = hoverText;
            return this;
        }

        public Builder valueSupplier(Supplier<Number> valueSupplier) {
            this.valueSupplier = valueSupplier;
            return this;
        }

        public Builder maxValueSupplier(Supplier<Number> maxValueSupplier) {
            this.maxValueSupplier = maxValueSupplier;
            return this;
        }

        public Builder tooltip(Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public GradientBar build() {
            return new GradientBar(x, y, width, height, message, valueSupplier, maxValueSupplier, colorFrom, colorTo,
                    backgroundColor, text, hoverText, tooltip);
        }
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int value = valueSupplier.get().intValue();
        int maxValue = maxValueSupplier.get().intValue();
        RenderUtil.drawGradient(pGuiGraphics, getX(), getY(), width, height, colorFrom, colorTo, backgroundColor, value, maxValue);
        if (isHovered()) {
            if (hoverText != null) {
                hoverText.draw(pGuiGraphics, getX() + (width - hoverText.getWidth()) / 2, getY() + height / 2 - hoverText.getFont().lineHeight / 2);
            }
        } else if (text != null)
            text.draw(pGuiGraphics, getX() + (width - text.getWidth()) / 2, getY() + height / 2 - text.getFont().lineHeight / 2);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }
}
