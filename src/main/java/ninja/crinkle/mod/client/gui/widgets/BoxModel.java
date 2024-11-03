package ninja.crinkle.mod.client.gui.widgets;

import ninja.crinkle.mod.client.gui.properties.*;
import ninja.crinkle.mod.client.gui.renderers.ThemeGraphics;

public interface BoxModel {
    Margin margin();
    void margin(Margin margin);
    Padding padding();
    void padding(Padding padding);
    Border border();
    void border(Border border);
    Box box();
    Box borderBox();
    Box contentBox();
    void renderContent(ThemeGraphics pGuiGraphics, Point pMouse, Box pBox, float pPartialTick);
    void renderDebug(ThemeGraphics pGuiGraphics, Box pOuter, Box pBorder, Box pInner);
}
