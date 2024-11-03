package ninja.crinkle.mod.client.ui.config;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraftforge.common.ForgeConfigSpec;
import ninja.crinkle.mod.client.ui.widgets.Label;

public class BooleanConfigEditor extends AbstractConfigEditor<Boolean> {
    public BooleanConfigEditor(ForgeConfigSpec.ConfigValue<Boolean> configValue, Label label, AbstractWidget widget) {
        super(configValue, label, widget);
    }
}
