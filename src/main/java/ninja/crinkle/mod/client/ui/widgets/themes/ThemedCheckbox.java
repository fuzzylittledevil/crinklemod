package ninja.crinkle.mod.client.ui.widgets.themes;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.icons.Icons;
import ninja.crinkle.mod.client.renderers.IconRenderer;
import ninja.crinkle.mod.client.ui.themes.BoxTheme;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.client.ui.widgets.Label;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ThemedCheckbox extends AbstractThemedButton {
    private boolean selected;
    private final boolean showLabel;
    private final boolean labelRight;
    private final Label label;
    private final Consumer<ThemedCheckbox> onChange;

    public ThemedCheckbox(int pX, int pY, int pWidth, int pHeight, Component pMessage, Theme pTheme, boolean pSelected,
                          boolean pShowLabel, boolean pLabelRight, Consumer<ThemedCheckbox> onChange, Label pLabel,
                          Tooltip pTooltip) {
        super(pX, pY, pWidth, pHeight, pMessage, pTheme, null, BoxTheme.Type.CHECKBOX);
        this.showLabel = pShowLabel;
        this.selected = pSelected;
        this.onChange = onChange;
        this.label = pLabel;
        this.labelRight = pLabelRight;
        if (this.label != null) {
            this.label.setTooltip(pTooltip);
        }
        setTooltip(pTooltip);
    }

    public boolean isSelected() {
        return this.selected;
    }

    public boolean isShowLabel() {
        return this.showLabel && this.label != null;
    }

    public boolean isLabelRight() {
        return this.labelRight;
    }

    public Label getLabel() {
        return this.label;
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
        this.setSelected(!isSelected());
    }

    public void setSelected(boolean pSelected) {
        if (pSelected != this.selected) {
            this.selected = pSelected;
            if (this.onChange != null)
                this.onChange.accept(this);
        }
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.showLabel) {
            if (this.labelRight) {
                this.label.setX(this.getX() + this.getWidth() + 4);
                this.label.setY(this.getY() + (this.getHeight() - this.label.getHeight()) / 2);
            } else {
                this.label.setX(this.getX() - this.label.getWidth() - 4);
                this.label.setY(this.getY() + (this.getHeight() - this.label.getHeight()) / 2);
            }
            this.label.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if (isSelected()) {
            Color color = this.active ? this.getTheme().getSuccessColor() : this.getTheme().getInactiveColor();
            BoxTheme boxTheme = this.getTheme().getBorderTheme(BoxTheme.Type.CHECKBOX);
            IconRenderer iconRenderer = new IconRenderer(pGuiGraphics);
            iconRenderer.render(Icons.CHECKMARK, this.getX() + boxTheme.edgeWidth(),
                    this.getY() + boxTheme.edgeHeight(), color.withAlpha(this.alpha));
        }
    }

    public static Builder builder(Theme theme, Component label, Consumer<ThemedCheckbox> onChange) {
        return new Builder(theme, label, onChange);
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
        private boolean showLabel;
        private boolean labelRight;
        private Label labelWidget;
        private Tooltip tooltip;

        public Builder(Theme theme, Component label, Consumer<ThemedCheckbox> onChange) {
            this.theme = theme;
            this.label = label;
            this.onChange = onChange;
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

        public Builder label(Label labelWidget, boolean labelRight) {
            this.labelWidget = labelWidget;
            this.labelRight = labelRight;
            this.showLabel = true;
            return this;
        }

        public Builder tooltip(Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public ThemedCheckbox build() {
            return new ThemedCheckbox(this.x, this.y, this.width, this.height, this.label, this.theme, this.selected,
                    this.showLabel, this.labelRight, this.onChange, this.labelWidget, this.tooltip);
        }
    }
}
