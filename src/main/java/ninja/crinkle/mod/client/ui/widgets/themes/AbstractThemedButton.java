package ninja.crinkle.mod.client.ui.widgets.themes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.ui.themes.BoxTheme;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.client.ui.widgets.properties.Border;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractThemedButton extends ThemedBorderBox {
    private Consumer<AbstractThemedButton> onPress;
    private boolean isClicked = false;
    private Predicate<AbstractThemedButton> activePredicate;

    public AbstractThemedButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, Theme pTheme,
                                Consumer<AbstractThemedButton> onPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pTheme, BoxTheme.Type.BUTTON);
        this.onPress = onPress;
    }

    public AbstractThemedButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, Theme pTheme,
                                Consumer<AbstractThemedButton> onPress, BoxTheme.Type type) {
        super(pX, pY, pWidth, pHeight, pMessage, pTheme, type);
        this.onPress = onPress;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (activePredicate != null)
            this.active = activePredicate.test(this);
        if (!isHovered && isClicked) isClicked = false;
        setInverted(this.isClicked);
        Border border = this.getBorder();
        if (this.isHoveredOrFocused()) {
            this.setBorder(new Border(border.top(), border.bottom(), border.left(), border.right(),
                    this.getTheme().getForegroundColor()));
        }
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.setBorder(border);
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
            if (this.onPress != null)
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

    public void setActivePredicate(Predicate<AbstractThemedButton> activePredicate) {
        this.activePredicate = activePredicate;
    }

    public void setOnPress(Consumer<AbstractThemedButton> onPress) {
        this.onPress = onPress;
    }
}
