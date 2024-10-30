package ninja.crinkle.mod.client.ui.widgets.themes;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.ui.themes.BoxTheme;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.client.ui.widgets.properties.Border;
import ninja.crinkle.mod.client.ui.widgets.properties.Box;
import ninja.crinkle.mod.client.ui.widgets.properties.Margin;
import ninja.crinkle.mod.client.ui.widgets.properties.Padding;
import org.jetbrains.annotations.NotNull;

public class ThemedBorderBox extends AbstractWidget {
    private final Theme theme;
    private final BoxTheme.Type borderThemeType;
    private boolean debug = false;
    private boolean inverted = false;
    private Margin margin = Margin.ZERO;
    private Padding padding = Padding.ZERO;
    private Border border = Border.ZERO;
    private Box flex = Box.ZERO;

    public ThemedBorderBox(int x, int y, int width, int height, Component message, Theme theme,
                           BoxTheme.Type borderThemeType) {
        super(x, y, width, height, message);
        this.theme = theme;
        this.borderThemeType = borderThemeType;
    }

    @Override
    protected void renderScrollingString(@NotNull GuiGraphics pGuiGraphics, @NotNull Font pFont, int pWidth, int pColor) {
        Box box = getContentBox();
        int i = box.x() + pWidth;
        int j = box.x() + box.width() - pWidth;
        renderScrollingString(pGuiGraphics, pFont, this.getMessage(), i, box.y(), j, box.y() + box.height(), pColor);
    }

    @Override
    protected boolean clicked(double pMouseX, double pMouseY) {
        return isActive() && visible && getBorderBox().contains(pMouseX, pMouseY);
    }

    @Override
    public boolean isMouseOver(double pMouseX, double pMouseY) {
        return isActive() && visible && getBorderBox().contains(pMouseX, pMouseY);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // We can't override isHovered in render() effectively, so we need to check it here
        this.isHovered = getBorderBox().contains(pMouseX, pMouseY);
        Color color = active ? getTheme().getBackgroundColor() : getTheme().getInactiveColor();
        renderBorder(pGuiGraphics);
        pGuiGraphics.setColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        BoxTheme borderTheme = getTheme().getBorderTheme(borderThemeType);
        BoxTheme.TextureType textureType = BoxTheme.TextureType.NORMAL;
        if (!active)
            textureType = BoxTheme.TextureType.INACTIVE;
        else if (inverted)
            textureType = BoxTheme.TextureType.INVERTED;
        Box borderThemeBox = getBorderBox().add(
                getBorder().left(),
                getBorder().top(),
                -(getBorder().right() + getBorder().left()),
                -(getBorder().bottom() + getBorder().top()));
        renderTexture(pGuiGraphics,
                borderTheme.generateTexture(borderThemeBox.width(), borderThemeBox.height(), textureType),
                borderThemeBox.x(), borderThemeBox.y(), 0, 0, 0,
                borderThemeBox.width(), borderThemeBox.height(), borderThemeBox.width(), borderThemeBox.height());
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderContent(pGuiGraphics, pMouseX, pMouseY, pPartialTick, getContentBox());
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        renderDebug(pGuiGraphics);
    }


    protected void renderBorder(GuiGraphics guiGraphics) {
        Box borderBox = getBorderBox();
        getBorder().render(guiGraphics, borderBox.x(), borderBox.y(), borderBox.width(), borderBox.height(), debug);
    }

    protected void renderDebug(GuiGraphics guiGraphics) {
        if (!debug) return;
        Box fullBox = getBox();
        Box borderBox = getBorderBox();
        Box contentBox = getContentBox();
        guiGraphics.fill(fullBox.x(), fullBox.y(), fullBox.x() + fullBox.width(), fullBox.y() + fullBox.height(),
                10, Color.of("#dbff33").withAlpha(0.5f).color());
        guiGraphics.fill(borderBox.x(), borderBox.y(), borderBox.x() + borderBox.width(), borderBox.y() + borderBox.height(),
                10, Color.of("#ffbd33").withAlpha(0.5f).color());
        guiGraphics.fill(contentBox.x(), contentBox.y(), contentBox.x() + contentBox.width(), contentBox.y() + contentBox.height(),
                10, Color.of("#ff5733").withAlpha(0.5f).color());
    }

    /**
     * Renders the content inside the border box
     *
     * <p>
     * This method is called after the border box has been rendered. The position and size of the content area is
     * determined by the margin, padding, and border properties of the border box. Use the provided values for
     * x, y, width, and height to render content inside the border box.
     * <br>
     * The default implementation of this method does nothing.
     * <br>
     * Override this method to render content inside the border box.
     * </p>
     *
     * @param pGuiGraphics The GuiGraphics instance
     * @param pMouseX      The x position of the mouse
     * @param pMouse       The y position of the mouse
     * @param pPartialTick How far the current tick is between the previous and next tick
     * @param pBox         The box representing the content area inside the border box
     */
    protected void renderContent(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouse, float pPartialTick, Box pBox) {
        // Override this method to render content inside the border box
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }

    public Theme getTheme() {
        return theme;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public Margin getMargin() {
        return margin;
    }

    public void setMargin(Margin margin) {
        this.margin = margin;
    }

    public Padding getPadding() {
        return padding;
    }

    public void setPadding(Padding padding) {
        this.padding = padding;
    }

    public Border getBorder() {
        return border != Border.ZERO ? border : theme.getBorderTheme(borderThemeType).border();
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public Box getFlex() {
        return flex;
    }

    public void setFlex(Box flex) {
        this.flex = flex;
    }

    public int getFlexedX() {
        return getFlex() != Box.ZERO ? flex.x() : getX();
    }

    public int getFlexedY() {
        return getFlex() != Box.ZERO ? getFlex().y() : getY();
    }

    public int getEffectiveX() {
        return getFlexedX() + getMargin().left();
    }

    public int getEffectiveY() {
        return getFlexedY() + getMargin().top();
    }

    public Box getContentBox() {
        BoxTheme borderTheme = getTheme().getBorderTheme(borderThemeType);
        return new Box(getEffectiveX() + getBorder().left() + getPadding().left() + borderTheme.edgeSize(),
                getEffectiveY() + getBorder().top() + getPadding().top() + borderTheme.edgeSize(),
                getWidth() - getMargin().left() - getMargin().right() - getBorder().left()
                        - getBorder().right() - getPadding().left() - getPadding().right() - borderTheme.edgeSize() * 2,
                getHeight() - getMargin().top() - getMargin().bottom() - getBorder().top()
                        - getBorder().bottom() - getPadding().top() - getPadding().bottom() - borderTheme.edgeSize() * 2);
    }

    public Box getBorderBox() {
        return new Box(getEffectiveX(), getEffectiveY(),
                getWidth() - getMargin().left() - getMargin().right(),
                getHeight() - getMargin().top() - getMargin().bottom());
    }

    public Box getBox() {
        return new Box(getFlexedX(), getFlexedY(), getWidth(), getHeight());
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
