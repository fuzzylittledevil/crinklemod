package ninja.crinkle.mod.client.gui.events.sources;

import ninja.crinkle.mod.client.gui.properties.Point;

public interface MouseSource extends InputSource {
    boolean hovered();
    boolean clicked();
    boolean mouseOver(Point position);
    boolean dragged();
}
