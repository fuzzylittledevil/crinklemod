package ninja.crinkle.mod.lib.common.util;

import org.jetbrains.annotations.NotNull;

public class Range<T extends Comparable<? super T>> {
    private final T min;
    private final T max;

    public Range(T min, T max) {
        this.min = min;
        this.max = max;
    }

    public boolean contains(@NotNull T value) {
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }

    public T max() {
        return max;
    }

    public T min() {
        return min;
    }
}
