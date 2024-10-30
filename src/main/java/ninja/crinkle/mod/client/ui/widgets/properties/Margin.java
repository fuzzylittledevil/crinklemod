package ninja.crinkle.mod.client.ui.widgets.properties;

public record Margin(int top, int bottom, int left, int right) {
    public static final Margin ZERO = new Margin(0, 0, 0, 0);

    public Margin {
        if (top < 0 || bottom < 0 || left < 0 || right < 0) {
            throw new IllegalArgumentException("Margin values cannot be negative");
        }
    }

    public Margin(int all) {
        this(all, all, all, all);
    }

    public Margin(int vertical, int horizontal) {
        this(vertical, vertical, horizontal, horizontal);
    }
}
