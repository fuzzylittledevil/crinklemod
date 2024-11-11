package ninja.crinkle.mod.client.gui.events;

import ninja.crinkle.mod.client.gui.events.sources.EventSource;
import ninja.crinkle.mod.client.gui.properties.Scope;

public class KeyEvent extends InputEvent {
    private final int keyCode;
    private final int modifiers;
    private final boolean released;
    private final int scanCode;

    public KeyEvent(Type type, Scope scope, EventSource source, int modifiers) {
        this(type, scope, source, java.awt.event.KeyEvent.VK_UNDEFINED, 0, modifiers, false);
    }

    public KeyEvent(Type type, Scope scope, EventSource source, int keyCode, int scanCode, int modifiers, boolean released) {
        super(type, scope, source);
        this.keyCode = keyCode;
        this.scanCode = scanCode;
        this.modifiers = modifiers;
        this.released = released;
    }

    public KeyEvent(Scope scope, EventSource source, int keyCode, int scanCode, int modifiers, boolean released) {
        super(Type.Key, scope, source);
        this.keyCode = keyCode;
        this.scanCode = scanCode;
        this.modifiers = modifiers;
        this.released = released;
    }

    public boolean isAltDown() {
        return java.awt.event.InputEvent.ALT_DOWN_MASK == (modifiers & java.awt.event.InputEvent.ALT_DOWN_MASK);
    }

    public boolean isAltGraphDown() {
        return java.awt.event.InputEvent.ALT_GRAPH_DOWN_MASK == (modifiers & java.awt.event.InputEvent.ALT_GRAPH_DOWN_MASK);
    }

    public boolean isControlDown() {
        return java.awt.event.InputEvent.CTRL_DOWN_MASK == (modifiers & java.awt.event.InputEvent.CTRL_DOWN_MASK);
    }

    public boolean isMetaDown() {
        return java.awt.event.InputEvent.META_DOWN_MASK == (modifiers & java.awt.event.InputEvent.META_DOWN_MASK);
    }

    public boolean isShiftDown() {
        return java.awt.event.InputEvent.SHIFT_DOWN_MASK == (modifiers & java.awt.event.InputEvent.SHIFT_DOWN_MASK);
    }

    public int keyCode() {
        return keyCode;
    }

    public int modifiers() {
        return modifiers;
    }

    public boolean released() {
        return released;
    }

    public int scanCode() {
        return scanCode;
    }

    @Override
    public String toString() {
        return "KeyEvent{" +
                "keyCode=" + keyCode +
                ", scanCode=" + scanCode +
                ", modifiers=" + modifiers +
                ", released=" + released +
                ", " + super.toString() +
                '}';
    }
}
