package ninja.crinkle.mod.client.ui.widgets.layouts;

import net.minecraft.client.gui.components.AbstractWidget;
import ninja.crinkle.mod.util.WidgetUtil;

public record VerticalLayout(Alignment alignment, int spacing) implements LayoutManager {

    @Override
    public void arrange(Container container) {
        int yOffset;
        int totalHeight = container.getTotalHeight();

        yOffset = switch (alignment) {
            case CENTER -> WidgetUtil.yOf(container) + (WidgetUtil.heightOf(container) - totalHeight) / 2;
            case BOTTOM -> WidgetUtil.yOf(container) + WidgetUtil.heightOf(container) - totalHeight;
            default -> WidgetUtil.yOf(container);
        };

        for (AbstractWidget child : container.getChildren()) {
            child.setY(yOffset);
            yOffset += WidgetUtil.heightOf(child) + spacing;
        }
    }
}
