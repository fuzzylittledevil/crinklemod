package ninja.crinkle.mod.client.gui.layouts;

import ninja.crinkle.mod.client.gui.managers.GuiManager;
import ninja.crinkle.mod.client.gui.properties.Point;
import ninja.crinkle.mod.client.gui.widgets.AbstractWidget;
import ninja.crinkle.mod.client.gui.widgets.Button;
import ninja.crinkle.mod.client.gui.widgets.Container;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerticalAbsoluteTest {
    Container container;
    GuiManager manager = spy(GuiManager.create());
    Vertical verticalLayout;

    @Test
    void arrangeTestNotNested() {
        doCallRealMethod().when(verticalLayout).arrange(container);
        Point expectedRenderPosition = Point.of(30, 30);
        assertEquals(expectedRenderPosition, container.layout().boxes().rendered().contentBox().topLeft(), "Content box top left check");
        assertEquals(Layout.Alignment.TOP, verticalLayout.alignment(), "Alignment check");
        verticalLayout.arrange(container);
        List<AbstractWidget> sortedYX = container.children().stream()
                .sorted(Comparator.comparing((AbstractWidget widget) -> widget.position().point().yInt()))
                .toList();
        for (AbstractWidget widget : sortedYX) {
            assertEquals(expectedRenderPosition, widget.renderedPosition().point(), "Widget position check " + widget.name() + " index: " + container.children().indexOf(widget));
            expectedRenderPosition = expectedRenderPosition.add(0, widget.layout().size().height() + verticalLayout.spacing());
        }
    }

    @BeforeEach
    void setUp() {
        manager = spy(GuiManager.create());
        verticalLayout = spy(Vertical.builder().alignment(Layout.Alignment.TOP).spacing(2).build());
        container = spy(Container.builder(manager)
                .name("Container0")
                .layoutManager(verticalLayout)
                .absolute(15, 15)
                .size(300, 300)
                .margin(5)
                .padding(5)
                .border(5)
                .build());
        manager.root().add(container);
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
        manager.root().init();
    }

    @AfterEach
    void tearDown() {
        reset(container);
        reset(verticalLayout);
        reset(manager);
    }

    @Test
    void totalInnerHeightTest() {
        doCallRealMethod().when(verticalLayout).totalInnerHeight(container);
        int expectedHeight = container.children().stream().mapToInt(w -> w.layout().size().height()).sum() +
                (container.children().size() - 1) * verticalLayout.spacing();
        assertEquals(expectedHeight, verticalLayout.totalInnerHeight(container), "Total inner height check");
    }

    @Test
    void totalInnerWidthTest() {
        doCallRealMethod().when(verticalLayout).totalInnerWidth(container);
        int expectedWidth =
                container.children().stream().mapToInt(w -> w.layout().size().width()).max().orElse(0);
        assertEquals(expectedWidth, verticalLayout.totalInnerWidth(container), "Total inner width check");
    }
}