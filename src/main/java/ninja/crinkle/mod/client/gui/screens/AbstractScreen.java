package ninja.crinkle.mod.client.gui.screens;

import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.gui.events.TabIndexEvent;
import ninja.crinkle.mod.client.gui.events.adapters.GuiAdapter;
import ninja.crinkle.mod.client.gui.events.listeners.InputListener;
import ninja.crinkle.mod.client.gui.events.listeners.TabIndexListener;
import ninja.crinkle.mod.client.gui.events.sources.TabIndexSource;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public abstract class AbstractScreen extends Screen implements TabIndexListener {
    private final List<GuiEventListener> focusedElements = new ArrayList<>();
    private boolean dragging = false;
    private int currentTabIndex = 0;

    protected AbstractScreen(Component pTitle) {
        super(pTitle);
    }

    protected void addListener(InputListener inputListener) {
        if (inputListener instanceof Renderable renderable) {
            this.addRenderableOnly(renderable);
        }
        GuiAdapter adapter = new GuiAdapter(inputListener, this);
        this.focusedElements.add(adapter);
        if (inputListener instanceof TabIndexSource source) {
            if (currentTabIndex == 0 || source.tabIndex() < currentTabIndex) {
                currentTabIndex = source.tabIndex();
            }
        }
        this.addWidget(adapter);
    }

    @Override
    public int getTabOrderGroup() {
        return super.getTabOrderGroup();
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        for (var widget : this.children()) {
            if (widget.keyPressed(pKeyCode, pScanCode, pModifiers)) {
                return true;
            }
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        return children().stream()
                .flatMap(widget -> adapterFor(widget).stream())
                .flatMap(adapter -> adapter.layout().stream()
                        .filter(layoutWidget -> layoutWidget.box().contains(pMouseX, pMouseY) || adapter.isClicked()
                                || adapter.isDragged())
                        .map(layoutWidget -> adapter.mouseReleased(pMouseX, pMouseY, pButton)))
                .anyMatch(result -> result) || super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        return children().stream()
                .flatMap(widget -> adapterFor(widget).stream())
                .flatMap(adapter -> adapter.layout().stream()
                        .filter(layoutWidget -> layoutWidget.box().contains(pMouseX, pMouseY) || adapter.isClicked())
                        .map(layoutWidget -> adapter.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY)))
                .anyMatch(result -> result) || super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public @Nullable ComponentPath getCurrentFocusPath() {
        return ComponentPath.leaf(focusedElements.get(currentTabIndex));
    }

    @Override
    public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent pEvent) {
        if (pEvent.getVerticalDirectionForInitialFocus() == ScreenDirection.DOWN) {
            currentTabIndex = (currentTabIndex + 1) % focusedElements.size();
        } else {
            currentTabIndex = (currentTabIndex - 1 + focusedElements.size()) % focusedElements.size();
        }
        return ComponentPath.leaf(focusedElements.get(currentTabIndex));
    }

    protected Optional<GuiAdapter> adapterFor(GuiEventListener listener) {
        if (listener instanceof GuiAdapter adapter) {
            return Optional.of(adapter);
        }
        return Optional.empty();
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
        children().forEach(widget -> adapterFor(widget).ifPresent(adapter -> {
            adapter.layout().ifPresent(layoutWidget -> {
                if (layoutWidget.box().contains(pMouseX, pMouseY) || adapter.isHovered()) {
                    adapter.mouseMoved(pMouseX, pMouseY);
                }
            });
        }));
    }

    @Override
    public void onTabIndexChanged(TabIndexEvent event) {
        this.focusedElements.sort(Comparator.comparingInt(GuiEventListener::getTabOrderGroup));
    }
}
