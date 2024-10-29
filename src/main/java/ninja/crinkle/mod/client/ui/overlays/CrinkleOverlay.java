package ninja.crinkle.mod.client.ui.overlays;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.icons.Icons;
import ninja.crinkle.mod.client.renderers.GraphicsUtil;
import ninja.crinkle.mod.client.ui.themes.BoxTheme;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedBorderBox;
import ninja.crinkle.mod.undergarment.Undergarment;
import org.slf4j.Logger;

import java.util.Optional;

public class CrinkleOverlay {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final IGuiOverlay HUD = (forgeGui, guiGraphics, v, w, h) -> {
        int x = w / 2 + 100;
        int y = h - 21;

        LocalPlayer player = Optional.ofNullable(forgeGui.getMinecraft().player)
                .orElseThrow(() -> new IllegalStateException("Player is null"));

        ItemStack undergarment = Undergarment.getWornUndergarment(player);
        if (undergarment.isEmpty())
            return;
        renderUndergarmentSlot(guiGraphics, x, y, undergarment);
        Undergarment data = Undergarment.of(undergarment);

        if (data.getLiquidsPercent() >= 1.0f && player.tickCount % 20 < 10)
            renderIndicatorIcon(guiGraphics, x + 24, y + 2, Icons.WETNESS_DANGER, data.getLiquidsPercent());
        else
            renderIndicatorIcon(guiGraphics, x + 24, y + 2, Icons.WETNESS_OUTLINED, data.getLiquidsPercent());

        if (data.getSolidsPercent() >= 1.0f && player.tickCount % 20 < 10)
            renderIndicatorIcon(guiGraphics, x + 42, y + 2, Icons.MESSINESS_DANGER, data.getSolidsPercent());
        else
            renderIndicatorIcon(guiGraphics, x + 42, y + 2, Icons.MESSINESS_OUTLINED, data.getSolidsPercent());

    };

    private static void renderUndergarmentSlot(GuiGraphics guiGraphics, int x, int y, ItemStack undergarment) {
        ThemedBorderBox borderBox =
                new ThemedBorderBox(x, y, 20, 20, Component.literal("undergarment slot"),
                        Theme.DEFAULT, BoxTheme.Type.CHECKBOX);
        borderBox.render(guiGraphics, 0, 0, 0);
        guiGraphics.renderItem(undergarment, x + 2, y + 2);
    }

    private static void renderIndicatorIcon(GuiGraphics guiGraphics, int x, int y, Icons icon, double percent) {
        GraphicsUtil renderer = new GraphicsUtil(guiGraphics);
        if (percent < 1.0f)
            renderer.render(icon, x, y, Color.of("#808080").withAlpha(1.0f));
        if (percent > 0) {
            renderer.render(icon, x, y, icon.width(), icon.height(), (float) percent, 1.0f, Color.WHITE);
        }
    }
}
