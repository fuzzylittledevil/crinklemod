package ninja.crinkle.mod.client.gui.textures;

import java.util.function.IntUnaryOperator;

public enum ColorFilters {
    NORMAL(i -> i),
    INVERT(i -> 0xFFFFFF - i),
    GRAYSCALE(i -> {
        int r = (i >> 16) & 0xFF;
        int g = (i >> 8) & 0xFF;
        int b = i & 0xFF;
        int avg = (r + g + b) / 3;
        return (avg << 16) | (avg << 8) | avg;
    }),
    SEPIA(i -> {
        int r = (i >> 16) & 0xFF;
        int g = (i >> 8) & 0xFF;
        int b = i & 0xFF;
        int avg = (r + g + b) / 3;
        r = Math.min(255, (int) (avg * 1.07));
        g = Math.min(255, (int) (avg * 0.74));
        b = Math.min(255, (int) (avg * 0.43));
        return (r << 16) | (g << 8) | b;
    }),
    ;


    private final IntUnaryOperator filter;

    ColorFilters(IntUnaryOperator filter) {
        this.filter = filter;
    }

    public static ColorFilters fromString(String name) {
        for (ColorFilters filter : values()) {
            if (filter.name().equalsIgnoreCase(name)) {
                return filter;
            }
        }
        return NORMAL;
    }

    public IntUnaryOperator filter() {
        return filter;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
