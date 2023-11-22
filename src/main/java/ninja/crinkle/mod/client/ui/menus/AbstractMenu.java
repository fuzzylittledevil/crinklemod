package ninja.crinkle.mod.client.ui.menus;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import ninja.crinkle.mod.client.ClientHooks;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractMenu {
    private static final int DEFAULT_SPACER = 4;
    private static final int DEFAULT_MARGIN = 8;
    private static final int DEFAULT_LINE_HEIGHT = 15;
    private static final int DEFAULT_LINE_SPACING = 4;
    private final Font font;
    private final List<AbstractWidget> children = Lists.newArrayList();
    private boolean visible;
    private final int leftPos;
    private final int topPos;
    private final int spacer;
    private final int margin;
    private final int lineHeight;
    private final int lineSpacing;

    protected AbstractMenu(int leftPos, int topPos, int spacer, int lineSpacing, int margin, int lineHeight,
                           boolean visible, Font font) {
        this.visible = visible;
        this.leftPos = leftPos;
        this.topPos = topPos;
        this.spacer = spacer;
        this.lineSpacing = lineSpacing;
        this.margin = margin;
        this.lineHeight = lineHeight;
        this.font = font;
    }

    public void add(AbstractWidget widget) {
        children.add(widget);
    }

    public void addAll(List<AbstractWidget> widgets) {
        children.addAll(widgets);
    }

    public void visitChildren(Consumer<AbstractWidget> visitor) {
        children.forEach(visitor);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        visitChildren(widget -> widget.visible = visible);
    }

    public boolean isVisible() {
        return visible;
    }

    public void tick() {}

    public int getFontOffset() {
        return (getLineHeight() - getFont().lineHeight) / 2;
    }

    public int getLineYOffset(int count) {
        int offset = (getLineHeight() + getLineSpacing()) * count;
        return offset + getLineSpacing() + getMargin();
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
}