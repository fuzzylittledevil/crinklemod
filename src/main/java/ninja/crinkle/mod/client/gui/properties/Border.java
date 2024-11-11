package ninja.crinkle.mod.client.gui.properties;

import net.minecraft.client.gui.GuiGraphics;
import ninja.crinkle.mod.client.color.Color;

public record Border(int top, int right, int bottom, int left, Color color) implements BoxProperty {
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
        this(vertical, horizontal, vertical, horizontal, color);
    }

    public static Border all(int all) {
        return new Border(all, Color.BLACK);
    }

    public Border add(int top, int right, int bottom, int left) {
        return new Border(this.top() + top, this.right() + right,
                this.bottom() + bottom, this.left() + left, color());
    }

    public Border add(Border border) {
        return add(border.top(), border.right(), border.bottom(), border.left());
    }

    public void render(GuiGraphics guiGraphics, Box box, int zIndex) {
        // top
        Point min = box.topLeft();
        Point max = box.topRight().add(0, top());
        guiGraphics.fill((int) min.x(), (int) min.y(), (int) max.x(), (int) max.y(), zIndex, color().color());
        
        // bottom
        min = box.bottomLeft();
        max = box.bottomRight().subtract(0, bottom());
        guiGraphics.fill((int) min.x(), (int) min.y(), (int) max.x(), (int) max.y(), zIndex, color().color());

        // left
        min = box.topLeft();
        max = box.bottomLeft().add(left(), 0);
        guiGraphics.fill((int) min.x(), (int) min.y(), (int) max.x(), (int) max.y(), zIndex, color().color());

        // right
        min = box.topRight();
        max = box.bottomRight().subtract(right(), 0);
        guiGraphics.fill((int) min.x(), (int) min.y(), (int) max.x(), (int) max.y(), zIndex, color().color());
    }
}
