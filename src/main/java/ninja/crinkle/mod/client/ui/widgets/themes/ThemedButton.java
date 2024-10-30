package ninja.crinkle.mod.client.ui.widgets.themes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.ui.themes.BoxTheme;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.client.ui.widgets.properties.Border;
import ninja.crinkle.mod.client.ui.widgets.properties.Box;
import ninja.crinkle.mod.client.ui.widgets.properties.Margin;
import ninja.crinkle.mod.client.ui.widgets.properties.Padding;
import ninja.crinkle.mod.util.ClientUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ThemedButton extends AbstractThemedButton {
    private Component label;

    public ThemedButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, Theme pTheme,
                        Consumer<AbstractThemedButton> onPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pTheme, onPress);
        setLabel(pMessage);
    }

    public Component getLabel() {
        return label;
    }

    public void setLabel(Component label) {
        this.label = label;
    }

    public void renderString(GuiGraphics pGuiGraphics, Font pFont, int pColor) {
        this.renderScrollingString(pGuiGraphics, pFont, 2, pColor);
    }

    @Override
    protected void renderContent(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, Box pBox) {
        super.renderContent(pGuiGraphics, pMouseX, pMouseY, pPartialTick, pBox);
        Minecraft minecraft = ClientUtil.getMinecraft();
        if (minecraft == null || label == null) return;
        this.renderString(pGuiGraphics, minecraft.font,
                getTheme().getForegroundColor().color() | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Builder builder(Theme theme) {
        return new Builder(theme);
    }

    public static class Builder {
        protected int x;
        protected int y;
        protected int width;
        protected int height;
        protected Component label;
        protected final Theme theme;
        protected Consumer<AbstractThemedButton> onPress;
        protected Predicate<AbstractThemedButton> activePredicate;
        private Margin margin = Margin.ZERO;
        private Border border = Border.ZERO;
        private Padding padding = Padding.ZERO;

        public Builder(Theme theme) {
            this.theme = theme;
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

        public Builder activePredicate(Predicate<AbstractThemedButton> activePredicate) {
            this.activePredicate = activePredicate;
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

        public ThemedButton build() {
            ThemedButton button = new ThemedButton(x, y, width, height, label, theme, onPress);
            button.setActivePredicate(activePredicate);
            button.setMargin(margin);
            button.setBorder(border);
            button.setPadding(padding);
            return button;
        }
    }
}
