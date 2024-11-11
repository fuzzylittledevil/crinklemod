package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.events.sources.FocusSource;
import ninja.crinkle.mod.client.gui.properties.Scope;

public class FocusEvent extends AbstractEvent {
    private final boolean focused;
    private final FocusSource focusSource;

    public FocusEvent(Scope scope, EventSource source, boolean focused, FocusSource focusSource) {
        super(Type.Focus, scope, source);
        this.focused = focused;
        this.focusSource = focusSource;
    }

    public FocusEvent(Type type, Scope scope, EventSource source, boolean focused, FocusSource focusSource) {
        super(type, scope, source);
        this.focused = focused;
        this.focusSource = focusSource;
    }

    public boolean focused() {
        return focused;
    }

    public FocusSource focusSource() {
        return focusSource;
    }

    @Override
    public String toString() {
        return "FocusEvent{" +
                "focused=" + focused +
                ", source=" + focusSource +
                ", " + super.toString() +
                '}';
    }
}
