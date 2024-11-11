package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.listeners.EventListener;
import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.properties.Scope;

import java.util.List;

public class MousePressedEvent extends MouseEvent {
    private final List<EventListener> listeners;

    public MousePressedEvent(Scope scope, EventSource source, double x, double y, int button, List<EventListener> listeners) {
        super(Type.MousePressed, scope, source, x, y, button);
        this.listeners = listeners;
    }

    public List<EventListener> listeners() {
        return listeners;
    }

    @Override
    public String toString() {
        return "MousePressedEvent{" + super.toString() + '}';
    }
}
