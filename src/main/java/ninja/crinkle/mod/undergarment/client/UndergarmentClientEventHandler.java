package ninja.crinkle.mod.undergarment.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.crinkle.mod.metabolism.client.ui.screens.MetabolismScreen;
import ninja.crinkle.mod.undergarment.client.ui.screens.UndergarmentScreen;
import ninja.crinkle.mod.undergarment.common.Undergarment;
import org.jetbrains.annotations.NotNull;

public class UndergarmentClientEventHandler {
    @SubscribeEvent
    public void onPlayerTick(TickEvent.@NotNull PlayerTickEvent event) {
        if (!event.side.isClient()) return;
        if (event.phase == TickEvent.Phase.END && event.player.tickCount % 50 == 0) {
            Undergarment.of(event.player).tick();
        }
    }

    @SubscribeEvent
    public void addButtonToInventory(ScreenEvent.Init.Pre event) {
        if (event.getScreen() instanceof AbstractContainerScreen<?> screen) {
            if (!(screen instanceof InventoryScreen) && !(screen instanceof CreativeModeInventoryScreen)) return;
            int leftPos = screen.getGuiLeft() + 8;
            int topPos = screen.getGuiTop() + 8 + 28;
            int width = 80;
            int height = 20;
            Button button = Button.builder(Component.translatable("gui.crinklemod.undergarment_button.title"), b -> {
                        Minecraft.getInstance().setScreen(new UndergarmentScreen());
                    })
                    .bounds(leftPos, topPos, width, height)
                    .build();
            event.addListener(button);
        }
    }
}
