package ninja.crinkle.mod.client.gui.screens;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.gui.layouts.Layout;
import ninja.crinkle.mod.client.gui.properties.ImmutablePoint;
import ninja.crinkle.mod.client.gui.properties.MutablePoint;
import ninja.crinkle.mod.client.gui.widgets.AbstractContainer;
import ninja.crinkle.mod.util.ClientUtil;
import org.slf4j.Logger;

public class TestScreen extends AbstractScreen {
    private static final Logger LOGGER = LogUtils.getLogger();

    public TestScreen() {
        super(Component.literal("Test Screen"));
    }

    @Override
    public String name() {
        return "TestScreen";
    }

    @Override
    protected void init() {
        AbstractContainer vPanel = root().addContainer()
                .name("window1")
                .widgetTheme("panel")
                .layoutManager(Layout.vertical().alignment(Layout.Alignment.CENTER).spacing(5))
                .margin(10)
                .padding(10)
                .absolute(20, 20)
                .size(300, 200)
                .pushAndReturn();
        AbstractContainer hPanel = vPanel.addContainer()
                .name("window2")
                .layoutManager(Layout.horizontal().alignment(Layout.Alignment.CENTER).spacing(5))
                .margin(10)
                .padding(10)
                .size(vPanel.layout().boxes().contentBox().size())
                .pushAndReturn();
        AbstractContainer buttons = hPanel.addContainer()
                .name("buttons")
                .layoutManager(Layout.horizontal().alignment(Layout.Alignment.CENTER).spacing(5))
                .size(hPanel.layout().boxes().contentBox().size())
                .pushAndReturn();

        int width = ClientUtil.getMinecraft().font.width("Button 0") + 10;
        for (int i = 0; i < 3; i++) {
            buttons.addButton()
                    .name("button" + i)
                    .widgetTheme("button")
                    .text("Button " + i)
                    .relative(ImmutablePoint.ZERO)
                    .margin(2)
                    .padding(3)
                    .size(width, 20)
                    .pushAndReturn();
        }
        super.init();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics) {
        super.renderBackground(pGuiGraphics);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}