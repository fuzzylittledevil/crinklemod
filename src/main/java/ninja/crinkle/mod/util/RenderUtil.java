package ninja.crinkle.mod.util;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.awt.image.BufferedImage;

public class RenderUtil {
    public static void drawGradient(GuiGraphics graphics, int x, int y, int width, int height, int colorFrom,
                                    int colorTo, int fillColor, double value, double max) {
        double percentage = max == 0 ? 0 : (value / max);
        int offset = (int) (width * percentage);

        graphics.fillGradient(x, y, x + offset, y + height, colorFrom, colorTo);
        graphics.fill(x + offset, y, x + width, y + height, fillColor);
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        java.awt.Image tmp = img.getScaledInstance(newW, newH, java.awt.Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        java.awt.Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }
}
