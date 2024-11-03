package ninja.crinkle.mod.client.ui.widgets.layouts;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.ui.themes.BoxTheme;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.client.ui.widgets.properties.Border;
import ninja.crinkle.mod.client.ui.widgets.properties.Box;
import ninja.crinkle.mod.client.ui.widgets.properties.Margin;
import ninja.crinkle.mod.client.ui.widgets.properties.Padding;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedBorderBox;
import ninja.crinkle.mod.util.WidgetUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Container extends ThemedBorderBox {
    private final List<AbstractWidget> children = new ArrayList<>();
    private final LayoutManager layoutManager;

    public Container(int x, int y, int width, int height, Theme theme, LayoutManager layoutManager,
                     BoxTheme.Type borderThemeType) {
        super(x, y, width, height, Component.empty(), theme, borderThemeType);
        this.layoutManager = layoutManager;
    }

    public static Builder builder(Theme theme) {
        return new Container.Builder(theme);
    }


    public void addChild(AbstractWidget child) {
        children.add(child);
        updateLayout();
    }

    public List<AbstractWidget> getChildren() {
        return children;
    }

    public void updateLayout() {
        if (layoutManager != null) {
            layoutManager.arrange(this);
        }
        for (AbstractWidget child : children) {
            if (child instanceof Container container) {
                container.updateLayout();
            }
        }
    }

    @Override
    protected void renderContent(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouse, float pPartialTick, Box pBox) {
        super.renderContent(pGuiGraphics, pMouseX, pMouse, pPartialTick, pBox);
        for (AbstractWidget child : children) {
            child.render(pGuiGraphics, pMouseX, pMouse, pPartialTick);
        }
    }

    public int getTotalWidth() {
        return getChildren().stream()
                .mapToInt(WidgetUtil::widthOf)
                .sum() + (getChildren().size() - 1) * layoutManager.spacing();
    }

    public int getTotalHeight() {
        return getChildren().stream()
                .mapToInt(WidgetUtil::heightOf)
                .sum() + (getChildren().size() - 1) * layoutManager.spacing();
    }

    public static class Builder {
        private final Theme theme;
        private int x;
        private int y;
        private int width;
        private int height;
        private LayoutManager layoutManager = null;
        private BoxTheme.Type borderThemeType = BoxTheme.Type.NONE;
        private Margin margin = Margin.ZERO;
        private Border border = Border.ZERO;
        private Padding padding = Padding.ZERO;


        public Builder(Theme theme) {
            this.theme = theme;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder vertical(Alignment alignment, int spacing) {
            this.layoutManager = new VerticalLayout(alignment, spacing);
            return this;
        }

        public Builder horizontal(Alignment alignment, int spacing) {
            this.layoutManager = new HorizontalLayout(alignment, spacing);
            return this;
        }

        public Builder borderThemeType(BoxTheme.Type borderThemeType) {
            this.borderThemeType = borderThemeType;
            return this;
        }

        public Builder margin(int top, int bottom, int left, int right) {
            this.margin = new Margin(top, bottom, left, right);
            return this;
        }

        public Builder margin(int horizontal, int vertical) {
            return margin(vertical, vertical, horizontal, horizontal);
        }

        public Builder margin(int all) {
            return margin(all, all, all, all);
        }

        public Builder border(int top, int bottom, int left, int right, Color color) {
            this.border = new Border(top, bottom, left, right, color);
            return this;
        }

        public Builder border(int horizontal, int vertical, Color color) {
            return border(vertical, vertical, horizontal, horizontal, color);
        }

        public Builder border(int all, Color color) {
            return border(all, all, all, all, color);
        }

        public Builder padding(int top, int bottom, int left, int right) {
            this.padding = new Padding(top, bottom, left, right);
            return this;
        }

        public Builder padding(int horizontal, int vertical) {
            return padding(vertical, vertical, horizontal, horizontal);
        }

        public Builder padding(int all) {
            return padding(all, all, all, all);
        }

        public Container build() {
            Container c = new Container(x, y, width, height, theme, layoutManager, borderThemeType);
            c.setMargin(margin);
            c.setBorder(border);
            c.setPadding(padding);
            return c;
        }
    }
}
