package ninja.crinkle.mod.client.ui.widgets.themes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.ui.themes.BoxTheme;
import ninja.crinkle.mod.client.ui.themes.Theme;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class AbstractThemedButton extends ThemedBorderBox {
    private final Consumer<AbstractThemedButton> onPress;
    private boolean isClicked = false;
    public AbstractThemedButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, Theme theme,
                                Consumer<AbstractThemedButton> onPress) {
        super(pX, pY, pWidth, pHeight, pMessage, theme, BoxTheme.Size.MEDIUM);
        this.onPress = onPress;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (!isHovered && isClicked) isClicked = false;
        setInverted(this.isClicked);
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if (this.isHoveredOrFocused()) {
            pGuiGraphics.renderOutline(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0xFFFFFFFF);
        }
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        isClicked = true;
        if (this.onPress != null)
            this.onPress.accept(this);
    }

    @Override
    public void onRelease(double pMouseX, double pMouseY) {
        super.onRelease(pMouseX, pMouseY);
        isClicked = false;
        setFocused(false);
    }

    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (!this.active || !this.visible) {
            return false;
        } else if (CommonInputs.selected(pKeyCode)) {
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            this.onPress.accept(this);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        isClicked = false;
        setFocused(false);
        return super.keyReleased(pKeyCode, pScanCode, pModifiers);
    }
}
