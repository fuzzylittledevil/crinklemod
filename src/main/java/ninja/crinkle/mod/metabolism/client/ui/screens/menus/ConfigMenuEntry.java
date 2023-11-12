package ninja.crinkle.mod.metabolism.client.ui.screens.menus;

import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigMenuEntry {
    private final int lineNumber;
    private final int maxLength;
    private final Component label;
    private final Component tooltip;
    private final Supplier<Double> valueSupplier;
    private final Consumer<Double> valueSetter;
    private final Supplier<Double> defaultSupplier;

    protected ConfigMenuEntry(int lineNumber, Component label, Component tooltip, Supplier<Double> valueSupplier,
                              Consumer<Double> valueSetter, Supplier<Double> defaultSupplier, int maxLength) {
        this.lineNumber = lineNumber;
        this.maxLength = maxLength;
        this.label = label;
        this.tooltip = tooltip;
        this.valueSupplier = valueSupplier;
        this.valueSetter = valueSetter;
        this.defaultSupplier = defaultSupplier;
    }

    public static Builder builder(int lineNumber, Component label) {
        return new Builder(lineNumber, label);
    }

    public static class Builder {
        private final int lineNumber;
        private int maxLength;
        private final Component label;
        private Component tooltip;
        private Supplier<Double> valueSupplier = () -> null;
        private Consumer<Double> valueSetter = (value) -> {};
        private Supplier<Double> defaultSupplier = () -> null;

        public Builder(int lineNumber, Component label) {
            this.lineNumber = lineNumber;
            this.label = label;
        }

        public Builder maxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public Builder tooltip(Component tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder valueSupplier(Supplier<Double> valueSupplier) {
            this.valueSupplier = valueSupplier;
            return this;
        }

        public Builder valueSetter(Consumer<Double> valueSetter) {
            this.valueSetter = valueSetter;
            return this;
        }

        public Builder defaultSupplier(Supplier<Double> defaultSupplier) {
            this.defaultSupplier = defaultSupplier;
            return this;
        }

        public ConfigMenuEntry build() {
            return new ConfigMenuEntry(lineNumber, label, tooltip, valueSupplier, valueSetter, defaultSupplier, maxLength);
        }
    }

    public Component getLabel() {
        return label;
    }

    public Component getTooltip() {
        return tooltip;
    }

    public Double getValue() {
        return valueSupplier.get();
    }

    public void setValue(Double value) {
        valueSetter.accept(value);
    }

    public Double getDefaultValue() {
        return defaultSupplier.get();
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getMaxLength() {
        return maxLength;
    }
}
