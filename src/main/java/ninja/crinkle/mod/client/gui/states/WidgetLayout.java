package ninja.crinkle.mod.client.gui.states;

import ninja.crinkle.mod.client.gui.properties.*;
import ninja.crinkle.mod.client.gui.widgets.AbstractWidget;

import java.util.Objects;

public final class WidgetLayout {
    private AbstractWidget widget;
    private final Border border;
    private final Margin margin;
    private final Padding padding;
    private final Position position;
    private final Size size;

    public WidgetLayout() {
        this(Position.relative(0, 0), Size.of(20, 20), Margin.ZERO, Border.ZERO, Padding.ZERO);
    }

    public WidgetLayout(Position position, Size size, Margin margin, Border border, Padding padding) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (size == null) {
            throw new IllegalArgumentException("Size cannot be null");
        }
        if (margin == null) {
            throw new IllegalArgumentException("Margin cannot be null");
        }
        if (border == null) {
            throw new IllegalArgumentException("Border cannot be null");
        }
        if (padding == null) {
            throw new IllegalArgumentException("Padding cannot be null");
        }
        this.position = position;
        this.size = size;
        this.margin = margin;
        this.border = border;
        this.padding = padding;
    }

    public WidgetLayout(AbstractWidget.AbstractBuilder<?> builder) {
        this(builder.position(), builder.size(), builder.margin(), builder.border(), builder.padding());
    }

    public void widget(AbstractWidget widget) {
        this.widget = widget;
    }

    public AbstractWidget widget() {
        return widget;
    }

    public Border border() {
        return border;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, size, margin, border, padding);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (WidgetLayout) obj;
        return Objects.equals(this.position, that.position) &&
                Objects.equals(this.size, that.size) &&
                Objects.equals(this.margin, that.margin) &&
                Objects.equals(this.border, that.border) &&
                Objects.equals(this.padding, that.padding);
    }

    @Override
    public String toString() {
        return "WidgetLayout{" +
                "position=" + position +
                ", size=" + size +
                ", margin=" + margin +
                ", border=" + border +
                ", padding=" + padding +
                '}';
    }

    public Margin margin() {
        return margin;
    }

    public Padding padding() {
        return padding;
    }

    public Position position() {
        return position;
    }

    public Size size() {
        return size;
    }

    public BoxBuilder boxFor(AbstractWidget widget) {
        return new BoxBuilder(this, widget);
    }

    public BoxBuilder boxes() {
        assert widget != null : "Widget must be set";
        return new BoxBuilder(this, widget);
    }

    public WidgetLayout withBorder(Border border) {
        return new WidgetLayout(position, size, margin, border, padding);
    }

    public WidgetLayout withMargin(Margin margin) {
        return new WidgetLayout(position, size, margin, border, padding);
    }

    public WidgetLayout withPadding(Padding padding) {
        return new WidgetLayout(position, size, margin, border, padding);
    }

    public WidgetLayout withPosition(Position position) {
        return new WidgetLayout(position, size, margin, border, padding);
    }

    public WidgetLayout withSize(Size size) {
        return new WidgetLayout(position, size, margin, border, padding);
    }

    public static class BoxBuilder {
        final WidgetLayout layout;
        final AbstractWidget widget;
        boolean rendered = false;

        public BoxBuilder(WidgetLayout layout, AbstractWidget widget) {
            this.layout = layout;
            this.widget = layout.widget();
        }

        public BoxBuilder rendered() {
            rendered = true;
            return this;
        }

        public Box box() {
            return new Box(rendered ? widget.renderedPosition() : layout.position, layout.size);
        }

        public Box borderBox() {
            return box().shrink(layout.margin());
        }

        public Box backgroundBox() {
            return borderBox().shrink(layout.border());
        }

        public Box paddingBox() {
            return backgroundBox().shrink(widget.textureBorder());
        }

        public Box contentBox() {
            return paddingBox().shrink(layout.padding());
        }
    }
}
