package ninja.crinkle.mod.client.gui.events;

public class DragEvent extends MouseEvent {
    private final double dragX;
    private final double dragY;
    private final boolean dragged;

    public DragEvent(double x, double y, int button, double dragX, double dragY, boolean dragged) {
        super(EventType.Drag, x, y, button);
        this.dragX = dragX;
        this.dragY = dragY;
        this.dragged = dragged;
    }

    public double dragX() {
        return dragX;
    }

    public double dragY() {
        return dragY;
    }

    public boolean dragged() {
        return dragged;
    }

    @Override
    public String toString() {
        return "DragEvent{" +
                "x=" + x() +
                ", y=" + y() +
                ", button=" + button() +
                ", dragX=" + dragX +
                ", dragY=" + dragY +
                ", dragged=" + dragged +
                '}';
    }
}
