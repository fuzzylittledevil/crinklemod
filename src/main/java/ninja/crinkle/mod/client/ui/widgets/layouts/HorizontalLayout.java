package ninja.crinkle.mod.client.ui.widgets.layouts;

import net.minecraft.client.gui.components.AbstractWidget;
import ninja.crinkle.mod.util.WidgetUtil;

public record HorizontalLayout(Alignment alignment, int spacing) implements LayoutManager {

    @Override
    public void arrange(Container container) {
        int xOffset;
        int totalWidth = container.getChildren().stream()
                .mapToInt(WidgetUtil::widthOf)
                .sum() + (container.getChildren().size() - 1) * spacing;

        xOffset = switch (alignment) {
            case CENTER -> WidgetUtil.xOf(container) + (WidgetUtil.widthOf(container) - totalWidth) / 2;
            case RIGHT -> WidgetUtil.xOf(container) + WidgetUtil.widthOf(container) - totalWidth;
            default -> WidgetUtil.xOf(container);
        };

        for (AbstractWidget child : container.getChildren()) {
            child.setX(xOffset);
            xOffset += WidgetUtil.widthOf(child) + spacing;
        }
    }
}
