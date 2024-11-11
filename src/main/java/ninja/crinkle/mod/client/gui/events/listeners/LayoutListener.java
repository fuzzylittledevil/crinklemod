package ninja.crinkle.mod.client.gui.events.listeners;

import ninja.crinkle.mod.client.gui.events.LayoutChangedEvent;

public interface LayoutListener extends EventListener {
    void onLayoutChanged(LayoutChangedEvent event);
}
