package ninja.crinkle.mod.client.gui.screens;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.gui.widgets.Button;
import org.slf4j.Logger;

public class TestScreen extends AbstractScreen {
    private static final Logger LOGGER = LogUtils.getLogger();
    public TestScreen() {
        super(Component.literal("Test Screen"));
    }

    @Override
    protected void init() {
        super.init();
        addListener(Button.builder()
                .tabIndex(1)
                .draggable(true)
                .border(5, Color.PURPLE)
                .onClick(event -> LOGGER.info("Button clicked!"))
                .margin(2)
                .padding(5)
                .visible(true)
                .active(true)
                .widgetTheme("button")
                .alpha(1.0f)
                .bounds(100, 40)
                .position(10, 10)
                .text("Test Button")
                .build());
        addListener(Button.builder()
                .tabIndex(2)
                .border(5, Color.PURPLE)
                .onClick(event -> LOGGER.info("Button clicked!"))
                .margin(2)
                .padding(5)
                .visible(true)
                .active(true)
                .widgetTheme("button")
                .alpha(1.0f)
                .bounds(100, 40)
                .position(10, 80)
                .text("Test Button")
                .build());
    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics) {
        super.renderBackground(pGuiGraphics);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}