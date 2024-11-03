package ninja.crinkle.mod.client.gui.properties;

public record Point(double x, double y, int z) {
    public static final Point ZERO = new Point(0, 0, 0);

    public Point(double x, double y) {
        this(x, y, 0);
    }

    public Point add(int x, int y, int z) {
        return new Point(this.x() + x, this.y() + y, this.z() + z);
    }

    public Point add(int x, int y) {
        return new Point(this.x() + x, this.y() + y, this.z());
    }

    public Point add(Point point) {
        return new Point(this.x() + point.x(), this.y() + point.y(), this.z() + point.z());
    }

    public Point add(Bounds bounds) {
        return new Point(this.x() + bounds.width(), this.y() + bounds.height(), this.z());
    }

    public Point add(double x, double y) {
        return new Point(this.x() + x, this.y() + y, this.z());
    }

    public boolean contains(Bounds bounds, Point point) {
        return point.x() >= x && point.x() < x + bounds.width() && point.y() >= y && point.y() < y + bounds.height();
    }

    public Point subtract(int x, int y) {
        return new Point(this.x() - x, this.y() - y, this.z());
    }

    public Point subtract(Point point) {
        return new Point(this.x() - point.x(), this.y() - point.y(), this.z() - point.z());
    }

    public Point subtract(Bounds bounds) {
        return new Point(this.x() - bounds.width(), this.y() - bounds.height(), this.z());
    }

    public Point max(Point point) {
        return new Point(Math.max(x, point.x()), Math.max(y, point.y()), Math.max(z, point.z()));
    }

    public Point min(Point point) {
        return new Point(Math.min(x, point.x()), Math.min(y, point.y()), Math.min(z, point.z()));
    }

    public boolean greaterThan(Point point) {
        return x > point.x() && y > point.y() && z > point.z();
    }

    public boolean lessThan(Point point) {
        return x < point.x() && y < point.y() && z < point.z();
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
