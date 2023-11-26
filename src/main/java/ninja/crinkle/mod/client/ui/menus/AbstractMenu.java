package ninja.crinkle.mod.client.ui.menus;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import ninja.crinkle.mod.client.ClientHooks;
import ninja.crinkle.mod.client.ui.screens.FlexContainerScreen;
import ninja.crinkle.mod.client.ui.themes.Theme;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractMenu {
    private static final int DEFAULT_SPACER = 4;
    private static final int DEFAULT_MARGIN = 8;
    private static final int DEFAULT_LINE_HEIGHT = 15;
    private static final int DEFAULT_LINE_SPACING = 4;
    private final Screen screen;
    private final Font font;
    private final List<AbstractWidget> children = Lists.newArrayList();
    private final List<AbstractMenu> subMenus = Lists.newArrayList();
    private boolean visible;
    private final int leftPos;
    private final int topPos;
    private final int spacer;
    private final int margin;
    private final int lineHeight;
    private final int lineSpacing;

    protected AbstractMenu(Screen screen, int leftPos, int topPos, int spacer, int lineSpacing, int margin, int lineHeight,
                           boolean visible, Font font) {
        this.screen = screen;
        this.visible = visible;
        this.leftPos = leftPos;
        this.topPos = topPos;
        this.spacer = spacer;
        this.lineSpacing = lineSpacing;
        this.margin = margin;
        this.lineHeight = lineHeight;
        this.font = font;
    }

    public void addWidget(AbstractWidget widget) {
        children.add(widget);
    }

    public void addSubMenu(AbstractMenu menu) {
        subMenus.add(menu);
    }

    public void addAllSubMenus(List<AbstractMenu> menus) {
        subMenus.addAll(menus);
    }

    public void addAllWidgets(List<AbstractWidget> widgets) {
        children.addAll(widgets);
    }

    public void visitChildren(Consumer<AbstractWidget> visitor) {
        children.forEach(visitor);
    }

    public void visitSubMenus(Consumer<AbstractMenu> visitor) {
        subMenus.forEach(visitor);
    }

    public void visitAll(Consumer<AbstractWidget> visitor) {
        visitChildren(visitor);
        visitSubMenus(menu -> menu.visitAll(visitor));
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        visitChildren(widget -> widget.visible = visible);
        visitSubMenus(menu -> menu.setVisible(visible));
    }

    public boolean isVisible() {
        return visible;
    }

    public void tick() {
    }

    public int getFontOffset() {
        return (getLineHeight() - getFont().lineHeight) / 2;
    }

    public int getLineYOffset(int count) {
        if (count == 0) return getMargin();
        return (getLineHeight() + getLineSpacing()) * count;
    }

    public int getLineXOffset() {
        return getSpacer() + getMargin();
    }

    public int getLeftPos() {
        return leftPos;
    }

    public int getTopPos() {
        return topPos;
    }

    public int getSpacer() {
        return spacer == 0 ? DEFAULT_SPACER : spacer;
    }

    public int getMargin() {
        return margin == 0 ? DEFAULT_MARGIN : margin;
    }

    public int getLineHeight() {
        return lineHeight == 0 ? DEFAULT_LINE_HEIGHT : lineHeight;
    }

    public Font getFont() {
        return font;
    }

    public int getLineSpacing() {
        return lineSpacing == 0 ? DEFAULT_LINE_SPACING : lineSpacing;
    }

    public Player getPlayer() {
        return Optional.ofNullable(DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHooks::getMinecraft))
                .map(m -> m.player)
                .orElseThrow(() -> new IllegalStateException("Cannot create menu without a player"));
    }

    public Screen getScreen() {
        return screen;
    }

    public Theme getTheme() {
        if (screen instanceof FlexContainerScreen flexContainerScreen)
            return flexContainerScreen.getTheme();
        return Theme.DEFAULT;
    }
}
