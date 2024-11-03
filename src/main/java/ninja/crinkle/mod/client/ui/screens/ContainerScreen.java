package ninja.crinkle.mod.client.ui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import ninja.crinkle.mod.client.ClientHooks;
import ninja.crinkle.mod.client.icons.Icons;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.client.ui.widgets.layouts.Alignment;
import ninja.crinkle.mod.client.ui.widgets.layouts.Container;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedIconButton;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ContainerScreen extends Screen {
    private final Screen parent;
    private final Container root;

    public ContainerScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
        this.root = Container.builder(Theme.DEFAULT).size(width, height)
                .vertical(Alignment.CENTER, 10)
                .build();
    }

    @Override
    protected void init() {
        super.init();
        Container vertical = Container.builder(Theme.DEFAULT)
                .size(300, 300)
                .vertical(Alignment.CENTER, 10)
                .build();
        vertical.setDebug(true);
        Container buttonContainer = Container.builder(Theme.DEFAULT)
                .size(200, 60)
                .horizontal(Alignment.CENTER, 10)
                .build();
        buttonContainer.setDebug(true);
        buttonContainer.addChild(ThemedIconButton.builder(Theme.DEFAULT, Icons.GEAR)
                .bounds(0, 0, 40, 40).build());
        buttonContainer.addChild(ThemedIconButton.builder(Theme.DEFAULT, Icons.MESSINESS_DANGER)
                .bounds(0, 0, 40, 40).build());
        buttonContainer.addChild(ThemedIconButton.builder(Theme.DEFAULT, Icons.WETNESS_DANGER)
                .bounds(0, 0, 40, 40).build());
        vertical.addChild(buttonContainer);
        root.addChild(vertical);

    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics) {
        super.renderBackground(pGuiGraphics);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        root.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        super.onClose();
        Optional.ofNullable(DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHooks::getMinecraft))
                .ifPresent(minecraft -> minecraft.setScreen(parent));
    }
}
