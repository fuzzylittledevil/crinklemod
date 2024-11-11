package ninja.crinkle.mod.client.gui.states;

import ninja.crinkle.mod.client.gui.widgets.AbstractWidget;

public record WidgetBehavior(boolean draggable, boolean dragged, boolean pressed, boolean focused, boolean hovered,
                             boolean active) {
    public WidgetBehavior {
        if (dragged && !draggable) {
            throw new IllegalArgumentException("Cannot be dragged if not draggable");
        }
    }

    public WidgetBehavior(AbstractWidget.AbstractBuilder<?> builder) {
        this(builder.draggable(), false, false, false, false, builder.active());
    }

    public WidgetBehavior() {
        this(false, false, false, false, false, true);
    }

    public WidgetBehavior withDraggable(boolean draggable) {
        return new WidgetBehavior(draggable, dragged, pressed, focused, hovered, active);
    }

    public WidgetBehavior withDragged(boolean dragged) {
        return new WidgetBehavior(draggable, dragged, pressed, focused, hovered, active);
    }

    public WidgetBehavior withPressed(boolean pressed) {
        return new WidgetBehavior(draggable, dragged, pressed, focused, hovered, active);
    }

    public WidgetBehavior withFocused(boolean focused) {
        return new WidgetBehavior(draggable, dragged, pressed, focused, hovered, active);
    }

    public WidgetBehavior withHovered(boolean hovered) {
        return new WidgetBehavior(draggable, dragged, pressed, focused, hovered, active);
    }

    public WidgetBehavior withActive(boolean active) {
        return new WidgetBehavior(draggable, dragged, pressed, focused, hovered, active);
    }

    @Override
    public String toString() {
        return "WidgetBehavior{" +
                "draggable=" + draggable +
                ", dragged=" + dragged +
                ", pressed=" + pressed +
                ", focused=" + focused +
                ", hovered=" + hovered +
                ", active=" + active +
                '}';
    }
}
