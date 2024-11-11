package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.properties.Scope;
import ninja.crinkle.mod.client.gui.widgets.AbstractWidget;

public class DragStartedEvent extends DragEvent {
    private final AbstractWidget widget;

    @Override
    public String toString() {
        return "DragStartedEvent{" +
                "widget=" + widget +
                ", " + super.toString() +
                '}';
    }

    public DragStartedEvent(Scope scope, EventSource source, double x, double y, int button, double dragX, double dragY,
                            AbstractWidget widget) {
        super(Type.DragStarted, scope, source, x, y, button, dragX, dragY, null);
        this.widget = widget;
    }

    public AbstractWidget widget() {
        return widget;
    }
}
