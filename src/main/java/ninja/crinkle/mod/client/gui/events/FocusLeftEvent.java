package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.events.sources.FocusSource;
import ninja.crinkle.mod.client.gui.properties.Scope;

public class FocusLeftEvent extends FocusEvent {
    public FocusLeftEvent(Scope scope, EventSource source, boolean focused, FocusSource focusSource) {
        super(Type.FocusLeft, scope, source, focused, focusSource);
    }

    @Override
    public String toString() {
        return "FocusLeftEvent{" + super.toString() + '}';
    }
}
