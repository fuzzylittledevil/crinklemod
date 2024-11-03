package ninja.crinkle.mod.client.gui.events.sources;

import ninja.crinkle.mod.client.gui.events.listeners.EventListener;

public interface EventSource {
    default void addListener(EventListener listener) {}
    default void removeListener(EventListener listener) {}
}
