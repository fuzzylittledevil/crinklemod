package ninja.crinkle.mod.client.gui.properties;

public record Size(int width, int height) {
    public static final Size ZERO = new Size(0, 0);

    public Size {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Box width and height values cannot be negative");
        }
    }

    public static Size of(int width, int height) {
        return new Size(width, height);
    }

    public Size add(Size size) {
        return new Size(Math.max(0, width() + size.width()), Math.max(0, height() + size.height()));
    }

    public Size add(int width, int height) {
        return new Size(Math.max(0, width() + width), Math.max(0, height() + height));
    }

    public Size copy() {
        return new Size(width, height);
    }

    public Size subtract(Size size) {
        return new Size(Math.max(0, width() - size.width()), Math.max(0, height() - size.height()));
    }

    public Size subtract(int width, int height) {
        return new Size(Math.max(0, width() - width), Math.max(0, height() - height));
    }

    public boolean contains(Point start, Point point) {
        return point.x() >= start.x()
                && point.x() < start.x() + width()
                && point.y() >= start.y()
                && point.y() < start.y() + height();
    }

    public boolean greaterThan(Size size) {
        return width() > size.width() && height() > size.height();
    }

    public boolean lessThan(Size size) {
        return width() < size.width() && height() < size.height();
    }

    public Size max(Size size) {
        return new Size(Math.max(width(), size.width()), Math.max(height(), size.height()));
    }

    public Size min(Size size) {
        return new Size(Math.min(width(), size.width()), Math.min(height(), size.height()));
    }

    @Override
    public String toString() {
        return "Bounds{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
