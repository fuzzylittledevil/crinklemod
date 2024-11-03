package ninja.crinkle.mod.client.gui.layouts;

import ninja.crinkle.mod.client.gui.properties.Bounds;
import ninja.crinkle.mod.client.gui.properties.Box;
import ninja.crinkle.mod.client.gui.properties.Point;

public interface LayoutWidget {
    Box box();
    Point position();
    void position(Point position);
    Bounds bounds();
    void bounds(Bounds bounds);
    PositionType positionType();
    void positionType(PositionType positionType);
}
