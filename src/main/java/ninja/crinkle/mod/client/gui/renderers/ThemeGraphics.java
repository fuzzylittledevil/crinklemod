package ninja.crinkle.mod.client.gui.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.gui.properties.Box;
import ninja.crinkle.mod.client.gui.properties.Point;
import ninja.crinkle.mod.client.gui.properties.UV;
import ninja.crinkle.mod.client.gui.textures.Atlas;
import ninja.crinkle.mod.config.ClientConfig;
import ninja.crinkle.mod.util.ClientUtil;
import org.joml.Matrix4f;
import org.slf4j.Logger;

public class ThemeGraphics extends GuiGraphics {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final GuiGraphics graphics;
    private final Atlas atlas;

    public ThemeGraphics(GuiGraphics guiGraphics, Atlas atlas) {
        super(ClientUtil.getMinecraft(), guiGraphics.bufferSource());
        this.graphics = guiGraphics;
        this.atlas = atlas;
    }

    public Atlas atlas() {
        return atlas;
    }



    public void drawBox(Box pBox, Color pColor) {
        if (ClientConfig.debug() && ClientUtil.getMinecraft().gui.getGuiTicks() % 20 == 0) {
            LOGGER.debug("Drawing box: {}", pBox);
        }
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        fill(pBox, pColor);
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
    }

    public void drawCenteredString(Font font, Component text, int x, int y, int color, boolean shadow) {
        drawString(font, text, x - font.width(text) / 2, y - font.lineHeight / 2, color, shadow);
    }

    public void fill(Box pBox, Color pColor) {
        fill((int) pBox.topLeft().x(), (int) pBox.topLeft().y(),
                (int) pBox.bottomRight().x(), (int) pBox.bottomRight().y(), pColor.ABGR());
    }

    public void drawSprite(ResourceLocation sprite, int x, int y, int width, int height) {
        @SuppressWarnings("resource") TextureAtlasSprite tas = atlas().getSprite(sprite);
        drawSprite(tas, x, y, width, height, 1.0F, 1.0F, Color.WHITE);
    }

    public void drawSprite(TextureAtlasSprite sprite, int x, int y, int width, int height, float uPercent,
                           float vPercent, Color color) {
        float x2 = (float)(x + width) - (width * (1.0F - uPercent));
        float y2 = (float)(y + height) - (height * (1.0F - vPercent));
        float minU = sprite.getU0();
        float maxU = sprite.getU1() - (sprite.getU1() - sprite.getU0()) * (1.0F - uPercent);
        float minV = sprite.getV0();
        float maxV = sprite.getV1() - (sprite.getV1() - sprite.getV0()) * (1.0F - vPercent);
        drawSprite(sprite, new Point(x, y, 0), new Point((int) x2, (int) y2, 0), new UV(minU, minV), new UV(maxU, maxV), color);
    }

    public void drawSprite(TextureAtlasSprite sprite, Point start, Point end, UV min, UV max, Color color) {
        ResourceLocation atlasLocation = sprite.atlasLocation();
        RenderSystem.setShaderTexture(0, atlasLocation);
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        Matrix4f matrix4f = graphics().pose().last().pose();
        graphics().setColor(1.0F, 1.0F, 1.0F, 1.0F);
        BufferBuilder bufBuilder = Tesselator.getInstance().getBuilder();
        bufBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        Color c = color;
        if (c == Color.RAINBOW)
            c = Color.rainbow(1000, 0);
        bufBuilder.vertex(matrix4f, (float) start.x(), (float) start.y(), start.z()).color(c.ABGR()).uv(min.u(), min.v()).endVertex();
        bufBuilder.vertex(matrix4f, (float) start.x(), (float) end.y(), start.z()).color(c.ABGR()).uv(min.u(), max.v()).endVertex();
        bufBuilder.vertex(matrix4f, (float) end.x(), (float) end.y(), start.z()).color(c.ABGR()).uv(max.u(), max.v()).endVertex();
        bufBuilder.vertex(matrix4f, (float) end.x(), (float) start.y(), start.z()).color(c.ABGR()).uv(max.u(), min.v()).endVertex();
        BufferUploader.drawWithShader(bufBuilder.end());
    }

    public GuiGraphics graphics() {
        return graphics;
    }
}
