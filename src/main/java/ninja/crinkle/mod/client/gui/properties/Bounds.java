package ninja.crinkle.mod.client.gui.properties;

public record Bounds(int width, int height) {
    public static final Bounds ZERO = new Bounds(0, 0);

    public Bounds {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Box width and height values cannot be negative");
        }
    }

    public Bounds add(Bounds bounds) {
        return new Bounds(width + bounds.width(), height + bounds.height());
    }

    public Bounds add(int width, int height) {
        return add(new Bounds(width, height));
    }

    public Bounds subtract(Bounds bounds) {
        return new Bounds(width - bounds.width(), height - bounds.height());
    }

    public Bounds subtract(int width, int height) {
        return subtract(new Bounds(width, height));
    }

    public boolean contains(Point start, Point point) {
        return point.x() >= start.x() && point.x() < start.x() + width() && point.y() >= start.y()
                && point.y() < start.y() + height();
    }

    public boolean greaterThan(Bounds bounds) {
        return width() > bounds.width() && height() > bounds.height();
    }

    public boolean lessThan(Bounds bounds) {
        return width() < bounds.width() && height() < bounds.height();
    }

    public Bounds max(Bounds bounds) {
        return new Bounds(Math.max(width, bounds.width()), Math.max(height, bounds.height()));
    }

    public Bounds min(Bounds bounds) {
        return new Bounds(Math.min(width, bounds.width()), Math.min(height, bounds.height()));
    }

    @Override
    public String toString() {
        return "Bounds{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
