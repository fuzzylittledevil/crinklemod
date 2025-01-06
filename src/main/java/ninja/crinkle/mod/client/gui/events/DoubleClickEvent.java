package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.listeners.EventListener;
import ninja.crinkle.mod.client.gui.properties.Scope;
import ninja.crinkle.mod.client.gui.screens.AbstractScreen;

import java.util.List;

public class DoubleClickEvent extends MouseEvent implements Event {
    private final List<EventListener> listeners;
    public DoubleClickEvent(Scope scope, AbstractScreen abstractScreen, double pMouseX, double pMouseY, int pButton,
                            List<EventListener> listeners) {
        super(Type.DoubleClick, scope, abstractScreen, pMouseX, pMouseY, pButton);
        this.listeners = listeners;
    }

    public List<EventListener> listeners() {
        return listeners;
    }
}
