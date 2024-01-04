package ninja.crinkle.mod.client.ui.widgets.themes;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.ui.themes.BoxTheme;
import ninja.crinkle.mod.client.ui.themes.Theme;
import org.jetbrains.annotations.NotNull;

public class ThemedBorderBox extends AbstractWidget {
    private final Theme theme;
    private final BoxTheme.Type borderThemeType;
    private boolean inverted = false;

    public ThemedBorderBox(int x, int y, int width, int height, Component message, Theme theme,
                           BoxTheme.Type borderThemeType) {
        super(x, y, width, height, message);
        this.theme = theme;
        this.borderThemeType = borderThemeType;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Get RGBA from int
        Color color = active ? theme.getBackgroundColor() : theme.getInactiveColor();
        pGuiGraphics.setColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        BoxTheme borderTheme = theme.getBorderTheme(borderThemeType);
        BoxTheme.TextureType textureType = BoxTheme.TextureType.NORMAL;
        if (!active)
            textureType = BoxTheme.TextureType.INACTIVE;
        else if (inverted)
            textureType = BoxTheme.TextureType.INVERTED;
        renderTexture(pGuiGraphics, borderTheme.generateTexture(getWidth(), getHeight(), textureType), getX(), getY(), 0, 0,
                0, getWidth(), getHeight(), getWidth(), getHeight());
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

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }
}
