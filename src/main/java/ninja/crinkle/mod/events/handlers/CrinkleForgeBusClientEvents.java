package ninja.crinkle.mod.events.handlers;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.crinkle.mod.client.ui.screens.CrinkleScreen;
import ninja.crinkle.mod.sounds.CrinkleSounds;
import ninja.crinkle.mod.undergarment.Undergarment;
import ninja.crinkle.mod.util.ClientUtil;
import org.slf4j.Logger;

import java.util.Random;

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

    @SubscribeEvent
    public void handleCrinkles(PlaySoundEvent event) {
        Minecraft minecraft = ClientUtil.getMinecraft();
        if (minecraft == null || minecraft.level == null || minecraft.player == null || event.getSound() == null)
            return;
        BlockState blockState = minecraft.level.getBlockState(minecraft.player.blockPosition().below());
        if (blockState.getSoundType().getStepSound().getLocation().equals(event.getSound().getLocation())
                && Undergarment.getWornUndergarment(minecraft.player) != ItemStack.EMPTY) {
            Random random = new Random();
            minecraft.player.playSound(CrinkleSounds.CRINKLE_SOUND.get(),
                    0.5f + random.nextFloat(0.5f),
                    0.75f + random.nextFloat(0.5f));
        }
    }
}
