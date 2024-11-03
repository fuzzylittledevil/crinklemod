package ninja.crinkle.mod.client.gui.events;

public class ClickEvent extends MouseEvent {
    private final boolean released;

    public ClickEvent(double x, double y, int button, boolean released) {
        super(EventType.Click, x, y, button);
        this.released = released;
    }

    public boolean released() {
        return released;
    }

    public boolean pressed() {
        return !released;
    }

    @Override
    public String toString() {
        return "ClickEvent{" +
                "x=" + x() +
                ", y=" + y() +
                ", button=" + button() +
                ", released=" + released +
                '}';
    }
}
