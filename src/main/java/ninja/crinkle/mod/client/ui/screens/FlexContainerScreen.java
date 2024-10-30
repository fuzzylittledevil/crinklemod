package ninja.crinkle.mod.client.ui.screens;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.ui.themes.BoxTheme;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.client.ui.widgets.properties.Box;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedBorderBox;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class FlexContainerScreen extends Screen {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Theme theme;
    private final ContainerInfo containerInfo = new ContainerInfo(0, 0, 0, 0);
    private ThemedBorderBox borderBox;
    private final List<RelativeWidget> flexedWidgets = new ArrayList<>();
    private int screenWidth;
    private int screenHeight;

    public FlexContainerScreen(Component title, Theme theme) {
        super(title);
        this.theme = theme;
        this.borderBox = new ThemedBorderBox(0, 0, 0, 0, title, theme, BoxTheme.Type.PANEL);
    }

    public void setPadding(int padding) {
        containerInfo.padding = padding;
    }

    protected void flex() {
        //LOGGER.debug("Flexing screen");
        int maxX = 0;
        int maxY = 0;
        flexedWidgets.clear();
        for (Renderable renderable : renderables) {
            if (renderable instanceof ThemedBorderBox themedBorderBox) {
                Box box = themedBorderBox.getBox();
                maxX = Math.max(box.x() + box.width(), maxX);
                maxY = Math.max(box.y() + box.height(), maxY);
            } else if (renderable instanceof AbstractWidget widget && widget.visible) {
                maxX = Math.max(widget.getX() + widget.getWidth(), maxX);
                maxY = Math.max(widget.getY() + widget.getHeight(), maxY);
            } else {
                LOGGER.warn("[Flex Reset] Unknown renderable: {}", renderable);
            }
        }
        containerInfo.width = maxX;
        containerInfo.height = maxY;
        containerInfo.leftPos = (width - containerInfo.width) / 2;
        containerInfo.topPos = (height - containerInfo.height) / 2;
        for (Renderable renderable : renderables) {
            if (renderable instanceof ThemedBorderBox themedBorderBox) {
                Box box = themedBorderBox.getBox();
                flexedWidgets.add(new RelativeWidget(themedBorderBox, box.x(), box.y()));
                themedBorderBox.setFlex(new Box(box.x() + containerInfo.leftPos, box.y() + containerInfo.topPos,
                        box.width(), box.height()));
            } else if (renderable instanceof AbstractWidget widget && widget.visible) {
                flexedWidgets.add(new RelativeWidget(widget, widget.getX(), widget.getY()));
                widget.setX(widget.getX() + containerInfo.leftPos);
                widget.setY(widget.getY() + containerInfo.topPos);
            } else {
                LOGGER.warn("[Flex] Unknown renderable: {}", renderable);
            }
        }
        BoxTheme borderTheme = theme.getBorderTheme(BoxTheme.Type.PANEL);
        containerInfo.width += borderTheme.edgeWidth() + containerInfo.padding;
        containerInfo.height += borderTheme.edgeHeight() + containerInfo.padding;
        borderBox = new ThemedBorderBox(containerInfo.leftPos, containerInfo.topPos, containerInfo.width,
                containerInfo.height, title, theme, BoxTheme.Type.PANEL);
    }

    public void refreshFlex() {
        for (RelativeWidget widget : flexedWidgets) {
            if (widget.widget instanceof ThemedBorderBox box) {
                box.setFlex(Box.ZERO);
            } else {
                widget.widget.setX(widget.relativeX);
                widget.widget.setY(widget.relativeY);
            }
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

    @Override
    public void tick() {
        super.tick();
        int newScreenWidth = Objects.requireNonNull(minecraft).getWindow().getGuiScaledWidth();
        int newScreenHeight = minecraft.getWindow().getGuiScaledHeight();
        if (newScreenWidth != screenWidth || newScreenHeight != screenHeight) {
            LOGGER.debug("Screen resized from ({}, {}) to ({}, {})", screenWidth, screenHeight, newScreenWidth, newScreenHeight);
            screenWidth = newScreenWidth;
            screenHeight = newScreenHeight;
            refreshFlex();
        }
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
