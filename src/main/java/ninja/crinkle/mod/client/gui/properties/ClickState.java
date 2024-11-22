package ninja.crinkle.mod.client.gui.properties;

import ninja.crinkle.mod.client.gui.events.listeners.EventListener;

import java.util.List;

public record ClickState(Point position, int button, List<EventListener> listeners) {
    public ClickState {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

    }

    public ClickState() {
        this(ImmutablePoint.ZERO, 0, List.of());
    }
}
