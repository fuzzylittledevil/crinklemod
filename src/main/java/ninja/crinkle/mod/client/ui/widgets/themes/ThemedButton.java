package ninja.crinkle.mod.client.ui.widgets.themes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.util.ClientUtil;
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
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        Minecraft minecraft = ClientUtil.getMinecraft();
        if (minecraft == null || label == null) return;
        this.renderString(pGuiGraphics, minecraft.font,
                getTheme().getForegroundColor().color() | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    public static Builder builder(Theme theme) {
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

        public ThemedButton build() {
            ThemedButton button = new ThemedButton(x, y, width, height, label, theme, onPress);
            button.setActivePredicate(activePredicate);
            return button;
        }
    }
}
