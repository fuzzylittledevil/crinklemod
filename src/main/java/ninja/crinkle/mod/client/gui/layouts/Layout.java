package ninja.crinkle.mod.client.gui.layouts;

import ninja.crinkle.mod.client.gui.properties.Box;
import ninja.crinkle.mod.client.gui.properties.Position;
import ninja.crinkle.mod.client.gui.properties.Size;
import ninja.crinkle.mod.client.gui.states.WidgetLayout;
import ninja.crinkle.mod.client.gui.widgets.AbstractContainer;

public interface Layout {
    static AbstractLayout.AbstractBuilder<Horizontal.Builder> horizontal() {
        return Horizontal.builder();
    }

    static AbstractLayout.AbstractBuilder<Vertical.Builder> vertical() {
        return Vertical.builder();
    }

    Alignment alignment();

    void arrange(AbstractContainer container);

    int spacing();

    int totalInnerHeight(AbstractContainer abstractContainer);

    int totalInnerWidth(AbstractContainer abstractContainer);

    enum Alignment {TOP, RIGHT, BOTTOM, LEFT, CENTER}

    interface Widget extends BoxModel {
        WidgetLayout layout();

        Position position();

        void position(Position position);

        void resetPosition();

        Size size();

        void size(Size size);

        String name();
    }
}
