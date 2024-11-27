package ninja.crinkle.mod.client.gui.widgets;

import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.gui.events.MoveEvent;
import ninja.crinkle.mod.client.gui.managers.DragManager;
import ninja.crinkle.mod.client.gui.managers.EventManager;
import ninja.crinkle.mod.client.gui.managers.GuiManager;
import ninja.crinkle.mod.client.gui.properties.*;
import ninja.crinkle.mod.client.gui.states.WidgetBehavior;
import ninja.crinkle.mod.client.gui.states.WidgetDisplay;
import ninja.crinkle.mod.client.gui.states.WidgetLayout;
import ninja.crinkle.mod.client.gui.textures.Texture;
import ninja.crinkle.mod.client.gui.themes.StyleVariant;
import ninja.crinkle.mod.client.gui.themes.Style;
import ninja.crinkle.mod.util.ClientUtil;
import ninja.crinkle.mod.util.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AbstractWidgetTest {
    @Mock
    GuiManager manager = spy(GuiManager.create());
    @Mock
    AbstractContainer container = spy(Container.builder(manager).priority(EventManager.PRIORITY_IGNORE).build());
    @Mock
    AbstractWidget widget = spy(Button.builder(container).priority(0).hoverable(true).build());

    @Test
    void calculateBoxes() {
        int margin = 5, border = 4, padding = 3, width = 100, height = 100;

        WidgetLayout layout = new WidgetLayout(Position.relative(0, 0), Size.of(width, height), Margin.all(margin),
                Border.all(border), Padding.all(padding));
        widget.layout().widget(widget);
        doReturn(layout).when(widget).layout();
        assertEquals(border, widget.layout().border().top(), "Border top check");
        assertEquals(padding, widget.layout().padding().bottom(), "Padding bottom check");
        assertEquals(margin, widget.layout().margin().right(), "Margin right check");
        assertEquals(0, widget.textureBorder().left(), "Ensure texture border is 0");

        Point expectedMarginTopLeft = Point.of(0, 0);
        Point expectedMarginBottomRight = Point.of(width, height);
        Point expectedBorderTopLeft = Point.of(margin, margin);
        Point expectedBorderBottomRight = Point.of(width - margin, height - margin);
        Point expectedPaddingTopLeft = Point.of(margin + border, margin + border);
        Point expectedPaddingBottomRight = Point.of(width - margin - border, height - margin - border);
        Point expectedContentTopLeft = Point.of(margin + border + padding, margin + border + padding);
        Point expectedContentBottomRight = Point.of(width - margin - border - padding,
                height - margin - border - padding);
        widget.layout().widget(widget);
        WidgetLayout.BoxBuilder bb = widget.layout().boxes();
        assertEquals(expectedMarginTopLeft, bb.box().topLeft(), "Margin top left check");
        assertEquals(expectedMarginBottomRight, bb.box().bottomRight(), "Margin bottom right check");
        assertEquals(expectedBorderTopLeft, bb.borderBox().topLeft(), "Border top left check");
        assertEquals(expectedBorderBottomRight, bb.borderBox().bottomRight(), "Border bottom right check");
        assertEquals(expectedPaddingTopLeft, bb.paddingBox().topLeft(), "Padding top left check");
        assertEquals(expectedPaddingBottomRight, bb.paddingBox().bottomRight(), "Padding bottom right check");
        assertEquals(expectedContentTopLeft, bb.contentBox().topLeft(), "Content top left check");
        assertEquals(expectedContentBottomRight, bb.contentBox().bottomRight(), "Content bottom right check");
    }

    @Test
    void screenPositionOf() {
        doReturn(Position.absolute(10, 10)).when(widget).renderedPosition();
        doCallRealMethod().when(widget).screenPositionOf(any());
        Box actual = widget.screenPositionOf(new Box(Position.relative(0, 0), Size.of(100, 100)));
        Box expected = new Box(10, 10, 100, 100);
        assertEquals(expected, actual, "Screen position of box check");
    }

    @Test
    void renderedPosition() {
        Container parent = spy(Container.builder(manager).absolute(15, 15).build());
        doReturn(Optional.of(parent)).when(widget).parent();
        doCallRealMethod().when(widget).renderedPosition();
        assertEquals(Position.absolute(15, 15), widget.renderedPosition(), "Rendered position check");
    }

    @BeforeEach
    void setUp() {
        WidgetDisplay display = new WidgetDisplay(true, 1.0f, 0);
        WidgetLayout layout = new WidgetLayout(Position.relative(0, 0), Size.of(100, 100), Margin.all(0),
                Border.all(0), Padding.all(0), widget);
        WidgetBehavior behavior = new WidgetBehavior(false, false, false, false, false, true, true, true, true);
        doReturn(display).when(widget).display();
        doReturn(layout).when(widget).layout();
        doReturn(behavior).when(widget).behavior();
    }

    @AfterEach
    void tearDown() {
        reset(widget);
    }

    @Test
    void textureBorder() {
        doCallRealMethod().when(widget).textureBorder();
        assertEquals(Border.ZERO, widget.textureBorder(), "Texture border check");

        Style theme = mock(Style.class);
        StyleVariant appearance = mock(StyleVariant.class);
        Texture texture = mock(Texture.class);
        doReturn(texture).when(appearance).getBackgroundTexture();
        doCallRealMethod().when(widget).textureBorder();
        doReturn(theme).when(widget).widgetTheme();
        doReturn(appearance).when(widget).appearance();
        Map<Texture.Slice.Location, Texture.Slice> slices = new HashMap<>();
        slices.put(Texture.Slice.Location.topLeft, new Texture.Slice(0, 0, 4, 4));
        slices.put(Texture.Slice.Location.topRight, new Texture.Slice(5, 0, 4, 4));
        slices.put(Texture.Slice.Location.bottomLeft, new Texture.Slice(0, 5, 4, 4));
        slices.put(Texture.Slice.Location.bottomRight, new Texture.Slice(5, 5, 4, 4));
        slices.put(Texture.Slice.Location.top, new Texture.Slice(4, 0, 1, 2));
        slices.put(Texture.Slice.Location.bottom, new Texture.Slice(4, 5, 1, 2));
        slices.put(Texture.Slice.Location.left, new Texture.Slice(0, 4, 2, 1));
        slices.put(Texture.Slice.Location.right, new Texture.Slice(5, 4, 2, 1));
        when(texture.boundsOf(any(Texture.Slice.Location.class))).thenAnswer(v -> slices.get(v.getArgument(0, Texture.Slice.Location.class)).size());

        assertEquals(new Border(2, Color.BLACK), widget.textureBorder(), "Texture border check");
    }

    @Test
    void totalHeight() {
        int width = 100, height = 100;
        WidgetLayout layout = new WidgetLayout(Position.relative(0, 0), Size.of(width, height), Margin.all(5),
                Border.all(5), Padding.all(5));
        doReturn(layout).when(widget).layout();
        doCallRealMethod().when(widget).totalHeight();
        assertEquals(height, widget.totalHeight(), "Total height check");
    }

    @Test
    void totalWidth() {
        int width = 100, height = 100;
        WidgetLayout layout = new WidgetLayout(Position.relative(0, 0), Size.of(width, height), Margin.all(5),
                Border.all(5), Padding.all(5));
        doReturn(layout).when(widget).layout();
        doCallRealMethod().when(widget).totalWidth();
        assertEquals(width, widget.totalWidth(), "Total width check");
    }

    @Test
    void mouseOver() {
        doCallRealMethod().when(widget).mouseOver(any());
        Point point = widget.layout().boxes().rendered().contentBox().start();
        assertTrue(widget.mouseOver(point), "Mouse over check");
    }

    @Test
    void onMove() {
        doCallRealMethod().when(widget).onMove(any());
        Point moveTo = widget.layout().boxes().contentBox().start().add(2, 2);
        MoveEvent event = new MoveEvent(Scope.Screen, widget, moveTo.xInt(), moveTo.yInt(), List.of(widget));
        try(var clientUtil = TestUtil.mockClientUtil()) {
            clientUtil.when(ClientUtil::getMousePosition).thenReturn(moveTo);
            widget.onMove(event);
        }
        // verify the widget saw the event
        verify(widget, times(1)).mouseOver(any());
    }
}