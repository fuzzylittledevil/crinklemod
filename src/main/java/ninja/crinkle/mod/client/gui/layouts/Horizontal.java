package ninja.crinkle.mod.client.gui.layouts;

import com.mojang.logging.LogUtils;
import ninja.crinkle.mod.client.gui.properties.Position;
import ninja.crinkle.mod.client.gui.widgets.AbstractContainer;
import ninja.crinkle.mod.client.gui.widgets.AbstractWidget;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.List;

public class Horizontal extends AbstractLayout {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected Horizontal(AbstractBuilder<?> builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void arrange(AbstractContainer container) {
        int width = container.layout().boxes().contentBox().size().width();
        int totalWidth = totalInnerWidth(container);

        assert totalWidth <= width : "Total width exceeds container width";
        assert List.of(Layout.Alignment.LEFT, Layout.Alignment.CENTER, Layout.Alignment.RIGHT)
                .contains(alignment()) : "Invalid alignment";

        int xOffset = calculateOffset(width, totalWidth);
        List<AbstractWidget> widgets = container.children().stream()
                .sorted(Comparator.comparingInt(AbstractWidget::zIndexOf).reversed()).toList();

        for (Widget child : widgets) {
            child.resetPosition();
            Position childPosition = child.position();
            if (childPosition.absolute()) {
                LOGGER.debug("Skipping absolute positioned child: {}, position: {}", child.name(), childPosition);
                continue;
            }
            Position newPosition = childPosition.offsetBy(xOffset, 0);
            child.position(newPosition);
            LOGGER.debug("[{}] Arranging child: {} from {} to {} (screen position: {})", alignment(), child.name(), childPosition, newPosition, child.renderedPosition());
            xOffset += child.layout().size().width() + spacing();
        }
    }

    @Override
    public int totalInnerHeight(AbstractContainer abstractContainer) {
        return abstractContainer.children().stream().mapToInt(w -> w.layout().size().height())
                .max().orElse(0);
    }

    @Override
    public int totalInnerWidth(AbstractContainer abstractContainer) {
        return abstractContainer.children().stream().mapToInt(w -> w.layout().size().width()).sum() +
                (abstractContainer.children().size() - 1) * spacing();

    }

    public static class Builder extends AbstractBuilder<Builder> {
        public Builder() {
            super();
        }

        @Override
        public Horizontal build() {
            return new Horizontal(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
