package ninja.crinkle.mod.client.ui.menus.status;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedCheckbox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CheckboxEntry implements IEntry {
    private final int lineNumber;
    private final ThemedCheckbox checkbox;

    public CheckboxEntry(int lineNumber, ThemedCheckbox checkbox) {
        this.lineNumber = lineNumber;
        this.checkbox = checkbox;
    }

    @Override
    public List<AbstractWidget> create(@NotNull StatusMenu menu) {
        List<AbstractWidget> widgets = new ArrayList<>();
        checkbox.setY(menu.getLineYOffset(lineNumber));
        if (checkbox.isShowLabel() && !checkbox.isLabelRight()) {
            checkbox.setX(menu.getMargin() + menu.getSpacer() + checkbox.getLabel().getWidth());
        } else {
            checkbox.setX(menu.getLineXOffset());
        }
        widgets.add(checkbox);
        return widgets;
    }

    @Override
    public int getLineWidth(Font font) {
        return 0;
    }

    public static Builder builder(int lineNumber) {
        return new Builder(lineNumber);
    }

    public static class Builder {
        private final int lineNumber;
        private ThemedCheckbox checkbox;
        public Builder(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        public Builder checkbox(ThemedCheckbox checkbox) {
            this.checkbox = checkbox;
            return this;
        }
        public CheckboxEntry build() {
            return new CheckboxEntry(lineNumber, checkbox);
        }
    }
}
