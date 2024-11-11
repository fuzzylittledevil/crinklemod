package ninja.crinkle.mod.client.gui.events.listeners;

import ninja.crinkle.mod.client.gui.events.FocusEnteredEvent;
import ninja.crinkle.mod.client.gui.events.FocusEvent;
import ninja.crinkle.mod.client.gui.events.FocusLeftEvent;

public interface FocusListener extends InputListener {
    default void onFocus(FocusEvent event) {}
    default void onFocusEntered(FocusEnteredEvent event) {}
    default void onFocusLeft(FocusLeftEvent event) {}
}
