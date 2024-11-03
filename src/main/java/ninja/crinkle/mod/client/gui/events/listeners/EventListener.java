package ninja.crinkle.mod.client.gui.events.listeners;

import ninja.crinkle.mod.client.gui.events.*;

public interface EventListener {
    default void onEvent(Event event) {
        if (!event.propagate() && event.dispatched())
            return;
        // If the event has already been dispatched, don't dispatch it again.
        if (event.dispatched())
            event.propagate(false);
        event.dispatched(true);
        switch (event.getType()) {
            case Click -> {
                if (this instanceof MouseListener listener)
                    listener.onClick((ClickEvent) event);
            }
            case Move -> {
                if (this instanceof MouseListener listener)
                    listener.onMove((MoveEvent) event);
            }
            case Key -> {
                if (this instanceof KeyListener listener)
                    listener.onKey((KeyEvent) event);
            }
            case CharTyped -> {
                if (this instanceof KeyListener listener)
                    listener.onCharTyped((CharTypedEvent) event);
            }
            case Scroll -> {
                if (this instanceof MouseListener listener)
                    listener.onScroll((ScrollEvent) event);
            }
            case Drag -> {
                if (this instanceof MouseListener listener)
                    listener.onDrag((DragEvent) event);
            }
            case Hover -> {
                if (this instanceof MouseListener listener)
                    listener.onHover((HoverEvent) event);
            }
            case Focus -> {
                if (this instanceof FocusListener listener)
                    listener.onFocus((FocusEvent) event);
            }
            case Narrate -> {
                if (this instanceof NarrateListener listener)
                    listener.onNarrateEvent((NarrateEvent) event);
            }
            case TabOrder -> {
                if (this instanceof TabIndexListener listener)
                    listener.onTabIndexChanged((TabIndexEvent) event);
            }
            default -> throw new IllegalStateException("Unexpected event type: " + event.getType());
        }
    }
}
