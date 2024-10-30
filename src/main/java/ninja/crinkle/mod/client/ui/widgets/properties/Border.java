package ninja.crinkle.mod.client.ui.widgets.properties;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import ninja.crinkle.mod.client.color.Color;
import org.slf4j.Logger;

public record Border(int top, int bottom, int left, int right, Color color) {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Border ZERO = new Border(0, 0, 0, 0, Color.BLACK);

    public Border {
        if (top < 0 || bottom < 0 || left < 0 || right < 0) {
            throw new IllegalArgumentException("Border values cannot be negative");
        }
    }

    public Border(int all, Color color) {
        this(all, all, all, all, color);
    }

    public Border(int vertical, int horizontal, Color color) {
        this(vertical, vertical, horizontal, horizontal, color);
    }

    public void render(GuiGraphics guiGraphics, int x, int y, int width, int height, boolean debug) {
        // Log all values because shit is broken
        int minX = x;
        int minY = y;
        int maxX = x + width;
        int maxY = y + top;
        if (debug)
            LOGGER.debug("TOP: minX: {}, minY: {}, maxX: {}, maxY: {}", minX, minY, maxX, maxY);
        guiGraphics.fill(minX, minY, maxX, maxY, color.color());
        minY = y + height - bottom;
        maxX = x + width;
        maxY = y + height;
        if (debug)
            LOGGER.debug("BOTTOM: minX: {}, minY: {}, maxX: {}, maxY: {}", minX, minY, maxX, maxY);
        guiGraphics.fill(minX, minY, maxX, maxY, color.color());
        minY = y + top;
        maxX = x + left;
        maxY = y + height - bottom;
        if (debug)
            LOGGER.debug("LEFT: minX: {}, minY: {}, maxX: {}, maxY: {}", minX, minY, maxX, maxY);
        guiGraphics.fill(minX, minY, maxX, maxY, color.color());
        minX = x + width - right;
        minY = y + top;
        maxX = x + width;
        maxY = y + height - bottom;
        if (debug)
            LOGGER.debug("RIGHT: minX: {}, minY: {}, maxX: {}, maxY: {}", minX, minY, maxX, maxY);
        guiGraphics.fill(minX, minY, maxX, maxY, color.color());
    }
}
