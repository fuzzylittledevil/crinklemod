package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.events.sources.FocusSource;
import ninja.crinkle.mod.client.gui.properties.Scope;

public class FocusEnteredEvent extends FocusEvent {
    public FocusEnteredEvent(Scope scope, EventSource source, boolean focused, FocusSource focusSource) {
        super(Type.FocusEntered, scope, source, focused, focusSource);
    }

    @Override
    public String toString() {
        return "FocusEnteredEvent{" + super.toString() + '}';
    }
}
