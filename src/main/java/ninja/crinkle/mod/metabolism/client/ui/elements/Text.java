package ninja.crinkle.mod.metabolism.client.ui.elements;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class Text {
    public static final int DEFAULT_COLOR = 0x404040;
    private Font font;
    private final Supplier<Component> valueSupplier;
    private int color;
    private boolean dropShadow;

    public Text(Font font, Supplier<Component> valueSupplier, int color, boolean dropShadow) {
        this.font = font;
        this.valueSupplier = valueSupplier;
        this.color = color;
        this.dropShadow = dropShadow;
    }

    public Text(Font font, Supplier<Component> valueSupplier) {
        this(font, valueSupplier, DEFAULT_COLOR, false);
    }

    public static Builder builder(Font font, Supplier<Component> valueSupplier) {
        return new Builder(font, valueSupplier);
    }

    public int getWidth() {
        if (getValue() != null) {
            return font.width(getValue());
        }
        return 0;
    }

    @Nullable
    public Component getValue() {
        if (valueSupplier != null) {
            return valueSupplier.get();
        }
        return null;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isDropShadow() {
        return dropShadow;
    }

    public void setDropShadow(boolean dropShadow) {
        this.dropShadow = dropShadow;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void draw(GuiGraphics graphics, int x, int y) {
        if (getValue() != null) {
            graphics.drawString(font, getValue(), x, y, color, dropShadow);
        }
    }

    public static class Builder {
        private int color = DEFAULT_COLOR;
        private boolean dropShadow;
        private final Font font;
        private final Supplier<Component> valueSupplier;

        public Builder(Font font, Supplier<Component> valueSupplier) {
            this.font = font;
            this.valueSupplier = valueSupplier;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder dropShadow(boolean dropShadow) {
            this.dropShadow = dropShadow;
            return this;
        }

        public Text build() {
            return new Text(font, valueSupplier, color, dropShadow);
        }
    }
}
