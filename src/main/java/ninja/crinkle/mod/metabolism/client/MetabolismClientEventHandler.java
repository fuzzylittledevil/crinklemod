package ninja.crinkle.mod.metabolism.client;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.crinkle.mod.metabolism.client.ui.screens.MetabolismScreen;
import ninja.crinkle.mod.metabolism.common.Metabolism;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * An event handler that is used to handle forge and Minecraft events.
 *
 * @author Galen
 * @see net.minecraftforge.eventbus.api.Event
 */
public class MetabolismClientEventHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Hook on when a living entity uses an item. This is used to consume items that are used as food or drink.
     *
     * @param event The event
     * @see LivingEntityUseItemEvent.Finish
     */
    @SubscribeEvent
    public void onLivingEntityUseItem(LivingEntityUseItemEvent.@NotNull Finish event) {
        if (!event.getEntity().level().isClientSide) return;
        if (event.getEntity() instanceof Player player)
            Metabolism.of(player).consume(event.getItem());
    }

    /**
     * Hook on when a player ticks. This is used to tick the metabolism of a player.
     * It only ticks the metabolism every 100 ticks, or 5 seconds.
     *
     * @param event The event
     * @see TickEvent.PlayerTickEvent
     */
    @SubscribeEvent
    public void onPlayerTick(TickEvent.@NotNull PlayerTickEvent event) {
        if (!event.side.isClient()) return;
        if (event.phase == TickEvent.Phase.END && event.player.tickCount % 100 == 0) {
            Metabolism.of(event.player).tick();
        }
    }

    /**
     * Hook when a player opens inventory so we can add a button.
     */
    @SubscribeEvent
    public void addButtonToSurvivalInventory(ScreenEvent.Init.Pre event) {
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
        }
    }
}
