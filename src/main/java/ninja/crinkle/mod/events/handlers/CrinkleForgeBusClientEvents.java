package ninja.crinkle.mod.events.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.crinkle.mod.client.ui.screens.MetabolismScreen;
import ninja.crinkle.mod.client.ui.screens.UndergarmentScreen;

public class CrinkleForgeBusClientEvents {
    @SubscribeEvent
    public void addButtonsToInventory(ScreenEvent.Init.Pre event) {
        if (event.getScreen() instanceof AbstractContainerScreen<?> screen) {
            if (!(screen instanceof InventoryScreen) && !(screen instanceof CreativeModeInventoryScreen)) return;
            int leftPos = screen.getGuiLeft() + 8;
            int topPos = screen.getGuiTop() + 8;
            int width = 80;
            int height = 20;
            Button button = Button.builder(Component.translatable("gui.crinklemod.metabolism_button.title"), b -> {
                        Minecraft.getInstance().setScreen(new MetabolismScreen());
                    })
                    .bounds(leftPos, topPos, width, height)
                    .build();
            event.addListener(button);
            leftPos = screen.getGuiLeft() + 8;
            topPos = screen.getGuiTop() + 8 + 28;
            button = Button.builder(Component.translatable("gui.crinklemod.undergarment_button.title"), b -> {
                        Minecraft.getInstance().setScreen(new UndergarmentScreen());
                    })
                    .bounds(leftPos, topPos, width, height)
                    .build();
            event.addListener(button);
        }
    }
}
