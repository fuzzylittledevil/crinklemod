package ninja.crinkle.mod.client.gui.events;

public class MoveEvent extends MouseEvent {
    public MoveEvent(double x, double y) {
        super(EventType.Move, x, y, -1);
    }

    @Override
    public String toString() {
        return "MoveEvent{" +
                "x=" + x() +
                ", y=" + y() +
                '}';
    }
}
