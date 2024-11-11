package ninja.crinkle.mod.client.gui.widgets;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.gui.builders.GenericBuilder;
import ninja.crinkle.mod.client.gui.events.*;
import ninja.crinkle.mod.client.gui.events.listeners.EventListener;
import ninja.crinkle.mod.client.gui.events.listeners.FocusListener;
import ninja.crinkle.mod.client.gui.events.listeners.MouseListener;
import ninja.crinkle.mod.client.gui.events.sources.FocusSource;
import ninja.crinkle.mod.client.gui.events.sources.MouseSource;
import ninja.crinkle.mod.client.gui.events.sources.TabIndexSource;
import ninja.crinkle.mod.client.gui.layouts.BoxModel;
import ninja.crinkle.mod.client.gui.layouts.Layout;
import ninja.crinkle.mod.client.gui.managers.DragManager;
import ninja.crinkle.mod.client.gui.managers.EventManager;
import ninja.crinkle.mod.client.gui.managers.GuiManager;
import ninja.crinkle.mod.client.gui.properties.*;
import ninja.crinkle.mod.client.gui.renderers.ThemeGraphics;
import ninja.crinkle.mod.client.gui.states.WidgetBehavior;
import ninja.crinkle.mod.client.gui.states.WidgetDisplay;
import ninja.crinkle.mod.client.gui.states.WidgetLayout;
import ninja.crinkle.mod.client.gui.states.references.ValueRef;
import ninja.crinkle.mod.client.gui.textures.Texture;
import ninja.crinkle.mod.client.gui.textures.ThemeAtlas;
import ninja.crinkle.mod.client.gui.themes.ThemeRegistry;
import ninja.crinkle.mod.client.gui.themes.WidgetAppearance;
import ninja.crinkle.mod.client.gui.themes.WidgetTheme;
import ninja.crinkle.mod.config.ClientConfig;
import ninja.crinkle.mod.util.ClientUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractWidget implements BoxModel, Renderable, Layout.Widget, MouseSource, MouseListener, FocusSource, FocusListener, TabIndexSource {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final ValueRef<WidgetBehavior> behavior;
    private final ValueRef<WidgetDisplay> display;
    private final ValueRef<WidgetLayout> layout;
    private final GuiManager manager;
    private final Set<Scope> scopes = new HashSet<>();
    private Predicate<AbstractWidget> activePredicate;
    private String name;
    private AbstractContainer parent;
    private int priority;
    private int tabIndex;
    private WidgetTheme widgetTheme;

    protected AbstractWidget(@NotNull AbstractBuilder<?> builder) {
        this.activePredicate = builder.activePredicate();
        this.name = builder.name();
        this.scopes.addAll(builder.scopes());
        this.widgetTheme = builder.widgetTheme();
        this.tabIndex = builder.tabIndex();
        this.manager = builder.manager();
        this.priority = builder.priority();
        this.parent = builder.parent();
        this.display = builder.displayRef();
        this.behavior = builder.behaviorRef();
        this.layout = builder.layoutRef();
        layout().widget(this);

        if (builder.zIndex() == DragManager.Z_MIN && manager().root() != null) {
            display(display().withZIndex(manager().root().display().zIndex() + 1));
        }
    }

    public WidgetLayout layout() {
        WidgetLayout l = layout.get();
        l.widget(this);
        return l;
    }

    public WidgetDisplay display() {
        return display.get();
    }

    public Border textureBorder() {
        Size top = appearance().getBackgroundTexture().boundsOf(Texture.Slice.Location.top);
        Size right = appearance().getBackgroundTexture().boundsOf(Texture.Slice.Location.right);
        Size bottom = appearance().getBackgroundTexture().boundsOf(Texture.Slice.Location.bottom);
        Size left = appearance().getBackgroundTexture().boundsOf(Texture.Slice.Location.left);
        return new Border(top.height(), right.width(), bottom.height(), left.width(), Color.BLACK);
    }

    public WidgetAppearance appearance() {
        return Optional.ofNullable(widgetTheme.appearances().get(status())).orElse(WidgetAppearance.getDefault(widgetTheme.id()));
    }

    public Status status() {
        return display().status();
    }

    public Position position() {
        return layout().position();
    }

    public void position(Position position) {
        layout(layout().withPosition(position));
    }

    public Size size() {
        return layout().size();
    }

    public void size(Size size) {
        layout(layout().withSize(size));
    }

    public String name() {
        return name;
    }

    public void layout(WidgetLayout layout) {
        this.layout.set(layout);
    }

    public AbstractContainer parent() {
        return parent;
    }

    public AbstractWidget() {
        this.activePredicate = null;
        this.name = "Unnamed_" + hashCode();
        this.scopes.addAll(List.of(Scope.Local, Scope.Screen));
        this.widgetTheme = WidgetTheme.getDefault();
        this.tabIndex = 0;
        this.manager = GuiManager.create();
        this.priority = 1;
        this.parent = null;
        this.display = null;
        this.behavior = null;
        this.layout = null;
    }

    public static int zIndexOf(AbstractWidget widget) {
        return widget.zIndex();
    }

    public int zIndex() {
        if (dragged()) return DragManager.Z_MAX;
        return display().zIndex();
    }

    public WidgetBehavior behavior() {
        return behavior.get();
    }

    public void active(boolean active) {
        behavior(behavior().withActive(active));
    }

    public void behavior(WidgetBehavior behavior) {
        this.behavior.set(behavior);
    }

    public void active(Predicate<AbstractWidget> activePredicate) {
        this.activePredicate = activePredicate;
    }

    @Override
    public void resetPosition() {
        layout(layout().withPosition(layout.defaultValue().position()));
    }

    public void resetSize() {
        layout(layout().withSize(layout.defaultValue().size()));
    }

    public void alpha(float alpha) {
        display(display().withAlpha(alpha));
    }

    public void display(WidgetDisplay display) {
        this.display.set(display);
    }

    public ValueRef<WidgetBehavior> behaviorRef() {
        return behavior;
    }

    public ValueRef<WidgetDisplay> displayRef() {
        return display;
    }

    public void draggable(boolean draggable) {
        behavior(behavior().withDraggable(draggable));
    }

    @Override
    public Optional<EventManager> eventManager(Scope scope) {
        return switch (scope) {
            case Screen -> manager().eventManager();
            case Local -> eventManager();
            case Global -> Optional.ofNullable(EventManager.global());
        };
    }

    public GuiManager manager() {
        return manager;
    }

    public WidgetTheme widgetTheme() {
        return widgetTheme;
    }

    public void hovered(boolean hovered) {
        if (hovered != hovered()) {
            double x = ClientUtil.getMinecraft().mouseHandler.xpos();
            double y = ClientUtil.getMinecraft().mouseHandler.ypos();
            dispatchEvent(new HoverEvent(Scope.Local, this, x, y, hovered));
        }
        behavior(behavior().withHovered(hovered));
    }

    protected void init() {}

    public ValueRef<WidgetLayout> layoutRef() {
        return layout;
    }

    public void name(String name) {
        this.name = name;
    }

    @Override
    public void onDrag(DragEvent event) {
        if (event.cancelled()) return;
        if (!dragged()) return;
        Position newPosition = position().offsetBy(event.dragX(), event.dragY());
        if (newPosition.equals(position())) return;
        layout(layout().withPosition(newPosition));
        event.consumer(this);
    }

    @Override
    public void onDragStarted(DragStartedEvent event) {
        if (event.cancelled()) return;
        if (event.widget() == this) {
            dragged(true);
            event.consumer(this);
        }
    }

    @Override
    public void onDropped(DroppedEvent event) {
        if (event.cancelled()) return;
        if (event.widget() == this) {
            dragged(false);
            pressed(false);
            AbstractWidget topMost = event.listeners().stream().filter(listener -> listener instanceof AbstractWidget && listener != this).map(listener -> (AbstractWidget) listener).max(Comparator.comparingInt(AbstractWidget::zIndex)).orElse(null);
            if (topMost != null && topMost != this) {
                int newZ = topMost.zIndex() + DragManager.Z_STEP;
                int newPriority = topMost.priority() + DragManager.PRIORITY_STEP;
                zIndex(newZ);
                priority(newPriority);
            }
            event.consumer(this);
        }
    }

    @Override
    public void onMousePressed(MousePressedEvent event) {
        if (event.cancelled()) return;
        if (mouseOver(event.position())) {
            if (event.isLeftButton()) {
                AbstractWidget topMost = (AbstractWidget) event.listeners().stream().max(Comparator.comparingInt(EventListener::priority)).filter(listener -> listener instanceof AbstractWidget).orElse(null);
                if (topMost == this) {
                    pressed(true);
                    focused(true);
                    if (draggable()) dragged(true);
                    event.consumer(this);
                }
            }
        } else if (pressed()) {
            pressed(false);
        }
    }

    @Override
    public void onMouseReleased(MouseReleasedEvent event) {
        if (event.cancelled()) return;
        if (pressed()) {
            pressed(false);
        }
    }

    @Override
    public void onMove(MoveEvent event) {
        if (event.cancelled()) return;
        AbstractWidget topMost = (AbstractWidget) event.listeners().stream().max(Comparator.comparingInt(EventListener::priority)).filter(listener -> listener instanceof AbstractWidget).orElse(null);
        if (topMost != null && topMost != this) {
            hovered(false);
            return;
        }

        if (hovered()) {
            if (!mouseOver(event.position())) {
                hovered(false);
            }
        } else if (mouseOver(event.position())) {
            hovered(true);
        }
    }

    public void pressed(boolean pressed) {
        behavior(behavior().withPressed(pressed));
    }

    public boolean draggable() {
        return behavior().draggable();
    }

    public boolean focused() {
        return behavior().focused();
    }

    public void dispatchEvent(AbstractEvent event) {
        switch (event.scope()) {
            case Screen -> manager.eventManager().ifPresent(manager -> manager.dispatchEvent(event));
            case Local -> eventManager().ifPresent(manager -> manager.dispatchEvent(event));
            case Global -> EventManager.global().dispatchEvent(event);
        }
    }

    public void focused(boolean focused) {
        if (focused != focused()) {
            if (focused) {
                dispatchEvent(new FocusEnteredEvent(Scope.Local, this, true, this));
            } else {
                dispatchEvent(new FocusLeftEvent(Scope.Local, this, false, this));
            }
        }
        behavior(behavior().withFocused(focused));
    }

    public void dragged(boolean dragged) {
        if (dragged == dragged()) return;
        behavior(behavior().withDragged(dragged));
    }

    @Override
    public void onFocus(FocusEvent event) {
        LOGGER.debug("Focus event: {}", event);
        if (event.cancelled()) return;
        if (event.focused() && !focused()) {
            focused(true);
            event.consumer(this);
        } else if (!event.focused() && focused()) {
            focused(false);
            event.consumer(this);
        }
    }

    public void parent(AbstractContainer parent) {
        this.parent = parent;
    }

    public int priority() {
        return priority;
    }

    public void priority(int priority) {
        this.priority = priority;
    }

    /**
     * @param graphics     the GuiGraphics object used for rendering.
     * @param pMouseX      the x-coordinate of the mouse cursor.
     * @param pMouseY      the y-coordinate of the mouse cursor.
     * @param pPartialTick the partial tick time.
     * @deprecated Use {@link #render(ThemeGraphics, Point, float)} instead.
     */
    @Deprecated
    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        render(new ThemeGraphics(graphics, ThemeAtlas.getAtlas()), new MutablePoint(pMouseX, pMouseY), pPartialTick);
    }

    public void render(@NotNull ThemeGraphics graphics, Point pMouse, float pPartialTick) {
        updateState();
        if (!visible()) return;
        Box borderBox = layout().boxes().rendered().borderBox();
        border().render(graphics, borderBox, zIndex() - 10);
        if (!ClientConfig.debug()) widgetTheme().render(graphics, borderBox, this);
        renderContent(graphics, pMouse, layout().boxes().rendered().contentBox(), pPartialTick);
        if (ClientConfig.debug()) {
            renderDebug(graphics);
        }
    }

    protected void resize(Size size) {
        layout(layout().withSize(size));
    }

    public List<Scope> scopes() {
        return scopes.stream().toList();
    }

    public Box screenPositionOf(Box box) {
        assert box != null && box.position().relative();
        return box.add(renderedPosition());
    }

    public void status(Status status) {
        this.display.set(display.get().withStatus(status));
    }

    protected void tick() {}

    @Override
    public String toString() {
        return "AbstractWidget{" + "active=" + active() + ", activePredicate=" + activePredicate + ", alpha=" + alpha() + ", border=" + border() + ", size=" + size() + ", clicked=" + pressed() + ", draggable=" + draggable() + ", dragged=" + dragged() + ", focused=" + focused() + ", hovered=" + hovered() + ", margin=" + margin() + ", name='" + name() + "'" + ", padding=" + padding() + ", position=" + position() + ", status=" + status() + ", tabIndex=" + tabIndex() + ", visible=" + visible() + ", widgetTheme=" + widgetTheme + ", zIndex=" + zIndex() + '}';
    }

    public boolean active() {
        if (activePredicate != null) {
            return activePredicate.test(this);
        }
        return behavior().active();
    }

    public float alpha() {
        return display().alpha();
    }

    public boolean hovered() {
        return behavior().hovered();
    }

    @Override
    public Margin margin() {
        return layout().margin();
    }

    @Override
    public void margin(Margin margin) {
        layout(layout().withMargin(margin));
    }

    @Override
    public Padding padding() {
        return layout().padding();
    }

    @Override
    public void padding(Padding padding) {
        layout(layout().withPadding(padding));
    }

    @Override
    public Border border() {
        return layout().border();
    }

    @Override
    public void border(Border border) {
        layout(layout().withBorder(border));
    }

    @Override
    public Position renderedPosition() {
        Position position = layout().position();
        if (position.absolute()) return position;
        return position.withBase(parent().layout().boxes().rendered().contentBox().position().point())
                .withType(Position.Type.Absolute);
    }

    @Override
    public Position parentPosition() {
        return Optional.ofNullable(parent())
                .map(AbstractWidget::renderedPosition)
                .orElse(Position.absolute(0, 0));
    }

    @Override
    public int totalHeight() {
        return this.layout().size().height();
    }

    @Override
    public int totalWidth() {
        return this.layout().size().width();
    }

    @Override
    public abstract void renderContent(ThemeGraphics graphics, Point pMouse, Box renderedBox, float pPartialTick);

    @Override
    public void renderDebug(ThemeGraphics pGuiGraphics) {
        if (parent() == null) return;
        Box pOuter = screenPositionOf(layout().boxes().box());
        Box pBorder = screenPositionOf(layout().boxes().borderBox());
        Box pPadding = screenPositionOf(layout().boxes().paddingBox());
        Box pInner = screenPositionOf(layout().boxes().contentBox());
        // Render an outline around the widget
        pGuiGraphics.drawBox(pOuter.subtract(1, 1, -2, -2), Color.WHITE, zIndex());
        pGuiGraphics.drawBox(pOuter, Color.RED, zIndex());
        pGuiGraphics.drawBox(pBorder, Color.GREEN, zIndex());
        pGuiGraphics.drawBox(pPadding, Color.YELLOW, zIndex());
        pGuiGraphics.drawBox(pInner, Color.BLUE, zIndex());
    }

    @Override
    public int tabIndex() {
        return tabIndex;
    }

    public boolean visible() {
        return this.display.get().visible();
    }

    @Override
    public void tabIndex(int tabIndex) {
        if (tabIndex != tabIndex())
            dispatchEvent(new TabIndexEvent(Scope.Screen, this, focused(), tabIndex(), tabIndex, this));
        this.tabIndex = tabIndex;
    }

    public boolean pressed() {
        return behavior().pressed();
    }

    @Override
    public boolean mouseOver(Point position) {
        return layout().boxes().borderBox().contains(position);
    }

    @Override
    public boolean dragged() {
        return behavior().dragged();
    }

    public void updateState() {
        Status newStatus = Status.inactive;
        if (active()) {
            newStatus = Status.active;
        }
        if (focused()) {
            newStatus = Status.focused;
        }
        if (hovered()) {
            newStatus = Status.hover;
        }
        if (pressed() || dragged()) {
            newStatus = Status.pressed;
        }
        status(newStatus);
    }

    public void visible(boolean visible) {
        this.display.set(display.get().withVisible(visible));
    }

    public void widgetTheme(WidgetTheme widgetTheme) {
        this.widgetTheme = widgetTheme;
    }

    public void zIndex(int zIndex) {
        display(display().withZIndex(zIndex));
    }

    @SuppressWarnings("unused")
    public abstract static class AbstractBuilder<T extends AbstractBuilder<T>> extends GenericBuilder<T, AbstractWidget> {
        private final GuiManager manager;
        private final AbstractContainer parent;
        private Predicate<AbstractWidget> activePredicate;
        private WidgetBehavior behavior = new WidgetBehavior();
        private WidgetDisplay display = new WidgetDisplay();
        private WidgetLayout layout = new WidgetLayout();
        private String name = "Unnamed_" + hashCode();
        private int priority = 1;
        private List<Scope> scopes = List.of(Scope.Local, Scope.Screen);
        private int tabIndex = 0;
        private boolean visible = true;
        private WidgetTheme widgetTheme = WidgetTheme.getDefault();

        protected AbstractBuilder(GuiManager manager) {
            this.manager = manager;
            this.parent = manager.root();
        }

        protected AbstractBuilder(GuiManager manager, AbstractContainer parent) {
            this.manager = manager;
            this.parent = parent;
        }

        protected AbstractBuilder(AbstractContainer parent) {
            if (parent == null || parent.manager() == null) {
                throw new IllegalArgumentException("Parent or GUI manager cannot be null");
            }
            this.manager = parent.manager();
            this.parent = parent;
        }

        public T absolute(int x, int y) {
            return absolute(new ImmutablePoint(x, y));
        }

        public T absolute(ImmutablePoint position) {
            layout = layout.withPosition(Position.absolute(position));
            return self();
        }

        public T active(boolean active) {
            behavior = behavior.withActive(active);
            return self();
        }

        public boolean active() {
            return behavior.active();
        }

        public T activePredicate(Predicate<AbstractWidget> activePredicate) {
            this.activePredicate = activePredicate;
            return self();
        }

        public Predicate<AbstractWidget> activePredicate() {
            return activePredicate;
        }

        public T alpha(float alpha) {
            display = display.withAlpha(alpha);
            return self();
        }

        public float alpha() {
            return display.alpha();
        }

        public ValueRef<WidgetBehavior> behaviorRef() {
            return manager().stateStorage().createValue(WidgetBehavior.class, behavior);
        }

        public GuiManager manager() {
            return manager;
        }

        public T border(int all, Color color) {
            return border(new Border(all, color));
        }

        public T border(Border border) {
            layout = layout.withBorder(border);
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
            return layout.border();
        }

        public abstract AbstractWidget build();

        public ValueRef<WidgetDisplay> displayRef() {
            return manager().stateStorage().createValue(WidgetDisplay.class, display);
        }

        public boolean draggable() {
            return behavior.draggable();
        }

        public T draggable(boolean draggable) {
            behavior = behavior.withDraggable(draggable);
            return self();
        }

        public ValueRef<WidgetLayout> layoutRef() {
            return manager().stateStorage().createValue(WidgetLayout.class, layout);
        }

        public Margin margin() {
            return layout.margin();
        }

        public T margin(int all) {
            return margin(new Margin(all));
        }

        public T margin(Margin margin) {
            layout = layout.withMargin(margin);
            return self();
        }

        public T margin(int top, int right, int bottom, int left) {
            return margin(new Margin(top, right, bottom, left));
        }

        public T margin(int vertical, int horizontal) {
            return margin(new Margin(vertical, horizontal));
        }

        public String name() {
            return name;
        }

        public T name(String name) {
            this.name = name;
            return self();
        }

        public Padding padding() {
            return layout.padding();
        }

        public T padding(int all) {
            return padding(new Padding(all));
        }

        public T padding(Padding padding) {
            layout = layout.withPadding(padding);
            return self();
        }

        public T padding(int vertical, int horizontal) {
            return padding(new Padding(vertical, horizontal));
        }

        public T padding(int top, int right, int bottom, int left) {
            return padding(new Padding(top, right, bottom, left));
        }

        public AbstractContainer parent() {
            return parent;
        }

        public Position position() {
            return layout.position();
        }

        public T priority(int priority) {
            this.priority = priority;
            return self();
        }

        public int priority() {
            return priority;
        }

        public abstract AbstractContainer push();

        public T relative(int x, int y) {
            return relative(new ImmutablePoint(x, y));
        }

        public T relative(ImmutablePoint position) {
            return position(Position.relative(position));
        }

        public T position(Position position) {
            layout = layout.withPosition(position);
            return self();
        }

        public T relative() {
            return relative(new ImmutablePoint(0, 0));
        }

        public T scopes(Scope... scopes) {
            this.scopes = Arrays.asList(scopes);
            return self();
        }

        public List<Scope> scopes() {
            return scopes;
        }

        public T scopes(List<Scope> scopes) {
            this.scopes = scopes;
            return self();
        }

        public T size(int width, int height) {
            return size(new Size(width, height));
        }

        public T size(Size size) {
            layout = layout.withSize(size);
            return self();
        }

        public T size(int all) {
            return size(new Size(all, all));
        }

        public Size size() {
            return layout.size();
        }

        public Status status() {
            return this.display.status();
        }

        public T status(Status state) {
            display = display.withStatus(state);
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

        public T zIndex(int z) {
            display = display.withZIndex(z);
            return self();
        }

        public int zIndex() {
            return display.zIndex();
        }
    }


}
