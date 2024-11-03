package ninja.crinkle.mod.client.gui.events;

public interface Event {
    EventType getType();
    boolean dispatched();
    void dispatched(boolean dispatched);
    boolean cancelled();
    void cancelled(boolean cancelled);
    boolean consumed();
    void consumed(boolean consumed);
    boolean propagate();
    void propagate(boolean propagate);
}
