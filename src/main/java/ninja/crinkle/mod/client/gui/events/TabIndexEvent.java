package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.events.sources.FocusSource;
import ninja.crinkle.mod.client.gui.properties.Scope;

public class TabIndexEvent extends FocusEvent {
    private final int oldIndex;
    private final int newIndex;

    public TabIndexEvent(Scope scope, EventSource source, boolean focused, int oldIndex, int newIndex, FocusSource focusSource) {
        super(Type.TabOrder, scope, source, focused, focusSource);
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
    }

    public int oldIndex() {
        return oldIndex;
    }

    @Override
    public String toString() {
        return "TabIndexEvent{" +
                "oldIndex=" + oldIndex +
                ", newIndex=" + newIndex +
                ", " + super.toString() +
                '}';
    }

    public int newIndex() {
        return newIndex;
    }
}
