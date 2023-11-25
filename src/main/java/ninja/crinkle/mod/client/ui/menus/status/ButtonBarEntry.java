package ninja.crinkle.mod.client.ui.menus.status;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;

import java.util.ArrayList;
import java.util.List;

public class ButtonBarEntry implements IEntry {
    private final int lineNumber;
    private final List<Button> buttons = new ArrayList<>();
    private final int buttonSpacing;

    public ButtonBarEntry(int lineNumber, List<Button> buttons, int buttonSpacing) {
        this.lineNumber = lineNumber;
        this.buttons.addAll(buttons);
        this.buttonSpacing = buttonSpacing;
    }

    @Override
    public List<AbstractWidget> create(StatusMenu menu) {
        List<AbstractWidget> widgets = new ArrayList<>();
        int offset = 0;
        for(Button button : buttons) {
            button.setY(menu.getTopPos() + menu.getLineYOffset(lineNumber));
            button.setX(menu.getLeftPos() + menu.getMargin() + offset);
            widgets.add(button);
            offset += button.getWidth() + buttonSpacing;
        }
        return widgets;
    }

    @Override
    public int getLineWidth(Font font) {
        // Return 0 to prevent this entry from being considered for line width calculations
        return 0;
    }

    public static Builder builder(int lineNumber) {
        return new Builder(lineNumber);
    }

    public static class Builder {
        private final int lineNumber;
        private final List<Button> buttons = new ArrayList<>();
        private int buttonSpacing = 4;

        public Builder(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        public Builder button(Button button) {
            buttons.add(button);
            return this;
        }

        public Builder buttonSpacing(int buttonSpacing) {
            this.buttonSpacing = buttonSpacing;
            return this;
        }

        public ButtonBarEntry build() {
            return new ButtonBarEntry(lineNumber, buttons, buttonSpacing);
        }
    }
}
