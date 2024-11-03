package ninja.crinkle.mod.events.handlers;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.crinkle.mod.client.gui.screens.TestScreen;
import ninja.crinkle.mod.client.icons.Icons;
import ninja.crinkle.mod.client.animations.AnimationController;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedButton;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedIconButton;
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
            int width = 80;
            int height = 20;
            int leftPos = 32;
            int topPos = event.getScreen().height - height - 6;
            LOGGER.debug("Adding button at ({}, {}) with size ({}, {})", leftPos, topPos, width, height);
            Screen current = Minecraft.getInstance().screen;
            ThemedButton button = ThemedButton.builder(Theme.DEFAULT)
                    .label(Component.translatable("gui.crinklemod.crinkle_button.title"))
//                    .onPress(b -> Minecraft.getInstance().setScreen(new CrinkleScreen(current)))
                    .width(width).height(height)
                    .x(leftPos).y(topPos)
                    .build();
            ThemedIconButton themedButton = ThemedIconButton.builder(Theme.DEFAULT, Icons.GEAR)
                    .onPress(b -> Minecraft.getInstance().setScreen(new TestScreen()))
                    .bounds(leftPos + width, topPos, 20, 20)
                    .build();
            event.addListener(themedButton);
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

    @SubscribeEvent
    public void renderAnimations(RenderGuiOverlayEvent event) {
        Minecraft minecraft = ClientUtil.getMinecraft();
        if (minecraft == null || minecraft.level == null || minecraft.player == null) return;
        double gameTime = minecraft.level.getGameTime() + event.getPartialTick();
        AnimationController.INSTANCE.render(event.getGuiGraphics(), gameTime);
    }
}
