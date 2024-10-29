package ninja.crinkle.mod.client.ui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class Label extends AbstractWidget {
    public static final int DEFAULT_COLOR = 0x404040;
    private final int wrapWidth;
    private Component value;
    private Supplier<Component> valueSupplier;
    private Font font;
    private int color;
    private boolean dropShadow;

    public Label(int pX, int pY, Component pMessage, Supplier<Component> valueSupplier, Font font, int color,
                 boolean dropShadow, boolean visible, int wrapWidth) {
        super(pX, pY, font.width(pMessage), font.lineHeight, pMessage);
        this.wrapWidth = wrapWidth;
        this.value = pMessage;
        this.valueSupplier = valueSupplier;
        this.font = font;
        this.color = color;
        this.dropShadow = dropShadow;
        this.visible = visible;
    }

    public static Builder builder(Font font, Component value) {
        return new Builder(font, value);
    }

    public static Builder builder(Font font, Supplier<Component> valueSupplier) {
        return new Builder(font, valueSupplier);
    }

    public void setValueSupplier(Supplier<Component> valueSupplier) {
        this.valueSupplier = valueSupplier;
    }

    public int getWrapWidth() {
        return wrapWidth;
    }

    @Override
    public int getWidth() {
        return wrapWidth == 0 ? font.width(value) : wrapWidth;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Component getValue() {
        return value;
    }

    public void setValue(Component value) {
        this.value = value;
    }

    public boolean getDropShadow() {
        return dropShadow;
    }

    public void setDropShadow(boolean dropShadow) {
        this.dropShadow = dropShadow;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (!visible) return;
        if (valueSupplier != null) {
            value = valueSupplier.get();
        }
        if (getWrapWidth() == 0) {
            pGuiGraphics.drawString(font, value, getX(), getY(), color, getDropShadow());
            return;
        }
        List<FormattedCharSequence> lines = font.split(value, wrapWidth);
        for (int i = 0; i < lines.size(); i++) {
            pGuiGraphics.drawString(font, lines.get(i), getX(), getY() + (i * font.lineHeight), color, getDropShadow());
        }
    }

    public int getLineHeight() {
        return font.lineHeight * (wrapWidth == 0 ? 1 : font.split(value, wrapWidth).size());
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }

    public static class Builder {
        private final Component value;
        private final Supplier<Component> valueSupplier;
        private final Font font;
        private int x;
        private int y;
        private int color = DEFAULT_COLOR;
        private boolean dropShadow;
        private boolean visible = true;
        private int wrapWidth;

        public Builder(Font font, Component value) {
            this.font = font;
            this.value = value;
            this.valueSupplier = null;
        }

        public Builder(Font font, Supplier<Component> valueSupplier) {
            this.font = font;
            this.value = Component.literal("Dynamic Label");
            this.valueSupplier = valueSupplier;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder pos(int x, int y) {
            return x(x).y(y);
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder dropShadow(boolean dropShadow) {
            this.dropShadow = dropShadow;
            return this;
        }

        public Label build() {
            return new Label(x, y, value, valueSupplier, font, color, dropShadow, visible, wrapWidth);
        }

        public Builder visible(boolean value) {
            this.visible = value;
            return this;
        }

        public Builder wrapWidth(int wrapWidth) {
            this.wrapWidth = wrapWidth;
            return this;
        }
    }
}
