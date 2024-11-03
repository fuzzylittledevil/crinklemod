package ninja.crinkle.mod.util;

import net.minecraft.client.gui.components.AbstractWidget;
import ninja.crinkle.mod.client.ui.widgets.layouts.Container;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedBorderBox;

public class WidgetUtil {
    public static int heightOf(AbstractWidget widget) {
        if (widget instanceof ThemedBorderBox themedBorderBox) {
            return themedBorderBox.getBox().height();
        } else {
            return widget.getHeight();
        }
    }

    public static int widthOf(AbstractWidget widget) {
        if (widget instanceof ThemedBorderBox themedBorderBox) {
            return themedBorderBox.getBox().width();
        } else {
            return widget.getWidth();
        }
    }

    public static int xOf(AbstractWidget widget) {
        if (widget instanceof ThemedBorderBox themedBorderBox) {
            return themedBorderBox.getBox().x();
        } else {
            return widget.getX();
        }
    }

    public static int yOf(AbstractWidget widget) {
        if (widget instanceof ThemedBorderBox themedBorderBox) {
            return themedBorderBox.getBox().y();
        } else {
            return widget.getY();
        }
    }
}
