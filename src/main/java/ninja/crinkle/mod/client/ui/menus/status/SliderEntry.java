package ninja.crinkle.mod.client.ui.menus.status;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.icons.Icons;
import ninja.crinkle.mod.client.ui.widgets.Icon;
import ninja.crinkle.mod.client.ui.widgets.Label;
import ninja.crinkle.mod.client.ui.widgets.themes.AbstractThemedButton;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedButton;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedSlider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class SliderEntry implements IEntry {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final int lineNumber;
    private final ThemedSlider slider;
    private boolean showLabel = true;
    private boolean showValues = true;
    private boolean labelOnTop = false;
    private final Map<Double, AbstractWidget> valueWidgets = new HashMap<>();

    public SliderEntry(int lineNumber, ThemedSlider slider) {
        this.lineNumber = lineNumber;
        this.slider = slider;
    }

    public static Builder builder(int lineNumber) {
        return new Builder(lineNumber);
    }

    private void adjustValueWidgets(boolean above) {
        for (Map.Entry<Double, AbstractWidget> entry : valueWidgets.entrySet()) {
            AbstractWidget widget = entry.getValue();
            int minValue = (int) slider.getMinValue();
            int maxValue = (int) slider.getMaxValue();
            int widgetX = (int) ((entry.getKey() - minValue) / (maxValue - minValue) * (slider.getTrackWidth() - widget.getWidth()));
            int widgetY = slider.getY() + (slider.getHeight() / 2) - (widget.getHeight() / 2) + 1;
            if (above) {
                widgetY -= widget.getHeight() + 1;
            } else {
                widgetY += slider.getHeight() + 1;
            }
            widget.setX(widgetX + slider.getX());
            widget.setY(widgetY);
        }
    }

    @Override
    public List<AbstractWidget> create(@NotNull StatusMenu menu) {
        List<AbstractWidget> widgets = new ArrayList<>();
        if (showValues && labelOnTop) {
            LOGGER.warn("Slider values labels are not supported when label is on top");
            showValues = false;
        }
        slider.setX(menu.getLeftPos() + menu.getMargin());
        slider.setY(menu.getLineYOffset(lineNumber));
        if (showLabel && slider.getLabel() != null) {
            Label label = Label.builder(menu.getFont(), slider.getLabel())
                    .x(menu.getLeftPos() + menu.getMargin())
                    .y(menu.getLineYOffset(lineNumber))
                    .build();
            widgets.add(label);
            if (!labelOnTop) {
                slider.setX(menu.getLeftPos() + menu.getMargin() + label.getWidth() + menu.getSpacer());
            }
            if (labelOnTop || showValues) {
                slider.setHeightScale(1.0d * label.getLineHeight() / slider.getHeight());
            }
            if (showValues) {
                label.setY(menu.getLineYOffset(lineNumber) + (slider.getHeight() / 2));
                adjustValueWidgets(false);
                widgets.addAll(valueWidgets.values());
            }
        }
        widgets.add(slider);
        return widgets;
    }

    @Override
    public int getLineWidth(Font font) {
        return 0;
    }

    private void setLabelOnTop(boolean labelOnTop) {
        this.labelOnTop = labelOnTop;
    }

    private void setShowValues(boolean showValues) {
        this.showValues = showValues;
    }

    private void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    private void addValueWidget(double value, AbstractWidget widget) {
        valueWidgets.put(value, widget);
    }

    public static class Builder {
        private final int lineNumber;
        private ThemedSlider slider;
        private boolean showLabel = true;
        private boolean showValues = true;
        private boolean labelOnTop = false;
        private final Map<Double, AbstractWidget> valueWidgets = new HashMap<>();

        public Builder(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        public Builder slider(ThemedSlider slider) {
            this.slider = slider;
            return this;
        }

        public Builder showLabel(boolean showLabel) {
            this.showLabel = showLabel;
            return this;
        }

        public Builder showValues(boolean showValues) {
            this.showValues = showValues;
            return this;
        }

        public Builder labelOnTop(boolean labelOnTop) {
            this.labelOnTop = labelOnTop;
            return this;
        }

        public Builder valueLabel(double value, Label label, Function<ThemedSlider, Component> valueSupplier) {
            label.setValueSupplier(() -> valueSupplier.apply(slider));
            valueWidgets.put(value, label);
            return this;
        }

        public Builder valueWidget(double value, AbstractWidget widget) {
            valueWidgets.put(value, widget);
            return this;
        }

        public Builder valueButton(double value, AbstractThemedButton button,
                                   BiConsumer<AbstractThemedButton, ThemedSlider> onPress) {
            button.setOnPress(widget -> onPress.accept(widget, slider));
            valueWidgets.put(value, button);
            return this;
        }

        public SliderEntry build() {
            SliderEntry entry = new SliderEntry(lineNumber, slider);
            entry.setShowLabel(showLabel);
            entry.setShowValues(showValues);
            entry.setLabelOnTop(labelOnTop);
            valueWidgets.forEach(entry::addValueWidget);
            return entry;
        }

        public Builder valueIcon(double value, Icon icon, Function<ThemedSlider, Icons> iconSupplier) {
            icon.setIconSupplier(() -> iconSupplier.apply(slider));
            valueWidgets.put(value, icon);
            return this;
        }
    }
}
