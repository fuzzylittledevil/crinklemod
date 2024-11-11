package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.properties.Scope;
import ninja.crinkle.mod.client.gui.widgets.AbstractWidget;

public class DragStoppedEvent extends DragEvent {
    private final AbstractWidget widget;
    public DragStoppedEvent(Scope scope, EventSource source, double x, double y, int button, double dragX, double dragY,
                            AbstractWidget widget) {
        super(Type.DragStopped, scope, source, x, y, button, dragX, dragY, null);
        this.widget = widget;
    }

    @Override
    public String toString() {
        return "DragStoppedEvent{" +
                "widget=" + widget +
                ", " + super.toString() +
                '}';
    }

    public AbstractWidget widget() {
        return widget;
    }
}
