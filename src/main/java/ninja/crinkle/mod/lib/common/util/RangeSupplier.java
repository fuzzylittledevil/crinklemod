package ninja.crinkle.mod.lib.common.util;

import java.util.function.Supplier;

public class RangeSupplier<T extends Comparable<? super T>> {
    private final Supplier<T> min;
    private final Supplier<T> max;

    public RangeSupplier(Supplier<T> min, Supplier<T> max) {
        this.min = min;
        this.max = max;
    }

    public boolean contains(T value) {
        return value.compareTo(min.get()) >= 0 && value.compareTo(max.get()) <= 0;
    }
}
