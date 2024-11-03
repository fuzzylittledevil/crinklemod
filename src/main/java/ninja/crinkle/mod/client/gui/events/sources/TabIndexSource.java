package ninja.crinkle.mod.client.gui.events.sources;

public interface TabIndexSource extends FocusSource {
    int tabIndex();
    void tabIndex(int tabIndex);
}
