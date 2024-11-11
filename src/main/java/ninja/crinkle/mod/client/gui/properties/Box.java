package ninja.crinkle.mod.client.gui.properties;

import java.util.Objects;

public final class Box {
    private final Position position;
    private final ImmutablePoint start;
    private final Size size;
    private final ImmutablePoint topLeft;
    private final ImmutablePoint topRight;
    private final ImmutablePoint bottomLeft;
    private final ImmutablePoint bottomRight;

    public Box(Position position, Size size) {
        if (size.width() < 0 || size.height() < 0) {
            throw new IllegalArgumentException("Box width and height values cannot be negative");
        }
        this.position = position;
        this.start = position.point();
        this.size = size;
        this.topLeft = this.start;
        this.topRight = ImmutablePoint.from(position.point().add(size.width(), 0));
        this.bottomLeft = ImmutablePoint.from(position.point().add(0, size.height()));
        this.bottomRight = ImmutablePoint.from(position.point().add(size.width(), size.height()));
    }

    public Box(Point start, Size size) {
        this(Position.absolute(ImmutablePoint.from(start)), size);
    }

    public Box(Point from, Point to) {
        this(from, new Size((int) (to.x() - from.x()), (int) (to.y() - from.y())));
    }

    public Box(int x, int y, int width, int height) {
        this(new ImmutablePoint(x, y), new Size(width, height));
    }

    public Box add(int x, int y, int width, int height) {
        return new Box(start().add(x, y), size.add(width, height));
    }

    public Box add(Box box) {
        return add(box.start(), box.size());
    }

    public Box add(Position position) {
        return new Box(position.offsetBy(start), size);
    }

    public Position position() {
        return position;
    }

    public Box shrink(BoxProperty property) {
        return new Box(start().add(property.left(), property.top()),
                size().subtract(property.right() + property.left(),
                        property.bottom() + property.top()));
    }

    private Box add(Point start, Size size) {
        return new Box(this.start().add(start), this.size.add(size));
    }

    public Box add(Point offset) {
        return new Box(this.start().add(offset), size);
    }

    public Box add(Size size) {
        return new Box(start(), this.size.add(size));
    }

    public ImmutablePoint bottomLeft() {
        return bottomLeft;
    }

    public ImmutablePoint bottomRight() {
        return bottomRight;
    }

    public boolean contains(Point point) {
        return contains(point.x(), point.y());
    }

    public boolean contains(double x, double y) {
        return x >= start().x() && x <= start().x() + size.width() && y >= start().y() && y <= start().y() + size.height();
    }

    public boolean overlaps(Box box) {
        return start().x() < box.start().x() + box.size.width() && start().x() + size.width() > box.start().x() &&
                start().y() < box.start().y() + box.size.height() && start().y() + size.height() > box.start().y();
    }

    public Box subtract(int x, int y, int width, int height) {
        return new Box(start().subtract(x, y), size.subtract(width, height));
    }

    public Box grow(BoxProperty property) {
        return subtract(-property.top(), -property.right(), property.bottom(), property.left());
    }

    public ImmutablePoint topLeft() {
        return topLeft;
    }

    public ImmutablePoint topRight() {
        return topRight;
    }

    @Override
    public String toString() {
        return "Box{" +
                "start=" + start +
                ", bounds=" + size +
                '}';
    }

    public ImmutablePoint start() {
        return start;
    }

    public Size size() {
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Box) obj;
        return Objects.equals(this.start, that.start) &&
                Objects.equals(this.size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, size);
    }

}
