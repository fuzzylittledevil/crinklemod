package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.listeners.EventListener;
import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.properties.Scope;

import java.util.List;

public class MoveEvent extends MouseEvent {
    private final List<EventListener> listeners;
    public MoveEvent(Scope scope, EventSource source, double x, double y, List<EventListener> listeners) {
        super(Type.Move, scope, source, x, y, -1);
        this.listeners = listeners;
    }

    public List<EventListener> listeners() {
        return listeners;
    }

    @Override
    public String toString() {
        return "MoveEvent{" +
                "x=" + x() +
                ", y=" + y() +
                ", " + super.toString() +
                '}';
    }
}
