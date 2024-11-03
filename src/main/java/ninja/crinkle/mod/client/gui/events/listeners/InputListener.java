package ninja.crinkle.mod.client.gui.events.listeners;

import ninja.crinkle.mod.client.gui.events.InputEvent;
import org.jetbrains.annotations.NotNull;

public interface InputListener extends EventListener {
    default void propagate(@NotNull InputEvent event) {
        if (event.propagate()) {
            onInputEvent(event);
        }
    }
    default void onInputEvent(@NotNull InputEvent event) {
        if (event.propagate()) {
            onEvent(event);
        }
    };
}
