package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.properties.Scope;

public abstract class InputEvent extends AbstractEvent {
    public InputEvent(Type type, Scope scope, EventSource source) {
        super(type, scope, source);
    }
}
