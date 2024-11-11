package ninja.crinkle.mod.client.gui.events.listeners;

import ninja.crinkle.mod.client.gui.events.NarrateEvent;

public interface NarrateListener extends InputListener {
    default void onNarrateEvent(NarrateEvent event) {}
}
