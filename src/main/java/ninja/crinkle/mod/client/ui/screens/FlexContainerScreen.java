package ninja.crinkle.mod.client.ui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.ui.themes.BorderThemeData;
import ninja.crinkle.mod.client.ui.themes.BorderThemeSize;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedBorderBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class FlexContainerScreen extends Screen {
    private final Theme theme;
    private final ContainerInfo containerInfo = new ContainerInfo(0, 0, 0, 0);
    private ThemedBorderBox borderBox;
    private final List<RelativeWidget> flexedWidgets = new ArrayList<>();

    public FlexContainerScreen(Component title, Theme theme) {
        super(title);
        this.theme = theme;
        this.borderBox = new ThemedBorderBox(0, 0, 0, 0, title, theme, BorderThemeSize.LARGE);
    }

    public void setPadding(int padding) {
        containerInfo.padding = padding;
    }

    protected void flex() {
        int maxX = 0;
        int maxY = 0;
        flexedWidgets.clear();
        for (Renderable renderable : renderables) {
            if (renderable instanceof AbstractWidget widget && widget.visible) {
                if (widget.getX() + widget.getWidth() > maxX) {
                    maxX = widget.getX() + widget.getWidth();
                }
                if (widget.getY() + widget.getHeight() > maxY) {
                    maxY = widget.getY() + widget.getHeight();
                }
            }
        }
        containerInfo.width = maxX;
        containerInfo.height = maxY;
        containerInfo.leftPos = (width - containerInfo.width) / 2;
        containerInfo.topPos = (height - containerInfo.height) / 2;
        for (Renderable renderable : renderables) {
            if (renderable instanceof AbstractWidget widget && widget.visible) {
                flexedWidgets.add(new RelativeWidget(widget, widget.getX(), widget.getY()));
                widget.setX(widget.getX() + containerInfo.leftPos);
                widget.setY(widget.getY() + containerInfo.topPos);
            }
        }
        BorderThemeData borderTheme = theme.getBorderTheme(BorderThemeSize.LARGE);
        containerInfo.width += borderTheme.edgeWidth() + containerInfo.padding;
        containerInfo.height += borderTheme.edgeHeight() + containerInfo.padding;
        borderBox = new ThemedBorderBox(containerInfo.leftPos, containerInfo.topPos, containerInfo.width, containerInfo.height, title, theme, BorderThemeSize.LARGE);
    }

    public void refreshFlex() {
        for (RelativeWidget widget : flexedWidgets) {
            widget.widget.setX(widget.relativeX);
            widget.widget.setY(widget.relativeY);
        }
        containerInfo.leftPos = 0;
        containerInfo.topPos = 0;
        containerInfo.width = 0;
        containerInfo.height = 0;
        flexedWidgets.clear();
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics graphics) {
        super.renderBackground(graphics);
        borderBox.render(graphics, 0, 0, 0);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (flexedWidgets.isEmpty()) {
            flex();
        }
        renderBackground(graphics);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

    public Theme getTheme() {
        return theme;
    }

    private static class ContainerInfo {
        protected int width;
        protected int height;
        protected int leftPos;
        protected int topPos;
        protected int padding;

        protected ContainerInfo(int width, int height, int leftPos, int topPos) {
            this.width = width;
            this.height = height;
            this.leftPos = leftPos;
            this.topPos = topPos;
        }
    }

    private record RelativeWidget(AbstractWidget widget, int relativeX, int relativeY) {
    }
}
