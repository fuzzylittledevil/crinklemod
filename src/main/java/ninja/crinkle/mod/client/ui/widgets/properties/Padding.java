package ninja.crinkle.mod.client.ui.widgets.properties;

public record Padding(int top, int bottom, int left, int right) {
    public static final Padding ZERO = new Padding(0, 0, 0, 0);

    public Padding {
        if (top < 0 || bottom < 0 || left < 0 || right < 0) {
            throw new IllegalArgumentException("Padding values cannot be negative");
        }
    }

    public Padding(int all) {
        this(all, all, all, all);
    }

    public Padding(int vertical, int horizontal) {
        this(vertical, vertical, horizontal, horizontal);
    }
}
