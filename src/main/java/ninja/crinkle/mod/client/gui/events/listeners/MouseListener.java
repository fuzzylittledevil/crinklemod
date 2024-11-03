package ninja.crinkle.mod.client.gui.events.listeners;

import ninja.crinkle.mod.client.gui.events.*;

public interface MouseListener extends InputListener {
    default void onClick(ClickEvent event) {
        propagate(event);
    }

    default void onDrag(DragEvent event) {
        propagate(event);
    }

    default void onHover(HoverEvent event) {
        propagate(event);
    }

    default void onScroll(ScrollEvent event) {
        propagate(event);
    }

    default void onMove(MoveEvent event) {
        propagate(event);
    }
}
