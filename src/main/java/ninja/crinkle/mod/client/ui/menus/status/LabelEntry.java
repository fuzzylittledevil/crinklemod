package ninja.crinkle.mod.client.ui.menus.status;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.ui.widgets.Label;

import java.util.List;
import java.util.function.Supplier;

public class LabelEntry implements IEntry {
    private final int lineNumber;
    private final Component text;
    private final Supplier<Component> textSupplier;
    private final Font font;
    private int color;

    public LabelEntry(int lineNumber, Component text, Supplier<Component> textSupplier, Font font, int color) {
        this.lineNumber = lineNumber;
        this.textSupplier = textSupplier;
        this.text = text;
        this.font = font;
        this.color = color;
    }

    @Override
    public List<AbstractWidget> create(StatusMenu menu) {
        Label.Builder builder = Label.builder(font, text);
        if (textSupplier != null) {
            builder = Label.builder(font, textSupplier);
        }
        Label label = builder
                .color(color)
                .pos(menu.getLeftPos() + menu.getMargin(),
                        menu.getTopPos() + menu.getLineYOffset(lineNumber))
                .dropShadow(false)
                .build();
        return List.of(label);
    }

    @Override
    public int getLineWidth(Font font) {
        return font.width(text);
    }

    public static Builder builder(Font font, Component value) {
        return new Builder(font, value);
    }

    public static Builder builder(Font font, Supplier<Component> valueSupplier) {
        return new Builder(font, valueSupplier);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public static class Builder {
        private int lineNumber;
        private final Component value;
        private final Supplier<Component> valueSupplier;
        private final Font font;
        private int color = Label.DEFAULT_COLOR;

        public Builder(Font font, Component value) {
            this.font = font;
            this.value = value;
            this.valueSupplier = null;
        }

        public Builder(Font font, Supplier<Component> valueSupplier) {
            this.font = font;
            this.value = Component.literal("Dynamic Label");
            this.valueSupplier = valueSupplier;
        }

        public Builder lineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
            return this;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public LabelEntry build() {
            return new LabelEntry(lineNumber, value, valueSupplier, font, color);
        }
    }
}
