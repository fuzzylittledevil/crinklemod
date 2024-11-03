package ninja.crinkle.mod.client.ui.config;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraftforge.common.ForgeConfigSpec;
import ninja.crinkle.mod.client.ui.widgets.Label;

public abstract class AbstractConfigEditor<T> implements IConfigEditor<T> {
    protected ForgeConfigSpec.ConfigValue<T> configValue;
    protected Label label;
    protected AbstractWidget widget;

    public AbstractConfigEditor(ForgeConfigSpec.ConfigValue<T> configValue, Label label, AbstractWidget widget) {
        this.configValue = configValue;
        this.label = label;
        this.widget = widget;
    }

    @Override
    public void set(T value) {
        configValue.set(value);
    }

    @Override
    public T get() {
        return configValue.get();
    }

    @Override
    public void reset() {
        configValue.set(configValue.getDefault());
    }

    @Override
    public void save() {
        configValue.save();
    }

    @Override
    public boolean validate(T value) {
        return true;
    }

    @Override
    public AbstractWidget getWidget() {
        return widget;
    }

    @Override
    public Label getLabel() {
        return label;
    }
}
