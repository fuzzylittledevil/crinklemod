package ninja.crinkle.mod.util;

import net.minecraft.client.gui.GuiGraphics;
import ninja.crinkle.mod.client.color.Color;

public class RenderUtil {
    public static void drawGradient(GuiGraphics graphics, int x, int y, int width, int height, Color colorFrom,
                                    Color colorTo, Color fillColor, double value, double max) {
        double percentage = max == 0 ? 0 : (value / max);
        int offset = (int) (width * percentage);
        graphics.fillGradient(x, y, x + offset, y + height, colorFrom.color(), colorTo.color());
        graphics.fill(x + offset, y, x + width, y + height, fillColor.color());
    }
}
