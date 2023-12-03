package ninja.crinkle.mod.client.renderers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.icons.Icons;

public class IconRenderer {
    @SuppressWarnings("resource")
    public static void renderIcon(GuiGraphics graphics, Icons icon, int x, int y) {
        TextureAtlasSprite sprite = icon.getSprite();
        SpriteContents contents = sprite.contents();
        graphics.blit(x, y, 0, contents.width(), contents.height(), sprite);
    }

    public static void renderIcon(GuiGraphics graphics, Icons icon, int x, int y, Color color) {
        // Get RGBA from int
        graphics.setColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(),
                (float) color.getAlpha());
        renderIcon(graphics, icon, x, y);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
