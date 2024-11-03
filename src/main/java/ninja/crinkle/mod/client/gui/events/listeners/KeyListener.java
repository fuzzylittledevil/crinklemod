package ninja.crinkle.mod.client.gui.events.listeners;

import ninja.crinkle.mod.client.gui.events.CharTypedEvent;
import ninja.crinkle.mod.client.gui.events.KeyEvent;
import org.jetbrains.annotations.NotNull;

public interface KeyListener extends InputListener {
    default void onKey(KeyEvent event) {
        propagate(event);
    }
    default void onCharTyped(@NotNull CharTypedEvent event) {
        propagate(event);
    }
}
