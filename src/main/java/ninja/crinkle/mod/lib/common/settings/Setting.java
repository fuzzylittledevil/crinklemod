package ninja.crinkle.mod.lib.common.settings;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;

public abstract class Setting<T extends Comparable<? super T>, E extends Entity> {
    protected final String key;
    protected final Class<T> type;
    protected final Component label;
    protected final Component tooltip;
    protected final Map<BiPredicate<E, T>, BiFunction<E, T, Component>> validators;
    protected final Supplier<T> defaultSupplier;
    protected final Function<E, T> getter;
    protected final BiConsumer<E, T> setter;
    protected final RangeSupplier<T, E> rangeSupplier;
    protected final Class<E> entityType;

    protected Setting(String key, Component label, Component tooltip, Class<T> type, Class<E> entityType,
                      Map<BiPredicate<E, T>, BiFunction<E, T, Component>> validators, Supplier<T> defaultSupplier,
                      Function<E, T> getter, BiConsumer<E, T> setter, RangeSupplier<T, E> rangeSupplier) {
        this.key = key;
        this.type = type;
        this.entityType = entityType;
        this.label = label;
        this.tooltip = tooltip;
        this.validators = validators;
        this.defaultSupplier = defaultSupplier;
        this.getter = getter;
        this.setter = setter;
        this.rangeSupplier = rangeSupplier;
    }

    @Contract("_, _ -> new")
    public static <E extends Entity> Setting.@NotNull IntValueBuilder<E> intBuilder(String key, Class<E> entityType) {
        return new IntValueBuilder<>(key, entityType);
    }

    @Contract("_, _ -> new")
    public static <E extends Entity> Setting.@NotNull DoubleValueBuilder<E> doubleBuilder(String key, Class<E> entityType) {
        return new DoubleValueBuilder<>(key, entityType);
    }

    @Contract("_, _ -> new")
    @SuppressWarnings("unused")
    public static <E extends Entity> Setting.@NotNull BooleanValueBuilder<E> booleanBuilder(String key, Class<E> entityType) {
        return new BooleanValueBuilder<>(key, entityType);
    }

    @Contract("_, _ -> new")
    @SuppressWarnings("unused")
    public static <E extends Entity> Setting.@NotNull StringValueBuilder<E> stringBuilder(String key, Class<E> entityType) {
        return new StringValueBuilder<>(key, entityType);
    }

    public Component label() {
        return label;
    }

    public Component tooltip() {
        return tooltip;
    }

    public RangeSupplier<T, E> range() {
        return rangeSupplier;
    }

    public List<Component> errors(E entity, Object value) {
        if (!isValid(value)) {
            if (isInt()) {
                return List.of(Component.translatable("validation.crinklemod.metabolism.failure.invalid.integer", value));
            }
            if (isDouble()) {
                return List.of(Component.translatable("validation.crinklemod.metabolism.failure.invalid.double", value));
            }
            if (isBoolean()) {
                return List.of(Component.translatable("validation.crinklemod.metabolism.failure.invalid.boolean", value));
            }
            return List.of(Component.translatable("validation.crinklemod.metabolism.failure.invalid.general", value));
        }
        List<Component> errors = new ArrayList<>();
        validators.forEach((k, v) -> {
            if (!k.test(entity, valueOf(value.toString()))) {
                errors.add(v.apply(entity, valueOf(value.toString())));
            }
        });
        return errors;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isInt() {
        return false;
    }

    public boolean isDouble() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public abstract T valueOf(String value);

    public boolean isValid(Object value) {
        if (value == null) return false;
        if (isBoolean() && value.toString().equals("true") || value.toString().equals("false")) return true;
        if (isInt() || isDouble()) {
            try {
                valueOf(value.toString());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        // String
        return true;
    }

    public T getDefault() {
        return defaultSupplier.get();
    }

    public int getDefaultInt() {
        return IntValue.of(getDefault());
    }

    public double getDefaultDouble() {
        return DoubleValue.of(getDefault());
    }

    public boolean getDefaultBoolean() {
        return BooleanValue.of(getDefault());
    }

    public String getDefaultString() {
        return StringValue.of(getDefault());
    }

    public String key() {
        return key;
    }

    public T get(E entity) {
        if (getter == null)
            return getDefault();
        return getter.apply(entity);
    }

    public int getInt(E entity) {
        return IntValue.of(get(entity));
    }

    public double getDouble(E entity) {
        return DoubleValue.of(get(entity));
    }

    public boolean getBoolean(E entity) {
        return BooleanValue.of(get(entity));
    }

    public String getString(E entity) {
        return StringValue.of(get(entity));
    }

    public void set(E entity, Object value) {
        if (setter != null)
            setter.accept(entity, valueOf(value.toString()));
    }

    public void setInteger(E entity, int value) {
        set(entity, value);
    }

    public void setDouble(E entity, double value) {
        set(entity, value);
    }

    public void setBoolean(E entity, boolean value) {
        set(entity, value);
    }

    public void setString(E entity, String value) {
        set(entity, value);
    }

    public static class DoubleValue<E extends Entity> extends Setting<Double, E> {
        protected DoubleValue(String key, Component label, Component tooltip, Class<E> entityType,
                              Map<BiPredicate<E, Double>, BiFunction<E, Double, Component>> validators,
                              Supplier<Double> defaultSupplier, Function<E, Double> getter,
                              BiConsumer<E, Double> setter, RangeSupplier<Double, E> rangeSupplier) {
            super(key, label, tooltip, Double.class, entityType, validators, defaultSupplier, getter, setter, rangeSupplier);
        }
        public static double of(Object value) {
            return Double.parseDouble(String.valueOf(value));
        }

        @Override
        public Double valueOf(String value) {
            return Double.valueOf(value);
        }

        @Override
        public boolean isDouble() {
            return true;
        }
    }

    public static class IntValue<E extends Entity> extends Setting<Integer, E> {
        protected IntValue(String key, Component label, Component tooltip, Class<E> entityType,
                           Map<BiPredicate<E, Integer>, BiFunction<E, Integer, Component>> validators,
                           Supplier<Integer> defaultSupplier, Function<E, Integer> getter,
                           BiConsumer<E, Integer> setter, RangeSupplier<Integer, E> rangeSupplier) {
            super(key, label, tooltip, Integer.class, entityType, validators, defaultSupplier, getter, setter, rangeSupplier);
        }

        public static int of(Object value) {
            return Integer.parseInt(String.valueOf(value));
        }

        @Override
        public Integer valueOf(String value) {
            return Integer.valueOf(value);
        }

        @Override
        public boolean isInt() {
            return true;
        }
    }

    public static class BooleanValue<E extends Entity> extends Setting<Boolean, E> {
        protected BooleanValue(String key, Component label, Component tooltip, Class<E> entityType,
                               Map<BiPredicate<E, Boolean>, BiFunction<E, Boolean, Component>> validators,
                               Supplier<Boolean> defaultSupplier, Function<E, Boolean> getter,
                               BiConsumer<E, Boolean> setter, RangeSupplier<Boolean, E> rangeSupplier) {
            super(key, label, tooltip, Boolean.class, entityType, validators, defaultSupplier, getter, setter, rangeSupplier);
        }

        public static boolean of(Object value) {
            return Boolean.parseBoolean(String.valueOf(value));
        }

        @Override
        public Boolean valueOf(String value) {
            return Boolean.valueOf(value);
        }

        @Override
        public boolean isBoolean() {
            return true;
        }
    }

    public static class StringValue<E extends Entity> extends Setting<String, E> {
        protected StringValue(String key, Component label, Component tooltip, Class<E> entityType,
                              Map<BiPredicate<E, String>, BiFunction<E, String, Component>> validators,
                              Supplier<String> defaultSupplier, Function<E, String> getter,
                              BiConsumer<E, String> setter, RangeSupplier<String, E> rangeSupplier) {
            super(key, label, tooltip, String.class, entityType, validators, defaultSupplier, getter, setter, rangeSupplier);
        }

        public static String of(Object value) {
            return String.valueOf(value);
        }

        @Override
        public String valueOf(String value) {
            return value;
        }

        @Override
        public boolean isString() {
            return true;
        }
    }

    public static abstract class Builder<T extends Comparable<? super T>, E extends Entity> {
        protected final String key;
        protected final Class<E> entityType;
        protected final Map<BiPredicate<E, T>, BiFunction<E, T, Component>> validators = new HashMap<>();
        protected Component label;
        protected Component tooltip;
        protected Supplier<T> defaultSupplier;
        protected Function<E, T> getter;
        protected BiConsumer<E, T> setter;
        protected RangeSupplier<T, E> rangeSupplier;

        private Builder(String key, Class<E> entityType) {
            this.key = key;
            this.entityType = entityType;
        }

        public Builder<T, E> label(Component label) {
            this.label = label;
            return this;
        }

        public Builder<T, E> tooltip(Component tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder<T, E> defaultValue(T value) {
            this.defaultSupplier = () -> value;
            return this;
        }

        public Builder<T, E> defaultValue(Supplier<T> supplier) {
            this.defaultSupplier = supplier;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder<T, E> validator(BiPredicate<E, T> validator, BiFunction<E, T, Component> messageSupplier) {
            validators.put(validator, messageSupplier);
            return this;
        }

        public Builder<T, E> getter(Function<E, T> getter) {
            this.getter = getter;
            return this;
        }

        public Builder<T, E> setter(BiConsumer<E, T> setter) {
            this.setter = setter;
            return this;
        }

        public Builder<T, E> range(RangeSupplier<T, E> rangeSupplier) {
            this.rangeSupplier = rangeSupplier;
            return this;
        }

        public Builder<T, E> range(Function<E, T> min, Function<E, T> max) {
            return this.range(new RangeSupplier<>(min, max));
        }

        public abstract Setting<T, E> build();
    }

    public static class IntValueBuilder<E extends Entity> extends Builder<Integer, E> {
        private IntValueBuilder(String key, Class<E> entityType) {
            super(key, entityType);
        }

        @Override
        public IntValue<E> build() {
            if (rangeSupplier != null)
                validators.put((e, v) -> rangeSupplier.apply(e).contains(v), (e, v) -> {
                    RangeSupplier.Result<Integer> range = rangeSupplier.apply(e);
                    return Component.translatable("validation.crinklemod.metabolism.failure.out_of_range.integer", v, range.min(), range.max());
                });
            return new IntValue<>(key, label, tooltip, entityType, validators, defaultSupplier, getter, setter, rangeSupplier);
        }
    }

    public static class DoubleValueBuilder<E extends Entity> extends Builder<Double, E> {

        private DoubleValueBuilder(String key, Class<E> entityType) {
            super(key, entityType);
        }

        public DoubleValue<E> build() {
            if (rangeSupplier != null) {
                validators.put((e, v) -> rangeSupplier.apply(e).contains(v), (e, v) -> {
                    RangeSupplier.Result<Double> range = rangeSupplier.apply(e);
                    return Component.translatable("validation.crinklemod.metabolism.failure.out_of_range.double", v, range.min(), range.max());
                });
            }
            return new DoubleValue<>(key, label, tooltip, entityType, validators, defaultSupplier, getter, setter, rangeSupplier);
        }
    }

    public static class BooleanValueBuilder<E extends Entity> extends Builder<Boolean, E> {
        private BooleanValueBuilder(String key, Class<E> entityType) {
            super(key, entityType);
        }

        public BooleanValue<E> build() {
            if (rangeSupplier != null)
                throw new IllegalStateException("Boolean values cannot have a rangeSupplier");
            return new BooleanValue<>(key, label, tooltip, entityType, validators, defaultSupplier, getter, setter, null);
        }
    }

    public static class StringValueBuilder<E extends Entity> extends Builder<String, E> {
        private StringValueBuilder(String key, Class<E> entityType) {
            super(key, entityType);
        }

        public StringValue<E> build() {
            if (rangeSupplier != null)
                throw new IllegalStateException("String values cannot have a rangeSupplier");
            return new StringValue<>(key, label, tooltip, entityType, validators, defaultSupplier, getter, setter, null);
        }
    }

    public static class RangeSupplier<T extends Comparable<? super T>, E extends Entity> {
        private final Function<E, T> min;
        private final Function<E, T> max;

        public RangeSupplier(Function<E, T> min, Function<E, T> max) {
            this.min = min;
            this.max = max;
        }

        public Result<T> apply(E entity) {
            return new Result<>(min.apply(entity), max.apply(entity));
        }

        public record Result<T extends Comparable<? super T>>(T min, T max) {

            public Result<Integer> asInt() {
                return new Result<>(IntValue.of(min), IntValue.of(max));
            }

            public Result<Double> asDouble() {
                return new Result<>(DoubleValue.of(min), DoubleValue.of(max));
            }

            public boolean contains(T value) {
                return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
            }
        }
    }
}
