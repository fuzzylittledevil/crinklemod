package ninja.crinkle.mod.client.gui.properties;

import org.jetbrains.annotations.NotNull;

public class ImmutablePoint implements Point, Comparable<Point> {
    public static final ImmutablePoint ZERO = new ImmutablePoint(0, 0);
    private final double x;
    private final double y;

    public ImmutablePoint(int x, int y) {
        this((double) x, y);
    }

    public ImmutablePoint(double x, double y) {
        this.x = x;
        this.y = y;

//        if (checkForRendering()) {
//            throw new IllegalStateException("ImmutablePoint created during rendering context");
//        }
    }

    public static ImmutablePoint from(Point other) {
        return new ImmutablePoint(other.x(), other.y());
    }

    @Override
    public ImmutablePoint add(Point point) {
        return add(point.x(), point.y());
    }

    @Override
    public ImmutablePoint add(double x, double y) {
        return new ImmutablePoint(x() + x, y() + y);
    }

    @Override
    public int compareTo(@NotNull Point o) {
        return Double.compare(x(), o.x()) == 0 ? Double.compare(y(), o.y()) : Double.compare(x(), o.x());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point other)) return false;
        return x() == other.x() && y() == other.y();
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
        throw new UnsupportedOperationException("Cannot modify immutable point");
    }

    @Override
    public void y(double y) {
        throw new UnsupportedOperationException("Cannot modify immutable point");
    }

    @Override
    public ImmutablePoint add(Size size) {
        return add(size.width(), size.height());
    }

    @Override
    public ImmutablePoint add(int x, int y) {
        return new ImmutablePoint(xInt() + x, yInt() + y);
    }

    @Override
    public ImmutablePoint subtract(double x, double y) {
        return new ImmutablePoint(x() - x, y() - y);
    }

    @Override
    public ImmutablePoint subtract(int x, int y) {
        return new ImmutablePoint(x() - x, y() - y);
    }

    @Override
    public ImmutablePoint copy() {
        return new ImmutablePoint(x, y);
    }

    @Override
    public String toString() {
        return "ImmutablePoint{" + "x=" + x + ", y=" + y + "}";
    }

}
