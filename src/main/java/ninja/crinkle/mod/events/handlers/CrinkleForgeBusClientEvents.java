package ninja.crinkle.mod.events.handlers;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.crinkle.mod.client.ui.screens.CrinkleScreen;
import org.slf4j.Logger;

public class CrinkleForgeBusClientEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public void addButtonsToInventory(ScreenEvent.Init.Pre event) {
        if (event.getScreen() instanceof AbstractContainerScreen<?> screen) {
            if (!(screen instanceof InventoryScreen) && !(screen instanceof CreativeModeInventoryScreen)) return;
            int leftPos = 8;
            int topPos = 8;
            int width = 80;
            int height = 20;
            LOGGER.debug("Adding button at ({}, {}) with size ({}, {})", leftPos, topPos, width, height);
            Screen current = Minecraft.getInstance().screen;
            Button button = Button.builder(Component.translatable("gui.crinklemod.crinkle_button.title"),
                            b -> Minecraft.getInstance().setScreen(new CrinkleScreen(current)))
                    .bounds(leftPos, topPos, width, height)
                    .build();
            event.addListener(button);
        }
    }
}
