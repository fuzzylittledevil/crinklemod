package ninja.crinkle.mod.client.gui.events.sources;

import ninja.crinkle.mod.client.gui.properties.Point;

public interface MouseSource extends InputSource {
    default boolean hovered() { return false; }
    default boolean pressed() { return false; }
    default boolean mouseOver(Point position) { return false; }
    default boolean dragged() { return false; }
}
