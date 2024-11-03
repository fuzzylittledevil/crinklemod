package ninja.crinkle.mod.client.ui.widgets.themes;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.icons.Icons;
import ninja.crinkle.mod.client.renderers.GraphicsUtil;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.client.ui.widgets.layouts.Container;
import ninja.crinkle.mod.client.ui.widgets.properties.Border;
import ninja.crinkle.mod.client.ui.widgets.properties.Box;
import ninja.crinkle.mod.client.ui.widgets.properties.Margin;
import ninja.crinkle.mod.client.ui.widgets.properties.Padding;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ThemedIconButton extends ThemedButton {
    private Icons icon;
    private Color iconColor;

    public ThemedIconButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, Theme theme, Consumer<AbstractThemedButton> onPress, Icons icon) {
        super(pX, pY, pWidth, pHeight, pMessage, theme, onPress);
        this.icon = icon;
        this.setLabel(null);
    }

    public static Builder builder(Theme theme, Icons icon) {
        return new Builder(theme, icon);
    }

    public static ThemedButton.Builder builder(Theme ignoredTheme) {
        throw new NotImplementedException("Use ThemedIconButton.builder(Theme theme, Icon icon) instead");
    }

    public Icons getIcon() {
        return icon;
    }

    public void setIcon(Icons icon) {
        this.icon = icon;
    }

    @Override
    protected void renderContent(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick,
                                 Box pBox) {
        super.renderContent(pGuiGraphics, pMouseX, pMouseY, pPartialTick, pBox);
        if (icon != null) {
            Color color = active ? getTheme().getForegroundColor() : getTheme().getInactiveColor();
            if (this.iconColor != null) {
                if (this.iconColor == Color.RAINBOW)
                    color = Color.rainbow(1000, 0);
                else
                    color = this.iconColor;
            }
            GraphicsUtil graphicsUtil = new GraphicsUtil(pGuiGraphics);
            graphicsUtil.render(icon, pBox.x(), pBox.y(), pBox.width(), pBox.height(), color.withAlpha(this.alpha));
        }
    }

    public Color getIconColor() {
        return iconColor;
    }

    public void setIconColor(Color iconColor) {
        this.iconColor = iconColor;
    }

    public static class Builder {
        private final Theme theme;
        private final Icons icon;
        private int x;
        private int y;
        private int width;
        private int height;
        private Component label;
        private Component tooltip;
        private Consumer<AbstractThemedButton> onPress;
        private Predicate<AbstractThemedButton> activePredicate;
        private Color iconColor;
        private Margin margin = Margin.ZERO;
        private Border border = Border.ZERO;
        private Padding padding = Padding.ZERO;

        public Builder(Theme theme, Icons icon) {
            this.theme = theme;
            this.icon = icon;
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

        public Builder label(Component label) {
            this.label = label;
            return this;
        }

        public Builder tooltip(Component tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder onPress(Consumer<AbstractThemedButton> onPress) {
            this.onPress = onPress;
            return this;
        }

        public Builder activePredicate(Predicate<AbstractThemedButton> activePredicate) {
            this.activePredicate = activePredicate;
            return this;
        }

        public Builder iconColor(Color iconColor) {
            this.iconColor = iconColor;
            return this;
        }

        public Builder margin(Margin margin) {
            this.margin = margin;
            return this;
        }

        public Builder border(Border border) {
            this.border = border;
            return this;
        }

        public Builder padding(Padding padding) {
            this.padding = padding;
            return this;
        }

        public ThemedIconButton build() {
            ThemedIconButton button = new ThemedIconButton(x, y, width, height, label, theme, onPress, icon);
            button.setActivePredicate(activePredicate);
            if (tooltip != null)
                button.setTooltip(Tooltip.create(tooltip));
            if (iconColor != null)
                button.setIconColor(iconColor);
            button.setMargin(margin);
            button.setBorder(border);
            button.setPadding(padding);
            return button;
        }
    }
}
