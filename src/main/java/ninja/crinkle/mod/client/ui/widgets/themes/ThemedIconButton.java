package ninja.crinkle.mod.client.ui.widgets.themes;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.renderers.IconRenderer;
import ninja.crinkle.mod.icons.Icons;
import ninja.crinkle.mod.client.ui.themes.BoxTheme;
import ninja.crinkle.mod.client.ui.themes.Theme;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ThemedIconButton extends ThemedButton {
    private final Icons icon;

    public ThemedIconButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, Theme theme, Consumer<AbstractThemedButton> onPress, Icons icon) {
        super(pX, pY, pWidth, pHeight, pMessage, theme, onPress);
        this.icon = icon;
        this.setLabel(null);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if (icon != null) {
            BoxTheme borderTheme = getTheme().getBorderTheme(BoxTheme.Size.MEDIUM);
            IconRenderer.renderIcon(pGuiGraphics, icon, getX() + borderTheme.edgeWidth(),
                    getY() + borderTheme.edgeHeight(), getTheme().getSecondaryColor().withAlpha(this.alpha));
        }
    }

    public static Builder builder(Theme theme, Icons icon) {
        return new Builder(theme, icon);
    }

    public static ThemedButton.Builder builder(Theme ignoredTheme) {
        throw new NotImplementedException("Use ThemedIconButton.builder(Theme theme, Icon icon) instead");
    }

    public static class Builder {
        private int x;
        private int y;
        private int width;
        private int height;
        private Component label;
        private final Theme theme;
        private Consumer<AbstractThemedButton> onPress;
        private final Icons icon;

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

        public Builder label(Component label) {
            this.label = label;
            return this;
        }

        public Builder onPress(Consumer<AbstractThemedButton> onPress) {
            this.onPress = onPress;
            return this;
        }

        public ThemedIconButton build() {
            ThemedIconButton button = new ThemedIconButton(x, y, width, height, label, theme, onPress, icon);
            button.setAutoSize(false);
            return button;
        }
    }
}
