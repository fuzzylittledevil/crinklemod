package ninja.crinkle.mod.client.gui.layouts;

import ninja.crinkle.mod.client.gui.properties.*;
import ninja.crinkle.mod.client.gui.renderers.ThemeGraphics;

public interface BoxModel {
    Margin margin();
    void margin(Margin margin);
    Padding padding();
    void padding(Padding padding);
    Border border();
    void border(Border border);

    Position renderedPosition();

    Position parentPosition();

    int totalHeight();

    int totalWidth();

    void renderContent(ThemeGraphics pGuiGraphics, Point pMouse, Box renderBox, float pPartialTick);
    void renderDebug(ThemeGraphics pGuiGraphics);
}
