package ninja.crinkle.mod.client.ui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.icons.Icons;
import ninja.crinkle.mod.client.renderers.GraphicsUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class Icon extends AbstractWidget {
    private Icons icon;
    private Supplier<Icons> iconSupplier;
    private Color color;

    public Icon(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
        super(pX, pY, pWidth, pHeight, pMessage);
        setIcon(Icons.WARNING1);
        setColor(Color.WHITE);
    }

    public static Builder builder(Icons icon) {
        return new Builder(icon);
    }

    public static Builder builder(Supplier<Icons> iconSupplier) {
        return new Builder(iconSupplier);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        GraphicsUtil graphicsUtil = new GraphicsUtil(guiGraphics);
        Icons i = getIcon();
        if (getIconSupplier() != null) {
            i = getIconSupplier().get();
        }
        graphicsUtil.render(i, getX(), getY(), getWidth(), getHeight(), getColor());
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }

    public Icons getIcon() {
        return icon;
    }

    public void setIcon(Icons icon) {
        this.icon = icon;
    }

    public Color getColor() {
        return color == null ? Color.WHITE : color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Supplier<Icons> getIconSupplier() {
        return iconSupplier;
    }

    public void setIconSupplier(Supplier<Icons> iconSupplier) {
        this.iconSupplier = iconSupplier;
    }

    public static class Builder {
        private final Icons icon;
        private final Supplier<Icons> iconSupplier;
        private Color color;
        private int x;
        private int y;
        private int width;
        private int height;
        private Component message;

        public Builder(Icons icon) {
            this.icon = icon;
            this.iconSupplier = null;
        }

        public Builder(Supplier<Icons> iconSupplier) {
            this.icon = null;
            this.iconSupplier = iconSupplier;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
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

        public Builder bounds(int x, int y, int width, int height) {
            return this.x(x)
                    .y(y)
                    .width(width)
                    .height(height);
        }

        public Builder message(Component message) {
            this.message = message;
            return this;
        }

        public Icon build() {
            if (message == null)
                message = Component.literal(this.icon != null ? this.icon.name() : "Dynamic Icon");
            Icon icon = new Icon(x, y, width, height, message);
            icon.setIcon(this.icon);
            icon.setColor(this.color);
            return icon;
        }
    }
}
