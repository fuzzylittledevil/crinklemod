package ninja.crinkle.mod.client.gui.widgets;

import ninja.crinkle.mod.client.gui.managers.GuiManager;
import ninja.crinkle.mod.client.gui.properties.*;
import ninja.crinkle.mod.client.gui.states.WidgetBehavior;
import ninja.crinkle.mod.client.gui.states.WidgetDisplay;
import ninja.crinkle.mod.client.gui.states.WidgetLayout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AbstractContainerAbsoluteTest {
    GuiManager manager = spy(GuiManager.create());
    AbstractContainer container = spy(Container.builder(manager).name("Container0").build());
    final WidgetDisplay display = new WidgetDisplay(true, 1.0f, 0, Status.active);
    final WidgetLayout layout = new WidgetLayout(Position.absolute(0, 0), Size.of(100, 100), Margin.all(0),
            Border.all(0), Padding.all(0));
    final WidgetBehavior behavior = new WidgetBehavior(false, false, false, false, false, true);

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
        container.children().forEach(w -> {
            int expectedZ = container.zIndex() + 1;
            assertEquals(expectedZ, w.zIndex(), "Z-index check for " + w.name());
        });
    }

    @Test
    void updateLayout() {
    }
}