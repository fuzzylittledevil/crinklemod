package ninja.crinkle.mod.client.gui.events.listeners;

import ninja.crinkle.mod.client.gui.events.FocusEvent;

public interface FocusListener extends InputListener {
    default void onFocus(FocusEvent event) {
        propagate(event);
    }
}
