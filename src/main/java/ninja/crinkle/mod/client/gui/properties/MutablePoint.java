package ninja.crinkle.mod.client.gui.properties;

import ninja.crinkle.mod.client.gui.renderers.ThemeGraphics;
import ninja.crinkle.mod.config.ClientConfig;
import org.jetbrains.annotations.NotNull;

public class MutablePoint implements Point, Comparable<Point> {
    public static final Point ZERO = new MutablePoint(0, 0);
    private double x;
    private double y;

    public MutablePoint(int x, int y) {
        this((double) x, y);
    }

    public MutablePoint(double x, double y) {
        this.x = x;
        this.y = y;

//        if (checkForRendering()) {
//            throw new IllegalStateException("MutablePoint created during rendering context");
//        }
    }

    @Override
    public Point add(Point point) {
        return add(point.x(), point.y());
    }

    @Override
    public int compareTo(@NotNull Point o) {
        return Double.compare(x(), o.x()) == 0 ? Double.compare(y(), o.y()) : Double.compare(x(), o.x());
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public void x(double x) {
        this.x = x;
    }

    @Override
    public void y(double y) {
        this.y = y;
    }


    @Override
    public Point add(Size size) {
        return add(size.width(), size.height());
    }

    @Override
    public Point add(int x, int y) {
        x(x() + x);
        y(y() + y);
        return this;
    }

    @Override
    public Point add(double x, double y) {
        x(x() + x);
        y(y() + y);
        return this;
    }

    @Override
    public Point subtract(double x, double y) {
        x(x() - x);
        y(y() - y);
        return this;
    }

    @Override
    public Point subtract(int x, int y) {
        x(x() - x);
        y(y() - y);
        return this;
    }

    @Override
    public Point copy() {
        return new MutablePoint(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point other)) return false;
        return x() == other.x() && y() == other.y();
    }

    @Override
    public String toString() {
        return "MutablePoint{" + "x=" + x + ", y=" + y + '}';
    }

}
