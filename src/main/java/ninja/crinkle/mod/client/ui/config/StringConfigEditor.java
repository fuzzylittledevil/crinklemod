package ninja.crinkle.mod.client.ui.config;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraftforge.common.ForgeConfigSpec;
import ninja.crinkle.mod.client.ui.widgets.Label;

public class StringConfigEditor extends AbstractConfigEditor<String> {
    public StringConfigEditor(ForgeConfigSpec.ConfigValue<String> configValue, Label label, AbstractWidget widget) {
        super(configValue, label, widget);
    }
}
