package ninja.crinkle.mod.client.ui.menus;

public class DisplayProperties {
    private final Margin margin;
    private final Padding padding;
    private final Position position;

    public DisplayProperties(Margin margin, Padding padding, Position position) {
        this.margin = margin;
        this.padding = padding;
        this.position = position;
    }

    public Margin getMargin() {
        return margin;
    }

    public Padding getPadding() {
        return padding;
    }

    public Position getPosition() {
        return position;
    }

    public record Margin(int top, int right, int bottom, int left) {
        public Margin {
            if (top < 0 || right < 0 || bottom < 0 || left < 0) {
                throw new IllegalArgumentException("Margins cannot be negative.");
            }
        }
    }

    public record Padding(int top, int right, int bottom, int left) {
        public Padding {
            if (top < 0 || right < 0 || bottom < 0 || left < 0) {
                throw new IllegalArgumentException("Paddings cannot be negative.");
            }
        }
    }

    public record Position(int x, int y) {
        public Position {
            if (x < 0 || y < 0) {
                throw new IllegalArgumentException("Positions cannot be negative.");
            }
        }
    }

    public int getTotalHeight() {
        return margin.top() + padding.top() + padding.bottom() + margin.bottom();
    }

    public int getTotalWidth() {
        return margin.left() + padding.left() + padding.right() + margin.right();
    }

    public int getTop() {
        return margin.top() + padding.top();
    }

    public int getRight() {
        return margin.right() + padding.right();
    }

    public int getBottom() {
        return margin.bottom() + padding.bottom();
    }

    public int getLeft() {
        return margin.left() + padding.left();
    }

    public int getX() {
        return position.x();
    }

    public int getY() {
        return position.y();
    }
}
