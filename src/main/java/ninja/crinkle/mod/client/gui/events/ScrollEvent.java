package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.properties.Scope;

public class ScrollEvent extends MouseEvent {
    private final double delta;

    public ScrollEvent(Scope scope, EventSource source, double x, double y, double delta) {
        super(Type.Scroll, scope, source, x, y, -1);
        this.delta = delta;
    }

    public double delta() {
        return delta;
    }

    @Override
    public String toString() {
        return "ScrollEvent{" +
                "x=" + x() +
                ", y=" + y() +
                ", delta=" + delta +
                ", " + super.toString() +
                '}';
    }
}
