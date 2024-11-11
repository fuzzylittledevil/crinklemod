package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.properties.Scope;

public class CharTypedEvent extends KeyEvent {
    private final char codePoint;

    public CharTypedEvent(Scope scope, EventSource source, char codePoint, int modifiers) {
        super(Type.CharTyped, scope, source, modifiers);
        this.codePoint = codePoint;
    }

    public char codePoint() {
        return codePoint;
    }

    @Override
    public String toString() {
        return "CharTypedEvent{" +
                "codePoint=" + codePoint +
                ", modifiers=" + modifiers() +
                '}';
    }
}
