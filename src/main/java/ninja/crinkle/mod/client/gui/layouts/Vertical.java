package ninja.crinkle.mod.client.gui.layouts;

import com.mojang.logging.LogUtils;
import ninja.crinkle.mod.client.gui.properties.Box;
import ninja.crinkle.mod.client.gui.properties.ImmutablePoint;
import ninja.crinkle.mod.client.gui.properties.Position;
import ninja.crinkle.mod.client.gui.widgets.AbstractContainer;
import ninja.crinkle.mod.client.gui.widgets.AbstractWidget;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.List;

public class Vertical extends AbstractLayout {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected Vertical(AbstractBuilder<?> builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void arrange(AbstractContainer container) {
        int height = container.layout().boxFor(container).contentBox().size().height();
        int totalHeight = totalInnerHeight(container);

        assert totalHeight <= height : "Total height exceeds container height";
        assert List.of(Layout.Alignment.TOP, Layout.Alignment.CENTER, Layout.Alignment.BOTTOM)
                .contains(alignment()) : "Invalid alignment";

        double yOffset = calculateOffset(height, totalHeight);
        List<AbstractWidget> widgets = container.children().stream()
                .sorted(Comparator.comparingInt(AbstractWidget::zIndexOf).reversed()).toList();

        for (AbstractWidget child : widgets) {
            child.resetPosition();
            Position childPosition = child.position();
            if (childPosition.absolute()) {
                //LOGGER.debug("Skipping absolute positioned child: {}, position: {}", child.name(), childPosition);
                continue;
            }
            Position newPosition = childPosition.offsetBy(0, yOffset);
            child.position(newPosition);
            //LOGGER.debug("[{}] Arranging child: {} from {} to {} (screen position: {})", alignment(), child.name(), childPosition, newPosition, child.renderedPosition());
            yOffset += child.layout().size().height() + spacing();
        }
    }

    @Override
    public int totalInnerHeight(AbstractContainer abstractContainer) {
        return abstractContainer.children().stream().mapToInt(AbstractWidget::totalHeight).sum() +
                (abstractContainer.children().size() - 1) * spacing();
    }

    @Override
    public int totalInnerWidth(AbstractContainer abstractContainer) {
        return abstractContainer.children().stream().mapToInt(AbstractWidget::totalWidth)
                .max().orElse(0);
    }

    public static class Builder extends AbstractBuilder<Builder> {
        public Builder() {
            super();
        }

        @Override
        public Vertical build() {
            return new Vertical(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
