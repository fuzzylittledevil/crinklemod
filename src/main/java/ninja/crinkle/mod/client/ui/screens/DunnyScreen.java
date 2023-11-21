package ninja.crinkle.mod.client.ui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.menus.DunnyContainer;

public class DunnyScreen extends AbstractContainerScreen<DunnyContainer> {

    private final ResourceLocation GUI = new ResourceLocation(CrinkleMod.MODID, "textures/gui/dunny_screen.png");

    public DunnyScreen(DunnyContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.inventoryLabelY = this.imageHeight - 110;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        graphics.blit(GUI, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }
}
