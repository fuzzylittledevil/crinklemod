package ninja.crinkle.mod.client.gui.events.listeners;

import ninja.crinkle.mod.client.gui.events.*;

public interface MouseListener extends InputListener {
    default void onClick(ClickEvent event) {}

    default void onDrag(DragEvent event) {}

    default void onDragStarted(DragStartedEvent event) {}
    default void onDragStopped(DragStoppedEvent event) {}

    default void onDropped(DroppedEvent event) {}

    default void onHover(HoverEvent event) {}

    default void onMousePressed(MousePressedEvent event) {}

    default void onMouseReleased(MouseReleasedEvent event) {}

    default void onScroll(ScrollEvent event) {}

    default void onMove(MoveEvent event) {}
}
