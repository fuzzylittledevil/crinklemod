package ninja.crinkle.mod.client.gui.properties;

import ninja.crinkle.mod.client.gui.renderers.ThemeGraphics;
import ninja.crinkle.mod.config.ClientConfig;
import org.jetbrains.annotations.NotNull;

public interface Point {
    Point ZERO = new ImmutablePoint(0, 0);

    static Point of(int x, int y) {
        return new ImmutablePoint(x, y);
    }

    Point add(Point point);

    Point add(Size size);

    Point add(int x, int y);

    Point add(double x, double y);

    Point copy();

    Point subtract(double x, double y);

    Point subtract(int x, int y);

    double x();

    void x(double x);

    default float xFloat() {
        return (float) x();
    }

    default int xInt() {
        return (int) x();
    }

    double y();

    void y(double y);

    default float yFloat() {
        return (float) y();
    }

    default int yInt() {
        return (int) y();
    }
//
//    default boolean checkForRendering() {
//        if (!ClientConfig.debug()) return true;
//        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//        for (StackTraceElement element : stackTrace) {
//            // Basic check to see if the point is being used in a rendering context
//            if (element.getClassName().equals(ThemeGraphics.class.getName())) {
//                return true;
//            }
//        }
//        return false;
//    }
}
