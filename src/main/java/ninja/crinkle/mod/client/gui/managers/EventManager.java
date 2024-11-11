package ninja.crinkle.mod.client.gui.managers;

import com.mojang.logging.LogUtils;
import ninja.crinkle.mod.client.gui.events.Event;
import ninja.crinkle.mod.client.gui.events.listeners.EventListener;
import ninja.crinkle.mod.client.gui.properties.Scope;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Predicate;

public class EventManager {
    private static final EventManager GLOBAL = new EventManager(Scope.Global);
    private static final Logger LOGGER = LogUtils.getLogger();
    private final List<EventListener> listeners = new ArrayList<>();
    private final Scope scope;

    public EventManager(Scope scope) {
        this.scope = scope;
    }

    public static EventManager createLocal() {
        return new EventManager(Scope.Local);
    }

    public static EventManager createScreen() {
        return new EventManager(Scope.Screen);
    }

    public static EventManager global() {
        return GLOBAL;
    }

    public void addListener(EventListener listener) {
        assert listener != null;

        if (listeners.contains(listener)) {
            LOGGER.warn("Attempted to add duplicate listener: {}", listener);
            return;
        }
        listeners.add(listener);
        sortListeners();
    }

    public void sortListeners() {
        listeners.sort(Comparator.comparingInt(EventListener::priority));
    }

    private List<EventListener> listeners() {
        return listeners.stream().toList();
    }

    public void clear() {
        listeners().forEach(l -> EventManager.global().removeListener(l));
        listeners.clear();
    }

    public void dispatchEvent(Event event) {
        dispatchEvent(event, listener -> true);
    }

    public void dispatchEvent(Event event, Predicate<EventListener> predicate) {
        if (event.dispatched() || !event.propagate() || event.scope() != scope) {
            LOGGER.debug("Invalid event: {}", event);
            return;
        }
        event.dispatched(true);
        listeners().stream().filter(predicate).forEach(listener -> {
            if (event.propagate()) {
                listener.onEvent(event);
            }
        });
    }

    public List<EventListener> listeners(Predicate<EventListener> predicate) {
        return listeners.stream().filter(predicate).toList();
    }

    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }

    public Scope scope() {
        return scope;
    }
}
