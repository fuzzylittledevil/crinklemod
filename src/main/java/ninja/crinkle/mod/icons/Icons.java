package ninja.crinkle.mod.icons;

public enum Icons {
    RESET(0, 0),
    WRENCH(1, 0),
    DOWN_ARROW(0, 1);

    private final int x;
    private final int y;

    Icons(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
