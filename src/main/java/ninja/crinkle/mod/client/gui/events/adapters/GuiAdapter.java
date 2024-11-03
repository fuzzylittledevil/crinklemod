package ninja.crinkle.mod.client.gui.events.adapters;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.Screen;
import ninja.crinkle.mod.client.gui.events.*;
import ninja.crinkle.mod.client.gui.events.listeners.InputListener;
import ninja.crinkle.mod.client.gui.events.listeners.MouseListener;
import ninja.crinkle.mod.client.gui.events.sources.FocusSource;
import ninja.crinkle.mod.client.gui.events.sources.InputSource;
import ninja.crinkle.mod.client.gui.events.sources.MouseSource;
import ninja.crinkle.mod.client.gui.events.sources.TabIndexSource;
import ninja.crinkle.mod.client.gui.layouts.LayoutWidget;
import ninja.crinkle.mod.client.gui.properties.Point;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Optional;

public class GuiAdapter implements GuiEventListener, InputSource, MouseListener, NarratableEntry, FocusSource {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final InputListener listener;
    private final Screen screen;

    public GuiAdapter(InputListener listener, Screen screen) {
        this.listener = listener;
        this.screen = screen;
        if (listener instanceof InputSource source) {
            source.addListener(this);
        }
    }

    private <T> Optional<T> as(Class<T> type) {
        return type.isInstance(listener()) ? Optional.of(type.cast(listener())) : Optional.empty();
    }

    @Override
    public boolean focused() {
        return as(FocusSource.class).map(FocusSource::focused).orElse(false);
    }

    @Override
    public int getTabOrderGroup() {
        return as(TabIndexSource.class).map(TabIndexSource::tabIndex).orElse(0);
    }

    public boolean hovered() {
        return as(MouseSource.class).map(MouseSource::hovered).orElse(false);
    }

    public boolean isClicked() {
        return as(MouseSource.class).map(MouseSource::clicked).orElse(false);
    }

    public boolean isHovered() {
        return hovered();
    }

    public Optional<LayoutWidget> layout() {
        return as(LayoutWidget.class);
    }

    public InputListener listener() {
        return listener;
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        dispatchEvent(new MoveEvent(pMouseX, pMouseY));
    }

    private boolean dispatchEvent(InputEvent event) {
        LOGGER.debug("Dispatching event: {}", event);
        listener().onInputEvent(event);
        return event.consumed() && !event.cancelled();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return dispatchEvent(new ClickEvent(pMouseX, pMouseY, pButton, false));
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        if (isDragged()) {
            dispatchEvent(new DragEvent(pMouseX, pMouseY, pButton, 0, 0, false));
        }
        return dispatchEvent(new ClickEvent(pMouseX, pMouseY, pButton, true));
    }

    public boolean isDragged() {
        return as(MouseSource.class).map(MouseSource::dragged).orElse(false);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        return dispatchEvent(new DragEvent(pMouseX, pMouseY, pButton, pDragX, pDragY, true));
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return dispatchEvent(new ScrollEvent(pMouseX, pMouseY, pDelta));
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return dispatchEvent(new KeyEvent(pKeyCode, pScanCode, pModifiers, false));
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        return dispatchEvent(new KeyEvent(pKeyCode, pScanCode, pModifiers, true));
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        return dispatchEvent(new CharTypedEvent(pCodePoint, pModifiers));
    }

    @Override
    public @Nullable ComponentPath nextFocusPath(@NotNull FocusNavigationEvent pEvent) {
        return screen.nextFocusPath(pEvent);
    }

    @Override
    public boolean isMouseOver(double pMouseX, double pMouseY) {
        return as(LayoutWidget.class)
                .map(widget -> widget.box().contains(new Point(pMouseX, pMouseY))).orElse(false);
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return focused() ? NarrationPriority.FOCUSED : hovered() ? NarrationPriority.HOVERED : NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
        LOGGER.debug("Updating narration: {}", pNarrationElementOutput);
        dispatchEvent(new NarrateEvent(pNarrationElementOutput));
    }

    @Override
    public void setFocused(boolean pFocused) {
        if (pFocused == focused())
            return;
        LOGGER.debug("Setting focused: {}", pFocused);
        dispatchEvent(new FocusEvent(pFocused));
    }


    @Override
    public boolean isFocused() {
        return focused();
    }

    @Override
    public @Nullable ComponentPath getCurrentFocusPath() {
        return screen.getCurrentFocusPath();
    }
}
