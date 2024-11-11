package ninja.crinkle.mod.client.gui.managers;

import ninja.crinkle.mod.client.gui.events.FocusEnteredEvent;
import ninja.crinkle.mod.client.gui.events.FocusLeftEvent;
import ninja.crinkle.mod.client.gui.events.listeners.FocusListener;
import ninja.crinkle.mod.client.gui.events.sources.FocusSource;

public class FocusManager implements FocusListener {
    private FocusSource currentFocus;

    public void currentFocus(FocusSource currentFocus) {
        this.currentFocus = currentFocus;
    }

    public FocusSource currentFocus() {
        return currentFocus;
    }

    @Override
    public String name() {
        return FocusManager.class.getSimpleName();
    }

    @Override
    public void onFocusEntered(FocusEnteredEvent event) {
        if (currentFocus != null) {
            currentFocus.focused(false);
        }
        currentFocus = event.focusSource();
        // This should be a no-op if the event originated from the current focus
        currentFocus.focused(true);
        // Consume the event so that the focus doesn't change again
        event.consumer(this);
    }

    @Override
    public void onFocusLeft(FocusLeftEvent event) {
        if (currentFocus == event.focusSource()) {
            currentFocus.focused(false);
            currentFocus = null;
            event.consumer(this);
        }
    }
}
