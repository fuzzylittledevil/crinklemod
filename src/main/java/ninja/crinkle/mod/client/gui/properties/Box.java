package ninja.crinkle.mod.client.gui.properties;

public record Box(Point start, Bounds bounds) {
    public Box {
        if (bounds.width() < 0 || bounds.height() < 0) {
            throw new IllegalArgumentException("Box width and height values cannot be negative");
        }
    }

    public Box add(int x, int y, int width, int height) {
        return new Box(start.add(x, y), bounds.add(width, height));
    }

    public Point bottomLeft() {
        return start.add(0, bounds.height());
    }

    public Point bottomRight() {
        return start.add(bounds.width(), bounds.height());
    }

    public boolean contains(Point point) {
        return contains(point.x(), point.y());
    }

    public boolean contains(double x, double y) {
        return x >= start.x() && x <= start.x() + bounds.width() && y >= start.y() && y <= start.y() + bounds.height();
    }

    public boolean greaterThan(Box other) {
        return false;
    }

    public Box subtract(int x, int y, int width, int height) {
        return new Box(start.subtract(x, y), bounds.subtract(width, height));
    }

    public Point topLeft() {
        return start;
    }

    public Point topRight() {
        return start.add(bounds.width(), 0);
    }

    public int z() {
        return start.z();
    }

    public double centerX() {
        return start.x() + bounds.width() / 2;
    }

    public double centerY() {
        return start.y() + bounds.height() / 2;
    }

    @Override
    public String toString() {
        return "Box{" +
                "start=" + start +
                ", bounds=" + bounds +
                '}';
    }
}
