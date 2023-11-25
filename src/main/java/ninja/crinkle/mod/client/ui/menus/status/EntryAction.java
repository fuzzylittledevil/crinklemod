package ninja.crinkle.mod.client.ui.menus.status;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import ninja.crinkle.mod.icons.Icons;
import ninja.crinkle.mod.settings.Setting;

import java.util.function.BiConsumer;

public class EntryAction {
    private final Icons icon;
    private final BiConsumer<Setting<?>, ICapabilityProvider> action;
    private final Tooltip tooltip;

    public EntryAction(Icons icon, BiConsumer<Setting<?>, ICapabilityProvider> action, Tooltip tooltip) {
        this.icon = icon;
        this.action = action;
        this.tooltip = tooltip;
    }

    public Icons getIcon() {
        return icon;
    }

    public BiConsumer<Setting<?>, ICapabilityProvider> getAction() {
        return action;
    }

    public Tooltip getTooltip() {
        return tooltip;
    }
}
