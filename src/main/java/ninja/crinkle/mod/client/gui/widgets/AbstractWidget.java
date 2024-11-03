package ninja.crinkle.mod.client.gui.widgets;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.gui.GenericBuilder;
import ninja.crinkle.mod.client.gui.events.*;
import ninja.crinkle.mod.client.gui.events.listeners.EventListener;
import ninja.crinkle.mod.client.gui.events.listeners.FocusListener;
import ninja.crinkle.mod.client.gui.events.listeners.MouseListener;
import ninja.crinkle.mod.client.gui.events.sources.FocusSource;
import ninja.crinkle.mod.client.gui.events.sources.MouseSource;
import ninja.crinkle.mod.client.gui.events.sources.TabIndexSource;
import ninja.crinkle.mod.client.gui.layouts.LayoutWidget;
import ninja.crinkle.mod.client.gui.layouts.PositionType;
import ninja.crinkle.mod.client.gui.properties.*;
import ninja.crinkle.mod.client.gui.renderers.ThemeGraphics;
import ninja.crinkle.mod.client.gui.textures.Texture;
import ninja.crinkle.mod.client.gui.textures.ThemeAtlas;
import ninja.crinkle.mod.client.gui.themes.ThemeRegistry;
import ninja.crinkle.mod.client.gui.themes.WidgetAppearance;
import ninja.crinkle.mod.client.gui.themes.WidgetTheme;
import ninja.crinkle.mod.config.ClientConfig;
import ninja.crinkle.mod.util.ClientUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractWidget implements BoxModel, Renderable, LayoutWidget, MouseSource,
        MouseListener, FocusSource, FocusListener, TabIndexSource {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final List<FocusListener> focusListeners = new ArrayList<>();
    private final List<MouseListener> mouseListeners = new ArrayList<>();
    private boolean active;
    private Predicate<AbstractWidget> activePredicate;
    private float alpha;
    private Border border;
    private Bounds bounds;
    private boolean clicked;
    private boolean draggable;
    private boolean dragged;
    private boolean focused;
    private boolean hovered;
    private Margin margin;
    private Component name;
    private Padding padding;
    private Point position;
    private PositionType positionType;
    private Point relativePosition;
    private WidgetState state;
    private int tabIndex;
    private boolean visible;
    private WidgetTheme widgetTheme;

    protected AbstractWidget(@NotNull AbstractBuilder<?> builder) {
        this.alpha = builder.alpha();
        this.active = builder.active();
        this.activePredicate = builder.activePredicate();
        this.border = builder.border();
        this.bounds = builder.bounds();
        this.clicked = false;
        this.draggable = builder.draggable();
        this.focused = false;
        this.hovered = false;
        this.margin = builder.margin();
        this.name = builder.name();
        this.padding = builder.padding();
        this.position = builder.position();
        this.positionType = builder.positionType();
        this.relativePosition = Point.ZERO;
        this.state = WidgetState.inactive;
        this.visible = builder.visible();
        this.widgetTheme = builder.widgetTheme();
        this.tabIndex = builder.tabIndex();
    }

    public void active(boolean active) {
        this.active = active;
    }

    public void active(Predicate<AbstractWidget> activePredicate) {
        this.activePredicate = activePredicate;
    }

    public boolean active() {
        if (activePredicate != null) {
            return activePredicate.test(this);
        }
        return active;
    }

    @Override
    public void addListener(EventListener listener) {
        if (listener instanceof FocusListener focusListener) {
            focusListeners.add(focusListener);
        }
        if (listener instanceof MouseListener mouseListener) {
            mouseListeners.add(mouseListener);
        }
    }

    @Override
    public void removeListener(EventListener listener) {
        if (listener instanceof FocusListener focusListener) {
            focusListeners.remove(focusListener);
        }
        if (listener instanceof MouseListener mouseListener) {
            mouseListeners.remove(mouseListener);
        }
    }

    public float alpha() {
        return alpha;
    }

    public void alpha(float alpha) {
        this.alpha = alpha;
    }

    public void draggable(boolean draggable) {
        this.draggable = draggable;
    }

    public Box getBorderTextureBox() {
        Bounds top = appearance().getBackgroundTexture().boundsOf(Texture.Slice.Location.top);
        Bounds topRight = appearance().getBackgroundTexture().boundsOf(Texture.Slice.Location.topRight);
        Bounds right = appearance().getBackgroundTexture().boundsOf(Texture.Slice.Location.right);
        Bounds bottomRight = appearance().getBackgroundTexture().boundsOf(Texture.Slice.Location.bottomRight);
        Bounds bottom = appearance().getBackgroundTexture().boundsOf(Texture.Slice.Location.bottom);
        Bounds bottomLeft = appearance().getBackgroundTexture().boundsOf(Texture.Slice.Location.bottomLeft);
        Bounds left = appearance().getBackgroundTexture().boundsOf(Texture.Slice.Location.left);
        Bounds topLeft = appearance().getBackgroundTexture().boundsOf(Texture.Slice.Location.topLeft);

        int width = bounds().width() - (topRight.max(right).max(bottomRight).width()
                + topLeft.max(left).max(bottomLeft).width());
        int height = bounds().height() - (topLeft.max(top).max(topRight).height()
                + bottomLeft.max(bottom).max(bottomRight).height());

        return new Box(position().add(topLeft.width(), topLeft.height()), new Bounds(width, height));
    }

    public WidgetAppearance appearance() {
        return Optional.ofNullable(widgetTheme.appearances().get(state())).orElse(WidgetAppearance.EMPTY);
    }

    @Override
    public Point position() {
        return positionType == PositionType.RELATIVE ? relativePosition : position;
    }

    public WidgetState state() {
        return this.state;
    }

    @Override
    public void position(Point position) {
        if (positionType == PositionType.RELATIVE) {
            this.relativePosition = position;
        } else {
            this.position = position;
        }
    }

    public Bounds bounds() {
        return bounds;
    }

    public void bounds(Bounds size) {
        this.bounds = size;
    }

    @Override
    public PositionType positionType() {
        return positionType;
    }

    @Override
    public void positionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public WidgetTheme getWidgetTheme() {
        return widgetTheme;
    }

    @Override
    public Margin margin() {
        return margin;
    }

    @Override
    public void margin(Margin margin) {
        this.margin = margin;
    }

    @Override
    public Padding padding() {
        return padding;
    }

    @Override
    public void padding(Padding padding) {
        this.padding = padding;
    }

    @Override
    public Border border() {
        return border;
    }

    @Override
    public void border(Border border) {
        this.border = border;
    }

    @Override
    public Box box() {
        return new Box(position(), bounds());
    }

    @Override
    public Box borderBox() {
        return box().subtract(-margin().left(), -margin().top(),
                margin().right() + margin().left(),
                margin().bottom() + margin().top());
    }

    @Override
    public Box contentBox() {
        return borderBox().subtract(-padding().left(), -padding().top(),
                padding().right() + padding().left(),
                padding().bottom() + padding().top());
    }

    @Override
    public abstract void renderContent(ThemeGraphics graphics, Point pMouse, Box pBox, float pPartialTick);

    @Override
    public void renderDebug(ThemeGraphics pGuiGraphics, Box pOuter, Box pBorder, Box pInner) {
        pGuiGraphics.drawBox(pOuter, Color.RED);
        pGuiGraphics.drawBox(pBorder, Color.GREEN);
        pGuiGraphics.drawBox(pInner, Color.BLUE);
    }

    public Component name() {
        return name;
    }

    public void name(Component name) {
        this.name = name;
    }

    @Override
    public void onClick(ClickEvent event) {
        LOGGER.debug("Click event: {}", event);
        if (event.cancelled()) return;
        if (box().contains(event.position())) {
            if (event.pressed() && event.isLeftButton()) {
                clicked(true);
                focused(true);
            } else if (event.released() && event.isLeftButton() && clicked()) {
                clicked(false);
            }
        } else if (clicked() && event.released() && event.isLeftButton()) {
            clicked(false);
        }
    }

    @Override
    public void onDrag(DragEvent event) {
        if (event.cancelled()) return;
        if (!dragged() && !box().contains(event.position()))
            return;
        dragged(!dragged() || event.dragged());
        if (draggable() && event.isLeftButton() && dragged()) {
            event.consumed(true);
            position(position().add(event.dragX(), event.dragY()));
        }
    }

    public void dragged(boolean dragged) {
        this.dragged = dragged;
    }

    public boolean draggable() {
        return draggable;
    }

    @Override
    public void onMove(MoveEvent event) {
        LOGGER.trace("Move event: {}", event);
        if (event.cancelled()) return;
        if (box().contains(event.position())) {
            hovered(true);
            event.consumed(true);
        } else {
            hovered(false);
        }
    }

    public void hovered(boolean hovered) {
        if (hovered != hovered()) {
            double x = ClientUtil.getMinecraft().mouseHandler.xpos();
            double y = ClientUtil.getMinecraft().mouseHandler.ypos();
            mouseListeners.forEach(listener -> listener.onHover(new HoverEvent(x, y, hovered)));
        }
        this.hovered = hovered;
    }

    public boolean hovered() {
        return hovered;
    }

    public boolean clicked() {
        return clicked;
    }

    @Override
    public boolean mouseOver(Point position) {
        return box().contains(position);
    }

    @Override
    public boolean dragged() {
        return dragged;
    }

    public void clicked(boolean clicked) {
        if (clicked != clicked()) dispatchEvent(new FocusEvent(clicked));
        this.clicked = clicked;
    }

    public void focused(boolean focused) {
        if (focused != focused())
            focusListeners.forEach(listener -> listener.onFocus(new FocusEvent(focused)));
        this.focused = focused;
    }

    public void dispatchEvent(InputEvent event) {
        LOGGER.debug("Dispatching event: {}", event);
        if (event instanceof MouseEvent mouseEvent) {
            mouseListeners.forEach(listener -> listener.onInputEvent(mouseEvent));
        } else if (event instanceof FocusEvent focusEvent) {
            focusListeners.forEach(listener -> listener.onInputEvent(focusEvent));
        } else {
            LOGGER.warn("Unhandled event type: {}", event);
        }
    }

    public boolean focused() {
        return focused;
    }

    @Override
    public void onFocus(FocusEvent event) {
        LOGGER.debug("Focus event: {}", event);
        if (event.cancelled()) return;
        if (event.focused() && !focused()) {
            focused(true);
            event.consumed(true);
        } else if (!event.focused() && focused()) {
            focused(false);
            event.consumed(true);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        ThemeGraphics graphics = new ThemeGraphics(pGuiGraphics, ThemeAtlas.getAtlas());
        updateState();
        if (!visible()) return;
        border().render(graphics, this.borderBox());
        if (!ClientConfig.debug())
            getWidgetTheme().render(graphics, borderBox(), this);
        renderContent(graphics, new Point(pMouseX, pMouseY), contentBox(), pPartialTick);
        if (ClientConfig.debug())
            renderDebug(graphics, box(), borderBox(), contentBox());
    }

    public void state(WidgetState state) {
        this.state = state;
    }

    public void updateState() {
        WidgetState newState = WidgetState.inactive;
        if (active()) {
            newState = WidgetState.active;
        }
        if (focused()) {
            newState = WidgetState.focused;
        }
        if (hovered()) {
            newState = WidgetState.hover;
        }
        if (clicked() || dragged()) {
            newState = WidgetState.pressed;
        }
        state(newState);
    }

    public boolean visible() {
        return visible;
    }

    public void visible(boolean visible) {
        this.visible = visible;
    }

    public void widgetTheme(WidgetTheme widgetTheme) {
        this.widgetTheme = widgetTheme;
    }

    public abstract static class AbstractBuilder<T extends AbstractBuilder<T>>
            extends GenericBuilder<T, AbstractWidget> {
        private boolean active = false;
        private Predicate<AbstractWidget> activePredicate;
        private float alpha = 1.0f;
        private Border border = Border.ZERO;
        private Bounds bounds = Bounds.ZERO;
        private boolean draggable = false;
        private Margin margin = Margin.ZERO;
        private Component name = Component.literal("Unnamed");
        private Padding padding = Padding.ZERO;
        private Point position = Point.ZERO;
        private PositionType positionType = PositionType.ABSOLUTE;
        private int tabIndex = 0;
        private boolean visible = true;
        private WidgetTheme widgetTheme = WidgetTheme.getDefault();

        public T active(boolean active) {
            this.active = active;
            return self();
        }

        public boolean active() {
            return active;
        }

        public T activePredicate(Predicate<AbstractWidget> activePredicate) {
            this.activePredicate = activePredicate;
            return self();
        }

        public Predicate<AbstractWidget> activePredicate() {
            return activePredicate;
        }

        public T alpha(float alpha) {
            this.alpha = alpha;
            return self();
        }

        public float alpha() {
            return alpha;
        }

        public T border(int all, Color color) {
            return border(new Border(all, color));
        }

        public T border(Border border) {
            this.border = border;
            return self();
        }

        public T border(int top, int right, int left, int bottom, Color color) {
            return border(new Border(top, right, bottom, left, color));
        }

        public T border(int horizontal, int vertical, Color color) {
            return border(new Border(horizontal, vertical, color));
        }

        public T border(int all) {
            return border(new Border(all, Color.of(0)));
        }

        public Border border() {
            return border;
        }

        public T bounds(int width, int height) {
            return bounds(new Bounds(width, height));
        }

        public T bounds(Bounds bounds) {
            this.bounds = bounds;
            return self();
        }

        public T bounds(int all) {
            return bounds(new Bounds(all, all));
        }

        public Bounds bounds() {
            return bounds;
        }

        public boolean draggable() {
            return draggable;
        }

        public T draggable(boolean draggable) {
            this.draggable = draggable;
            return self();
        }

        public Margin margin() {
            return margin;
        }

        public T margin(int all) {
            return margin(new Margin(all));
        }

        public T margin(Margin margin) {
            this.margin = margin;
            return self();
        }

        public T margin(int top, int right, int bottom, int left) {
            return margin(new Margin(top, right, bottom, left));
        }

        public T margin(int vertical, int horizontal) {
            return margin(new Margin(vertical, horizontal));
        }

        public Component name() {
            return name;
        }

        public T name(Component name) {
            this.name = name;
            return self();
        }

        public Padding padding() {
            return padding;
        }

        public T padding(int all) {
            return padding(new Padding(all));
        }

        public T padding(Padding padding) {
            this.padding = padding;
            return self();
        }

        public T padding(int vertical, int horizontal) {
            return padding(new Padding(vertical, horizontal));
        }

        public T padding(int top, int right, int bottom, int left) {
            return padding(new Padding(top, right, bottom, left));
        }

        public Point position() {
            return position;
        }

        public T position(int x, int y) {
            return position(new Point(x, y));
        }

        public T position(Point position) {
            this.position = position;
            return self();
        }

        public PositionType positionType() {
            return positionType;
        }

        public T positionType(PositionType positionType) {
            this.positionType = positionType;
            return self();
        }

        public int tabIndex() {
            return tabIndex;
        }

        public T tabIndex(int tabIndex) {
            this.tabIndex = tabIndex;
            return self();
        }

        public boolean visible() {
            return visible;
        }

        public T visible(boolean visible) {
            this.visible = visible;
            return self();
        }

        public WidgetTheme widgetTheme() {
            return widgetTheme;
        }

        public T widgetTheme(String widgetTheme) {
            return widgetTheme(ThemeRegistry.current().getWidgetTheme(widgetTheme));
        }

        public T widgetTheme(WidgetTheme widgetTheme) {
            this.widgetTheme = widgetTheme;
            return self();
        }
    }

    @Override
    public int tabIndex() {
        return tabIndex;
    }


    @Override
    public void tabIndex(int tabIndex) {
        if (tabIndex != tabIndex()) dispatchEvent(new TabIndexEvent(focused(), tabIndex(), tabIndex));
        this.tabIndex = tabIndex;
    }


}
