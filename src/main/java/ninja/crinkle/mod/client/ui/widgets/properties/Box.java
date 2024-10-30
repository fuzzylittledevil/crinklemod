package ninja.crinkle.mod.client.ui.widgets.properties;

public record Box(int x, int y, int width, int height) {
    public static final Box ZERO = new Box(0, 0, 0, 0);

    public Box {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Box width and height values cannot be negative");
        }
    }

    public Box(int all) {
        this(all, all, all, all);
    }

    public Box(int vertical, int horizontal) {
        this(vertical, horizontal, vertical, horizontal);
    }

    public Box add(int value) {
        return new Box(x() + value, y() + value, width() + value, height() + value);
    }

    public Box add(int x, int y, int width, int height) {
        return new Box(x() + x, y() + y, width() + width, height() + height);
    }

    public boolean contains(int x, int y) {
        return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
    }

    public boolean contains(double x, double y) {
        return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
    }
}
