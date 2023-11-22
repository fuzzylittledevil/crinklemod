package ninja.crinkle.mod.settings;

import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import ninja.crinkle.mod.api.ServerUpdater;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;

public abstract class Setting<T extends Comparable<? super T>> {
    protected final String key;
    protected final Class<T> type;
    protected final Component label;
    protected final Component tooltip;
    protected final Map<BiPredicate<ICapabilityProvider, T>, BiFunction<ICapabilityProvider, T, Component>> validators;
    protected final Function<ICapabilityProvider, T> defaultSupplier;
    protected final Function<ICapabilityProvider, T> getter;
    protected final BiConsumer<ICapabilityProvider, T> setter;

    protected final Function<ICapabilityProvider, ServerUpdater> syncer;
    protected final RangeSupplier<T> rangeSupplier;

    protected Setting(String key, Component label, Component tooltip, Class<T> type,
                      Map<BiPredicate<ICapabilityProvider, T>, BiFunction<ICapabilityProvider, T, Component>> validators,
                      Function<ICapabilityProvider, T> defaultSupplier, Function<ICapabilityProvider, T> getter,
                      BiConsumer<ICapabilityProvider, T> setter, Function<ICapabilityProvider, ServerUpdater> syncer, RangeSupplier<T> rangeSupplier) {
        this.key = key;
        this.type = type;
        this.label = label;
        this.tooltip = tooltip;
        this.validators = validators;
        this.defaultSupplier = defaultSupplier;
        this.getter = getter;
        this.setter = setter;
        this.syncer = syncer;
        this.rangeSupplier = rangeSupplier;
    }

    @Contract("_ -> new")
    public static Setting.@NotNull IntValueBuilder intBuilder(String key) {
        return new IntValueBuilder(key);
    }

    @Contract("_ -> new")
    public static Setting.@NotNull DoubleValueBuilder doubleBuilder(String key) {
        return new DoubleValueBuilder(key);
    }

    @Contract("_ -> new")
    @SuppressWarnings("unused")
    public static Setting.@NotNull BooleanValueBuilder booleanBuilder(String key) {
        return new BooleanValueBuilder(key);
    }

    @Contract("_ -> new")
    @SuppressWarnings("unused")
    public static Setting.@NotNull StringValueBuilder stringBuilder(String key) {
        return new StringValueBuilder(key);
    }

    public Component label() {
        return label;
    }

    public Component tooltip() {
        return tooltip;
    }

    public RangeSupplier<T> range() {
        return rangeSupplier;
    }

    public List<Component> errors(ICapabilityProvider entity, Object value) {
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

    public Optional<ServerUpdater> syncer(ICapabilityProvider provider) {
        return Optional.ofNullable(syncer).map(s -> s.apply(provider));
    }

    public T getDefault(ICapabilityProvider provider) {
        return defaultSupplier.apply(provider);
    }

    public double getDefaultDouble(ICapabilityProvider provider) {
        return DoubleValue.of(getDefault(provider));
    }

    public String key() {
        return key;
    }

    public T get(ICapabilityProvider provider) {
        if (getter == null)
            return getDefault(provider);
        return getter.apply(provider);
    }

    public int getInt(ICapabilityProvider provider) {
        return IntValue.of(get(provider));
    }

    public double getDouble(ICapabilityProvider entity) {
        return DoubleValue.of(get(entity));
    }

    public boolean getBoolean(ICapabilityProvider entity) {
        return BooleanValue.of(get(entity));
    }

    public String getString(ICapabilityProvider entity) {
        return StringValue.of(get(entity));
    }

    public void set(ICapabilityProvider entity, Object value) {
        if (setter != null)
            setter.accept(entity, valueOf(value.toString()));
    }

    public void setInteger(ICapabilityProvider entity, int value) {
        set(entity, value);
    }

    public void setDouble(ICapabilityProvider entity, double value) {
        set(entity, value);
    }

    public void setBoolean(ICapabilityProvider entity, boolean value) {
        set(entity, value);
    }

    public void setString(ICapabilityProvider entity, String value) {
        set(entity, value);
    }

    public static class DoubleValue extends Setting<Double> {
        protected DoubleValue(String key, Component label, Component tooltip,
                              Map<BiPredicate<ICapabilityProvider, Double>, BiFunction<ICapabilityProvider, Double, Component>> validators,
                              Function<ICapabilityProvider, Double> defaultSupplier, Function<ICapabilityProvider, Double> getter,
                              BiConsumer<ICapabilityProvider, Double> setter, Function<ICapabilityProvider, ServerUpdater> syncer, RangeSupplier<Double> rangeSupplier) {
            super(key, label, tooltip, Double.class, validators, defaultSupplier, getter, setter, syncer, rangeSupplier);
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

    public static class IntValue extends Setting<Integer> {
        protected IntValue(String key, Component label, Component tooltip,
                           Map<BiPredicate<ICapabilityProvider, Integer>, BiFunction<ICapabilityProvider, Integer, Component>> validators,
                           Function<ICapabilityProvider, Integer> defaultSupplier, Function<ICapabilityProvider, Integer> getter,
                           BiConsumer<ICapabilityProvider, Integer> setter, Function<ICapabilityProvider, ServerUpdater> syncer, RangeSupplier<Integer> rangeSupplier) {
            super(key, label, tooltip, Integer.class, validators, defaultSupplier, getter, setter, syncer, rangeSupplier);
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

    public static class BooleanValue extends Setting<Boolean> {
        protected BooleanValue(String key, Component label, Component tooltip,
                               Map<BiPredicate<ICapabilityProvider, Boolean>, BiFunction<ICapabilityProvider, Boolean, Component>> validators,
                               Function<ICapabilityProvider, Boolean> defaultSupplier, Function<ICapabilityProvider, Boolean> getter,
                               BiConsumer<ICapabilityProvider, Boolean> setter, Function<ICapabilityProvider, ServerUpdater> syncer, RangeSupplier<Boolean> rangeSupplier) {
            super(key, label, tooltip, Boolean.class, validators, defaultSupplier, getter, setter, syncer, rangeSupplier);
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

    public static class StringValue extends Setting<String> {
        protected StringValue(String key, Component label, Component tooltip,
                              Map<BiPredicate<ICapabilityProvider, String>, BiFunction<ICapabilityProvider, String, Component>> validators,
                              Function<ICapabilityProvider, String> defaultSupplier, Function<ICapabilityProvider, String> getter,
                              BiConsumer<ICapabilityProvider, String> setter, Function<ICapabilityProvider, ServerUpdater> syncer, RangeSupplier<String> rangeSupplier) {
            super(key, label, tooltip, String.class, validators, defaultSupplier, getter, setter, syncer, rangeSupplier);
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

    public static abstract class Builder<T extends Comparable<? super T>> {
        protected final String key;
        protected final Map<BiPredicate<ICapabilityProvider, T>, BiFunction<ICapabilityProvider, T, Component>> validators = new HashMap<>();
        protected Component label;
        protected Component tooltip;
        protected Function<ICapabilityProvider, T> defaultSupplier;
        protected Function<ICapabilityProvider, T> getter;
        protected BiConsumer<ICapabilityProvider, T> setter;
        protected Function<ICapabilityProvider, ServerUpdater> syncer;
        protected RangeSupplier<T> rangeSupplier;

        private Builder(String key) {
            this.key = key;
        }

        public Builder<T> label(Component label) {
            this.label = label;
            return this;
        }

        public Builder<T> tooltip(Component tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder<T> defaultValue(T value) {
            this.defaultSupplier = p -> value;
            return this;
        }

        public Builder<T> defaultValue(Function<ICapabilityProvider, T> supplier) {
            this.defaultSupplier = supplier;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder<T> validator(BiPredicate<ICapabilityProvider, T> validator, BiFunction<ICapabilityProvider, T, Component> messageSupplier) {
            validators.put(validator, messageSupplier);
            return this;
        }

        public Builder<T> getter(Function<ICapabilityProvider, T> getter) {
            this.getter = getter;
            return this;
        }

        public Builder<T> setter(BiConsumer<ICapabilityProvider, T> setter) {
            this.setter = setter;
            return this;
        }
        
        public Builder<T> synchronizer(Function<ICapabilityProvider, ServerUpdater> synchronizer) {
            this.syncer = synchronizer;
            return this;
        }

        public Builder<T> range(RangeSupplier<T> rangeSupplier) {
            this.rangeSupplier = rangeSupplier;
            return this;
        }

        public Builder<T> range(Function<ICapabilityProvider, T> min, Function<ICapabilityProvider, T> max) {
            return this.range(new RangeSupplier<>(min, max));
        }

        public abstract Setting<T> build();
    }

    public static class IntValueBuilder extends Builder<Integer> {
        private IntValueBuilder(String key) {
            super(key);
        }

        @Override
        public IntValue build() {
            if (rangeSupplier != null)
                validators.put((e, v) -> rangeSupplier.apply(e).contains(v), (e, v) -> {
                    RangeSupplier.Result<Integer> range = rangeSupplier.apply(e);
                    return Component.translatable("validation.crinklemod.metabolism.failure.out_of_range.integer", v, range.min(), range.max());
                });
            return new IntValue(key, label, tooltip, validators, defaultSupplier, getter, setter, syncer, rangeSupplier);
        }
    }

    public static class DoubleValueBuilder extends Builder<Double> {

        private DoubleValueBuilder(String key) {
            super(key);
        }

        public DoubleValue build() {
            if (rangeSupplier != null) {
                validators.put((e, v) -> rangeSupplier.apply(e).contains(v), (e, v) -> {
                    RangeSupplier.Result<Double> range = rangeSupplier.apply(e);
                    return Component.translatable("validation.crinklemod.metabolism.failure.out_of_range.double", v, range.min(), range.max());
                });
            }
            return new DoubleValue(key, label, tooltip, validators, defaultSupplier, getter, setter, syncer, rangeSupplier);
        }
    }

    public static class BooleanValueBuilder extends Builder<Boolean> {
        private BooleanValueBuilder(String key) {
            super(key);
        }

        public BooleanValue build() {
            if (rangeSupplier != null)
                throw new IllegalStateException("Boolean values cannot have a rangeSupplier");
            return new BooleanValue(key, label, tooltip, validators, defaultSupplier, getter, setter, syncer, null);
        }
    }

    public static class StringValueBuilder extends Builder<String> {
        private StringValueBuilder(String key) {
            super(key);
        }

        public StringValue build() {
            if (rangeSupplier != null)
                throw new IllegalStateException("String values cannot have a rangeSupplier");
            return new StringValue(key, label, tooltip, validators, defaultSupplier, getter, setter, syncer, null);
        }
    }

    public static class RangeSupplier<T extends Comparable<? super T>> {
        private final Function<ICapabilityProvider, T> min;
        private final Function<ICapabilityProvider, T> max;

        public RangeSupplier(Function<ICapabilityProvider, T> min, Function<ICapabilityProvider, T> max) {
            this.min = min;
            this.max = max;
        }

        public Result<T> apply(ICapabilityProvider entity) {
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
