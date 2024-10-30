package ninja.crinkle.mod.client.ui.screens;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import ninja.crinkle.mod.client.ClientHooks;
import ninja.crinkle.mod.client.ui.themes.Theme;

import java.util.Optional;

public class ConfigScreen extends FlexContainerScreen {
    private final Screen parent;
    public ConfigScreen(Screen parent) {
        super(Component.translatable("gui.crinklemod.screen.config.title"), Theme.DEFAULT);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        // Add your widgets here
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
