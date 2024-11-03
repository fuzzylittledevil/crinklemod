package ninja.crinkle.mod.client.gui.events;

public abstract class InputEvent implements Event {
    private final EventType type;
    private boolean cancelled;
    private boolean consumed;
    private boolean dispatched;
    private boolean propagate = true;

    public InputEvent(EventType type) {
        this.type = type;
    }

    public boolean cancelled() {
        return cancelled;
    }

    public void cancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean consumed() {
        return consumed;
    }

    public void consumed(boolean consumed) {
        this.consumed = consumed;
    }

    @Override
    public boolean propagate() {
        return !cancelled && !consumed && propagate;
    }

    @Override
    public EventType getType() {
        return type;
    }

    @Override
    public void propagate(boolean propagate) {
        this.propagate = propagate;
    }

    @Override
    public boolean dispatched() {
        return dispatched;
    }

    @Override
    public void dispatched(boolean dispatched) {
        this.dispatched = dispatched;
    }

    @Override
    public String toString() {
        return "InputEvent{" +
                "type=" + type +
                ", cancelled=" + cancelled +
                ", consumed=" + consumed +
                ", dispatched=" + dispatched +
                ", propagate=" + propagate +
                '}';
    }
}
