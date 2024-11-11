package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.properties.Scope;

public class HoverEvent extends MouseEvent {
    private final boolean hovered;
    public HoverEvent(Scope scope, EventSource source, double x, double y, boolean hovered) {
        super(Type.Hover, scope, source, x, y, -1);
        this.hovered = hovered;
    }

    public boolean hovered() {
        return hovered;
    }

    @Override
    public String toString() {
        return "HoverEvent{" +
                "x=" + x() +
                ", y=" + y() +
                ", hovered=" + hovered +
                ", " + super.toString() +
                '}';
    }
}
