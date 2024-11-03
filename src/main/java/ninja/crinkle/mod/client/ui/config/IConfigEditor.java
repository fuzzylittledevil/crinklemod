package ninja.crinkle.mod.client.ui.config;

import net.minecraft.client.gui.components.AbstractWidget;
import ninja.crinkle.mod.client.ui.widgets.Label;

import java.util.List;

public interface IConfigEditor<T> {
    void set(T value);
    T get();
    void reset();
    void save();
    boolean validate(T value);
    AbstractWidget getWidget();
    Label getLabel();
}
