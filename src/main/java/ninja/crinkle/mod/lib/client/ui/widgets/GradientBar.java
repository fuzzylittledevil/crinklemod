package ninja.crinkle.mod.lib.client.ui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.lib.client.ui.elements.Text;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class GradientBar extends AbstractWidget {
    public static final int DEFAULT_TEXT_COLOR = 0xffffffff;
    private final Supplier<Double> valueSupplier;
    private final Supplier<Double> maxValueSupplier;
    private int colorFrom;
    private int colorTo;
    private int backgroundColor;
    private final Text text;
    private final Text hoverText;

    protected GradientBar(int pX, int pY, int pWidth, int pHeight, Component pMessage, Supplier<Double> pValueSupplier,
                          Supplier<Double> pMaxValueSupplier, int colorFrom, int colorTo, int backgroundColor,
                          Text text, Text hoverText) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.valueSupplier = pValueSupplier;
        this.maxValueSupplier = pMaxValueSupplier;
        this.text = text;
        this.hoverText = hoverText;
        this.colorFrom = colorFrom;
        this.colorTo = colorTo;
        this.backgroundColor = backgroundColor;
    }

    public static GradientBar.@NotNull Builder builder(Component message) {
        return new GradientBar.Builder(message);
    }

    public void setColorFrom(int colorFrom) {
        this.colorFrom = colorFrom;
    }

    public void setColorTo(int colorTo) {
        this.colorTo = colorTo;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @SuppressWarnings("unused")
    public static class Builder {
        private int x;
        private int y;
        private int width;
        private int height;
        private int colorFrom;
        private int colorTo;
        private int backgroundColor;
        private final Component message;
        private Supplier<Double> valueSupplier;
        private Supplier<Double> maxValueSupplier;
        private Text text;
        private Text hoverText;

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

        public Builder valueSupplier(Supplier<Double> valueSupplier) {
            this.valueSupplier = valueSupplier;
            return this;
        }

        public Builder maxValueSupplier(Supplier<Double> maxValueSupplier) {
            this.maxValueSupplier = maxValueSupplier;
            return this;
        }

        public GradientBar build() {
            return new GradientBar(x, y, width, height, message, valueSupplier, maxValueSupplier, colorFrom, colorTo,
                    backgroundColor, text, hoverText);
        }
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        double value = valueSupplier.get();
        double maxValue = maxValueSupplier.get();
        double percentage = value / maxValue;

        int offset = (int) (percentage * width);

        pGuiGraphics.fillGradient(getX(), getY(), getX() + offset, getY() + height, colorFrom, colorTo);
        pGuiGraphics.fill(getX() + offset, getY(), getX() + width, getY() + height, backgroundColor);
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
