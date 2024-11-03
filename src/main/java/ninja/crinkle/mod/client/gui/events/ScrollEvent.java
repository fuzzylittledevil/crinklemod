package ninja.crinkle.mod.client.gui.events;

public class ScrollEvent extends MouseEvent {
    private final double delta;

    public ScrollEvent(double x, double y, double delta) {
        super(EventType.Scroll, x, y, -1);
        this.delta = delta;
    }

    public double delta() {
        return delta;
    }

    @Override
    public String toString() {
        return "ScrollEvent{" +
                "x=" + x() +
                ", y=" + y() +
                ", delta=" + delta +
                '}';
    }
}
