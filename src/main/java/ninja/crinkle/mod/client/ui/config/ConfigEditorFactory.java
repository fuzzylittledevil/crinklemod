package ninja.crinkle.mod.client.ui.config;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraftforge.common.ForgeConfigSpec;
import ninja.crinkle.mod.client.ui.widgets.Label;

public class ConfigEditorFactory {
    @SuppressWarnings("unchecked")
    public static IConfigEditor<?> create(ForgeConfigSpec.ConfigValue<?> configValue, Label label, AbstractWidget widget) {
        Object value = configValue.get();
        if (value instanceof Integer) {
            return new IntegerConfigEditor((ForgeConfigSpec.ConfigValue<Integer>) configValue, label, widget);
        } else if (value instanceof Enum) {
            return new EnumConfigEditor((ForgeConfigSpec.ConfigValue<Enum<?>>) configValue, label, widget);
        } else if (value instanceof Boolean) {
            return new BooleanConfigEditor((ForgeConfigSpec.ConfigValue<Boolean>) configValue, label, widget);
        } else if (value instanceof String) {
            return new StringConfigEditor((ForgeConfigSpec.ConfigValue<String>) configValue, label, widget);
        } else {
            throw new IllegalArgumentException("Unsupported config value type: " + value.getClass().getName());
        }
    }
}
