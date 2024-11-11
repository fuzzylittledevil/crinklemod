package ninja.crinkle.mod.client.gui.properties;

public record Position(ImmutablePoint point, Type type) {
    public Position {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
    }

    public static Position absolute(ImmutablePoint absolute) {
        return new Position(absolute, Type.Absolute);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position position = (Position) obj;
        return point.equals(position.point) && type == position.type;
    }

    public static Position absolute(int x, int y) {
        return absolute(new ImmutablePoint(x, y));
    }

    public static Position relative(ImmutablePoint relative) {
        return new Position(relative, Type.Relative);
    }

    public static Position relative(int x, int y) {
        return relative(new ImmutablePoint(x, y));
    }

    public Position offsetBy(double x, double y) {
        return new Position(point.add(x, y), type);
    }

    public Position withBase(ImmutablePoint base) {
        return new Position(base.add(point), type);
    }

    public Position offsetBy(int x, int y) {
        return new Position(point.add(x, y), type);
    }

    public Position offsetBy(ImmutablePoint offset) {
        return new Position(point.add(offset), type);
    }

    public Position withBase(int x, int y) {
        return withBase(new ImmutablePoint(x, y));
    }

    public boolean absolute() {
        return type() == Type.Absolute;
    }

    public boolean relative() {
        return type() == Type.Relative;
    }

    @Override
    public String toString() {
        return "Position[" + "point=" + point + ", type=" + type + ']';
    }

    public Position withType(Type type) {
        return new Position(point, type);
    }

    public enum Type {
        Absolute, Relative
    }
}
