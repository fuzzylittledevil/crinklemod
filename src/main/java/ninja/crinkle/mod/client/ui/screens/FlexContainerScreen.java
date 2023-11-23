package ninja.crinkle.mod.client.ui.screens;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.CrinkleMod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class FlexContainerScreen extends Screen {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation DEFAULT_THEME = new ResourceLocation(CrinkleMod.MODID,
            "textures/gui/window_border_purple.png");
    public static final BorderInfo DEFAULT_BORDER_INFO = new BorderInfo(4, 4, 4, 4);
    public static final TextureInfo DEFAULT_TEXTURE_INFO = new TextureInfo(DEFAULT_THEME, 9, 9, 0, 0);
    private final ContainerInfo containerInfo;
    private final TextureInfo textureInfo;
    private final BorderInfo borderInfo;
    private final List<RelativeWidget> flexedWidgets = new ArrayList<>();

    public FlexContainerScreen(Component title, TextureInfo textureInfo,
                               BorderInfo borderInfo) {
        super(title);
        this.containerInfo = new ContainerInfo(0, 0, 0, 0);
        this.textureInfo = textureInfo;
        this.borderInfo = borderInfo;
    }

    public FlexContainerScreen(Component title, TextureInfo textureInfo) {
        this(title, textureInfo, DEFAULT_BORDER_INFO);
    }

    public FlexContainerScreen(Component title) {
        this(title, DEFAULT_TEXTURE_INFO);
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
        containerInfo.width += borderInfo.edgeWidth + containerInfo.padding;
        containerInfo.height += borderInfo.edgeHeight + containerInfo.padding;
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
        graphics.blitNineSlicedSized(textureInfo.texture, containerInfo.leftPos, containerInfo.topPos,
                containerInfo.width, containerInfo.height, borderInfo.cornerWidth, borderInfo.cornerHeight,
                borderInfo.edgeWidth, borderInfo.edgeHeight, textureInfo.imageWidth, textureInfo.imageHeight,
                textureInfo.leftPos, textureInfo.topPos, textureInfo.imageWidth, textureInfo.imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (flexedWidgets.isEmpty()) {
            flex();
        }
        renderBackground(graphics);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

    public static class ContainerInfo {
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

    public static class TextureInfo {
        protected int imageWidth;
        protected int imageHeight;
        protected int leftPos;
        protected int topPos;
        protected ResourceLocation texture;

        protected TextureInfo(ResourceLocation texture, int imageWidth, int imageHeight, int leftPos, int topPos) {
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            this.leftPos = leftPos;
            this.topPos = topPos;
            this.texture = texture;
        }
    }

    public static class BorderInfo {
        protected int cornerWidth;
        protected int cornerHeight;
        protected int edgeWidth;
        protected int edgeHeight;
        protected BorderInfo(int cornerWidth, int cornerHeight, int edgeWidth, int edgeHeight) {
            this.cornerWidth = cornerWidth;
            this.cornerHeight = cornerHeight;
            this.edgeWidth = edgeWidth;
            this.edgeHeight = edgeHeight;
        }
    }

    private record RelativeWidget(AbstractWidget widget, int relativeX, int relativeY) {
    }
}
