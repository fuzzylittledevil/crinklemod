package ninja.crinkle.mod.client.gui.events;

public class TabIndexEvent extends FocusEvent {
    private final int oldIndex;
    private final int newIndex;

    public TabIndexEvent(boolean focused, int oldIndex, int newIndex) {
        super(focused);
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
    }

    public int oldIndex() {
        return oldIndex;
    }

    public int newIndex() {
        return newIndex;
    }
}
