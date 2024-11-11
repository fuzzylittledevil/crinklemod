package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.listeners.EventListener;
import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.properties.Scope;

import java.util.List;

public class ClickEvent extends MouseEvent {
    private final boolean released;
    private final List<EventListener> listeners;

    public ClickEvent(Scope scope, EventSource source, double x, double y, int button, boolean released, List<EventListener> listeners) {
        super(Type.Click, scope, source, x, y, button);
        this.released = released;
        this.listeners = listeners;
    }

    public List<EventListener> listeners() {
        return listeners;
    }

    public boolean released() {
        return released;
    }

    public boolean pressed() {
        return !released;
    }

    @Override
    public String toString() {
        return "ClickEvent{" +
                "x=" + x() +
                ", y=" + y() +
                ", button=" + button() +
                ", released=" + released +
                ", " + super.toString() +
                '}';
    }
}
