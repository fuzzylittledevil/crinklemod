package ninja.crinkle.mod.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.icons.Icons;

public class IconRenderer {
    private static final ResourceLocation ICONS = new ResourceLocation(CrinkleMod.MODID, "textures/gui/themes/icons.png");
    private static final int ICON_SIZE = 13;
    private static final int TEXTURE_SIZE = 26;


    public static void renderIcon(GuiGraphics graphics, Icons icon, int x, int y) {
        graphics.blit(ICONS, x, y, icon.getX() * ICON_SIZE, icon.getY() * ICON_SIZE,
                ICON_SIZE, ICON_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
    }

    public static void renderIcon(GuiGraphics graphics, Icons icon, int x, int y, Color color) {
        // Get RGBA from int
        graphics.setColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(),
                (float) color.getAlpha());
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        renderIcon(graphics, icon, x, y);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

}
