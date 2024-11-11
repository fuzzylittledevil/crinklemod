package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.listeners.EventListener;
import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.properties.Scope;

import java.util.List;

public class DragEvent extends MouseEvent {
    private final double dragX;
    private final double dragY;
    private final List<EventListener> listeners;

    public DragEvent(Scope scope, EventSource source, double x, double y, int button, double dragX, double dragY,
                     List<EventListener> listeners) {
        super(Type.Drag, scope, source, x, y, button);
        this.dragX = dragX;
        this.dragY = dragY;
        this.listeners = listeners == null ? List.of() : List.copyOf(listeners);
    }

    public DragEvent(Type type, Scope scope, EventSource source, double x, double y, int button, double dragX, double dragY, List<EventListener> listeners) {
        super(type, scope, source, x, y, button);
        this.dragX = dragX;
        this.dragY = dragY;
        this.listeners = listeners;
    }

    public double dragX() {
        return dragX;
    }

    public double dragY() {
        return dragY;
    }

    public List<EventListener> listeners() {
        return listeners;
    }

    @Override
    public String toString() {
        return "DragEvent{" +
                "x=" + x() +
                ", y=" + y() +
                ", button=" + button() +
                ", dragX=" + dragX +
                ", dragY=" + dragY +
                ", " + super.toString() +
                '}';
    }
}
