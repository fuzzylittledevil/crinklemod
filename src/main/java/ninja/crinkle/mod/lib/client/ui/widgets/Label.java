package ninja.crinkle.mod.lib.client.ui.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class Label extends AbstractWidget {
    public static final int DEFAULT_COLOR = 0x404040;
    private Component value;
    private Font font;
    private int color;
    private boolean dropShadow;

    public Label(int pX, int pY, Component pMessage, Font font, int color, boolean dropShadow, boolean visible) {
        super(pX, pY, font.width(pMessage), font.lineHeight, pMessage);
        this.value = pMessage;
        this.font = font;
        this.color = color;
        this.dropShadow = dropShadow;
        this.visible = visible;
    }

    public static Builder builder(Font font, Component value) {
        return new Builder(font, value);
    }

    public static class Builder {
        private int x;
        private int y;
        private Component value;
        private Font font;
        private int color = DEFAULT_COLOR;
        private boolean dropShadow;
        private boolean visible = true;

        public Builder(Font font, Component value) {
            this.font = font;
            this.value = value;
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

        public Builder value(Component value) {
            this.value = value;
            return this;
        }

        public Builder font(Font font) {
            this.font = font;
            return this;
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
            return new Label(x, y, value, font, color, dropShadow, visible);
        }

        public Builder visible(boolean value) {
            this.visible = value;
            return this;
        }
    }

    public int getColor() {
        return color;
    }

    public Font getFont() {
        return font;
    }

    public Component getValue() {
        return value;
    }

    public boolean getDropShadow() {
        return dropShadow;
    }

    public void setValue(Component value) {
        this.value = value;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDropShadow(boolean dropShadow) {
        this.dropShadow = dropShadow;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.drawString(font, value, getX(), getY(), color, dropShadow);
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }
}
