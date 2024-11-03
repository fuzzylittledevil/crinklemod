package ninja.crinkle.mod.client.gui.events;

public class CharTypedEvent extends KeyEvent {
    private final char codePoint;

    public CharTypedEvent(char codePoint, int modifiers) {
        super(EventType.CharTyped, modifiers);
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
