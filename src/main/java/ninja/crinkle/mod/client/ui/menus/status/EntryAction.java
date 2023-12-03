package ninja.crinkle.mod.client.ui.menus.status;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import ninja.crinkle.mod.client.icons.Icons;
import ninja.crinkle.mod.client.ui.widgets.themes.AbstractThemedButton;
import ninja.crinkle.mod.settings.Setting;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public record EntryAction(Icons icon, BiConsumer<Setting<?>, ICapabilityProvider> action,
                          Predicate<AbstractThemedButton> activePredicate, Tooltip tooltip) {
}
