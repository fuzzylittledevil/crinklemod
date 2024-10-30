package ninja.crinkle.mod.client.ui.widgets.themes;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.icons.Icons;
import ninja.crinkle.mod.client.renderers.GraphicsUtil;
import ninja.crinkle.mod.client.ui.themes.BoxTheme;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.client.ui.widgets.properties.Border;
import ninja.crinkle.mod.client.ui.widgets.properties.Box;
import ninja.crinkle.mod.client.ui.widgets.properties.Margin;
import ninja.crinkle.mod.client.ui.widgets.properties.Padding;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ThemedCheckbox extends AbstractThemedButton {
    private final Consumer<ThemedCheckbox> onChange;
    private boolean selected;

    public ThemedCheckbox(int pX, int pY, int pWidth, int pHeight, Component pMessage, Theme pTheme, boolean pSelected,
                          Consumer<ThemedCheckbox> onChange, Tooltip pTooltip) {
        super(pX, pY, pWidth, pHeight, pMessage, pTheme, null, BoxTheme.Type.CHECKBOX);
        this.selected = pSelected;
        this.onChange = onChange;
        setTooltip(pTooltip);
    }

    public static Builder builder(Theme theme, Component label, Consumer<ThemedCheckbox> onChange) {
        return new Builder(theme, label, onChange);
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean pSelected) {
        if (pSelected != this.selected) {
            this.selected = pSelected;
            if (this.onChange != null)
                this.onChange.accept(this);
        }
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        this.setSelected(!isSelected());
    }

    @Override
    protected void renderContent(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick,
                                 Box pBox) {
        if (isSelected()) {
            Color color = this.active ? this.getTheme().getSuccessColor() : this.getTheme().getInactiveColor();
            BoxTheme boxTheme = this.getTheme().getBorderTheme(BoxTheme.Type.CHECKBOX);
            GraphicsUtil graphicsUtil = new GraphicsUtil(pGuiGraphics);
            graphicsUtil.render(Icons.CHECKMARK,
                    pBox.x() + boxTheme.edgeWidth(),
                    pBox.y() + boxTheme.edgeHeight(),
                    Math.max(pBox.width() - boxTheme.edgeWidth() * 2, 0),
                    Math.max(pBox.height() - boxTheme.edgeHeight() * 2, 0),
                    color.withAlpha(this.alpha));
        }
    }

    public static class Builder {
        private final Theme theme;
        private final Component label;
        private final Consumer<ThemedCheckbox> onChange;
        private int x;
        private int y;
        private int width;
        private int height;
        private boolean selected;
        private Tooltip tooltip;
        private Color checkedColor;
        private Margin margin = Margin.ZERO;
        private Border border = Border.ZERO;
        private Padding padding = Padding.ZERO;
        private boolean debug = false;

        public Builder(Theme theme, Component label, Consumer<ThemedCheckbox> onChange) {
            this.theme = theme;
            this.label = label;
            this.onChange = onChange;
        }

        public Builder debug(boolean debug) {
            this.debug = debug;
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

        public Builder checkedColor(Color checkedColor) {
            this.checkedColor = checkedColor;
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

        public Builder selected(boolean selected) {
            this.selected = selected;
            return this;
        }

        public Builder tooltip(Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public ThemedCheckbox build() {
            ThemedCheckbox box = new ThemedCheckbox(this.x, this.y, this.width, this.height, this.label, this.theme, this.selected,
                    this.onChange, this.tooltip);
            box.setMargin(this.margin);
            box.setBorder(this.border);
            box.setPadding(this.padding);
            box.setDebug(this.debug);
            return box;
        }
    }
}
