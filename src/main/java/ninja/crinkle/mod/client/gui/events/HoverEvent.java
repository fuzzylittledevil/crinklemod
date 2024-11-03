package ninja.crinkle.mod.client.gui.events;

public class HoverEvent extends MouseEvent {
    private final boolean hovered;
    public HoverEvent(double x, double y, boolean hovered) {
        super(EventType.Hover, x, y, -1);
        this.hovered = hovered;
    }

    public boolean hovered() {
        return hovered;
    }

    @Override
    public String toString() {
        return "HoverEvent{" +
                "x=" + x() +
                ", y=" + y() +
                ", hovered=" + hovered +
                '}';
    }
}
