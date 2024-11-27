package ninja.crinkle.mod.client.gui.widgets;

import ninja.crinkle.mod.client.gui.layouts.Layout;
import ninja.crinkle.mod.client.gui.managers.DragManager;
import ninja.crinkle.mod.client.gui.managers.GuiManager;
import ninja.crinkle.mod.client.gui.properties.*;
import ninja.crinkle.mod.client.gui.states.WidgetBehavior;
import ninja.crinkle.mod.client.gui.states.WidgetDisplay;
import ninja.crinkle.mod.client.gui.states.WidgetLayout;
import ninja.crinkle.mod.client.gui.themes.Style;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AbstractContainerAbsoluteTest {
    GuiManager manager = spy(GuiManager.create());
    AbstractContainer container = spy(Container.builder(manager).name("Container0").build());
    final WidgetDisplay display = new WidgetDisplay(true, 1.0f, 0);
    final WidgetLayout layout = new WidgetLayout(Position.absolute(0, 0), Size.of(100, 100), Margin.all(0),
            Border.all(0), Padding.all(0));
    final WidgetBehavior behavior = new WidgetBehavior(false, false, false, false, false, true, false, false, false);

    @BeforeEach
    void setUp() {
        doReturn(display).when(container).display();
        doReturn(layout).when(container).layout();
        doReturn(behavior).when(container).behavior();
        doReturn(Optional.empty()).when(container).layoutManager();
        for (int i = 0; i < 5; i++) {
            Button button = spy(Button.builder(container)
                    .relative(0, 0)
                    .name("Button" + i)
                    .size(60, 25)
                    .margin(2)
                    .padding(3)
                    .border(0)
                    .build());
            container.add(button);
        }
        manager.root().add(container);
    }

    @AfterEach
    void tearDown() {
        reset(container);
    }

    @Test
    void totalHeight() {
        doCallRealMethod().when(container).totalHeight();
        assertEquals(layout.size().height(), container.totalHeight(), "Total height check");
    }

    @Test
    void totalWidth() {
        doCallRealMethod().when(container).totalWidth();
        assertEquals(layout.size().width(), container.totalWidth(), "Total width check");
    }

    @Test
    void totalInnerHeight() {
        doCallRealMethod().when(container).totalInnerHeight();
        int innerHeight = container.children().stream().mapToInt(AbstractWidget::totalHeight).sum();
        assertEquals(innerHeight, container.totalInnerHeight(), "Total inner height check");
    }

    @Test
    void totalInnerWidth() {
        doCallRealMethod().when(container).totalInnerWidth();
        int innerWidth = container.children().stream().mapToInt(AbstractWidget::totalWidth).sum();
        assertEquals(innerWidth, container.totalInnerWidth(), "Total inner width check");
    }

    @Test
    void checkZIndexes() {
        // We modify the z-index of the children when adding, check to see if they are consistent
        int expectedZ = container.zIndex();
        for (AbstractWidget w : container.children()) {
            expectedZ += DragManager.Z_STEP;
            assertEquals(expectedZ, w.zIndex(), "Z-index check for " + w.name());
        }
    }

    @Test
    void updateLayout() {
        int width = 300;
        int height = 300;
        int buttonWidth = 60;
        int buttonHeight = 25;
        int spacing = 2;
        int buttonCount = 3;
        AbstractContainer vertical = spy(Container.builder(manager)
                .name("VerticalContainer")
                .layoutManager(Layout.vertical().alignment(Layout.Alignment.CENTER).spacing(spacing).build())
                .size(width, height)
                .build());
        AbstractContainer horizontal = spy(Container.builder(vertical)
                .name("HorizontalContainer")
                .layoutManager(Layout.horizontal().alignment(Layout.Alignment.CENTER).spacing(spacing).build())
                .size(width, buttonHeight)
                .build());
        vertical.add(horizontal);
        for (int i = 0; i < buttonCount; i++) {
            Button button = spy(Button.builder(horizontal)
                    .name("Button" + i)
                    .size(buttonWidth, buttonHeight)
                    .margin(2)
                    .padding(3)
                    .build());
            horizontal.add(button);
        }
        manager.root().init();
        doCallRealMethod().when(vertical).updateLayout();
        doCallRealMethod().when(horizontal).updateLayout();
        vertical.updateLayout();
        verify(horizontal, times(1)).updateLayout();
        verify(vertical, times(1)).updateLayout();

        int contentWidth = buttonCount * (buttonWidth + spacing) - spacing;
        int expectedStartX = (width - contentWidth) / 2;
        horizontal.children().stream().min(Comparator.comparingInt(a -> a.renderedPosition().point().xInt()))
                .ifPresent(w -> assertEquals(expectedStartX, w.renderedPosition().point().xInt(),
                        "Horizontal container should be centered within the vertical container."));

        int expectedEndX = expectedStartX + contentWidth - buttonWidth;
        horizontal.children().stream().max(Comparator.comparingInt(a -> a.renderedPosition().point().xInt()))
                .ifPresent(w -> assertEquals(expectedEndX,
                        w.renderedPosition().point().xInt(),
                        "Horizontal container should be centered within the vertical container."));

        // Optionally, check that all buttons are vertically aligned in the middle of the horizontal container.
        int containerCenterY = height / 2;
        for (AbstractWidget button : horizontal.children()) {
            int expectedButtonY = containerCenterY - button.size().height() / 2;
            assertEquals(expectedButtonY, button.renderedPosition().point().yInt(), "Button '"+button.name()+"' should be vertically centered in the horizontal container.");
        }
    }

}