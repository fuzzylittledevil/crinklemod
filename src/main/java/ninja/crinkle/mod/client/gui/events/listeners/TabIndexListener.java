package ninja.crinkle.mod.client.gui.events.listeners;

import ninja.crinkle.mod.client.gui.events.TabIndexEvent;

public interface TabIndexListener {
    default void onTabIndexChanged(TabIndexEvent event) {}
}
