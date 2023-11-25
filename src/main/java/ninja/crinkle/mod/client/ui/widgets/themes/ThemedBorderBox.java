package ninja.crinkle.mod.client.ui.widgets.themes;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.ui.themes.BorderThemeData;
import ninja.crinkle.mod.client.ui.themes.BorderThemeSize;
import ninja.crinkle.mod.client.ui.themes.Theme;
import org.jetbrains.annotations.NotNull;

public class ThemedBorderBox extends AbstractWidget {
    private final Theme theme;
    private final BorderThemeSize borderThemeSize;
    private boolean inverted = false;
    public ThemedBorderBox(int x, int y, int width, int height, Component message, Theme theme,
                           BorderThemeSize borderThemeSize) {
        super(x, y, width, height, message);
        this.theme = theme;
        this.borderThemeSize = borderThemeSize;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Get RGBA from int
        Color color = theme.getBackgroundColor();
        pGuiGraphics.setColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        BorderThemeData borderTheme = theme.getBorderTheme(borderThemeSize);
        ResourceLocation texture = inverted ? theme.getInvertedTexture(borderTheme) : borderTheme.texture();
        pGuiGraphics.blitNineSlicedSized(texture, getX(), getY(), getWidth(), getHeight(), borderTheme.cornerWidth(),
                borderTheme.cornerHeight(), borderTheme.edgeWidth(), borderTheme.edgeHeight(), borderTheme.uWidth(), borderTheme.vHeight(),
                borderTheme.uOffset(), borderTheme.vOffset(), borderTheme.textureWidth(), borderTheme.textureHeight());
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {

    }

    public Theme getTheme() {
        return theme;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }
}
