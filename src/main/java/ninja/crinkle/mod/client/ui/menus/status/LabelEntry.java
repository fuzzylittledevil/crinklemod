package ninja.crinkle.mod.client.ui.menus.status;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.ui.widgets.Label;

import java.util.List;

public class LabelEntry implements IEntry {
    private final int lineNumber;
    private final Component text;
    private final Font font;
    private int color;

    public LabelEntry(int lineNumber, Component text, Font font, int color) {
        this.lineNumber = lineNumber;
        this.text = text;
        this.font = font;
        this.color = color;
    }

    @Override
    public List<AbstractWidget> create(StatusMenu menu) {
        Label label = Label.builder(font, text)
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

    public void setColor(int color) {
        this.color = color;
    }

    public static class Builder {
        private int lineNumber;
        private final Component value;
        private final Font font;
        private int color = Label.DEFAULT_COLOR;

        public Builder(Font font, Component value) {
            this.font = font;
            this.value = value;
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
            return new LabelEntry(lineNumber, value, font, color);
        }
    }
}
