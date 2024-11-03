package ninja.crinkle.mod.client.gui.events;

public class FocusEvent extends InputEvent {
    private final boolean focused;

    public FocusEvent(boolean focused) {
        super(EventType.Focus);
        this.focused = focused;
    }

    public boolean focused() {
        return focused;
    }

    @Override
    public String toString() {
        return "FocusEvent{" +
                "focused=" + focused +
                '}';
    }
}
