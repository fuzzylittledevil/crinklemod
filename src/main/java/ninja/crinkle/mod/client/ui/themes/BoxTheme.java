package ninja.crinkle.mod.client.ui.themes;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.util.ClientUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class BoxTheme {
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(CrinkleMod.MODID,
            "textures/gui/themes/gray.png");
    public static final BoxTheme GRAY_SMALL = new BoxTheme("gray_small", DEFAULT_TEXTURE,
            1, 1, 1, 1, 1, 3, 3,
            9, 5, 14, 9
    );
    public static final BoxTheme GRAY_MEDIUM = new BoxTheme("gray_medium", DEFAULT_TEXTURE,
            2, 2, 2, 2, 1, 5, 5,
            9, 0, 14, 9
    );

    public static final BoxTheme GRAY_LARGE = new BoxTheme("gray_large", DEFAULT_TEXTURE,
            4, 4, 4, 4, 1, 9, 9,
            0, 0, 14, 9
    );
    private final String name;
    private final ResourceLocation texture;
    private final int cornerWidth;
    private final int cornerHeight;
    private final int edgeWidth;
    private final int edgeHeight;
    private final int edgeSize;
    private final int uWidth;
    private final int vHeight;
    private final int uOffset;
    private final int vOffset;
    private final int textureWidth;
    private final int textureHeight;
    private ResourceLocation invertedTexture;
    private ResourceLocation inactiveTexture;
    private NativeImage invertedImage;
    private NativeImage inactiveImage;
    private final Map<String, ResourceLocation> slicedTextures = new HashMap<>();

    public BoxTheme(String name, ResourceLocation texture, int cornerWidth, int cornerHeight,
                    int edgeWidth, int edgeHeight, int edgeSize, int uWidth, int vHeight,
                    int uOffset, int vOffset, int textureWidth, int textureHeight) {
        this.name = name;
        this.texture = texture;
        this.cornerWidth = cornerWidth;
        this.cornerHeight = cornerHeight;
        this.edgeWidth = edgeWidth;
        this.edgeHeight = edgeHeight;
        this.edgeSize = edgeSize;
        this.uWidth = uWidth;
        this.vHeight = vHeight;
        this.uOffset = uOffset;
        this.vOffset = vOffset;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public int getTopLeftCornerU() {
        return uOffset;
    }

    public int getTopLeftCornerV() {
        return vOffset;
    }

    public int getTopRightCornerU() {
        return uOffset + cornerWidth + edgeSize;
    }

    public int getTopRightCornerV() {
        return vOffset;
    }

    public int getBottomLeftCornerU() {
        return uOffset;
    }

    public int getBottomLeftCornerV() {
        return vOffset + cornerHeight + edgeSize;
    }

    public int getBottomRightCornerU() {
        return uOffset + cornerWidth + edgeSize;
    }

    public int getBottomRightCornerV() {
        return vOffset + cornerHeight + edgeSize;
    }

    public int getTopEdgeU() {
        return uOffset + cornerWidth;
    }

    public int getTopEdgeV() {
        return vOffset;
    }

    public int getBottomEdgeU() {
        return uOffset + cornerWidth;
    }

    public int getBottomEdgeV() {
        return vOffset + cornerHeight + edgeSize;
    }

    public int getLeftEdgeU() {
        return uOffset;
    }

    public int getLeftEdgeV() {
        return vOffset + cornerHeight;
    }

    public int getRightEdgeU() {
        return uOffset + cornerWidth + edgeSize;
    }

    public int getRightEdgeV() {
        return vOffset + cornerHeight;
    }

    public int getBackgroundU() {
        return uOffset + cornerWidth;
    }

    public int getBackgroundV() {
        return vOffset + cornerHeight;
    }

    private NativeImage fromResource(ResourceLocation resourceLocation) {
        if (resourceLocation.equals(invertedTexture)) return invertedImage;
        if (resourceLocation.equals(inactiveTexture)) return inactiveImage;
        Minecraft minecraft = ClientUtil.getMinecraft();
        try {
            try (InputStream is = minecraft.getResourceManager().open(resourceLocation)) {
                return NativeImage.read(is);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getSlicedTexturePath(int width, int height, TextureType type) {
        return this.name() + "_" + width + "_" + height + "_" + type.name().toLowerCase();
    }

    public ResourceLocation draw(int width, int height, TextureType type) {
        String key = getSlicedTexturePath(width, height, type);
        if (slicedTextures.containsKey(key)) {
            return slicedTextures.get(key);
        }
        ResourceLocation texture = generateNineSliced(width, height, type);
        slicedTextures.put(key, texture);
        return texture;
    }

    private ResourceLocation generateNineSliced(int width, int height, TextureType type) {
        try (NativeImage texture = fromResource(getTexture(type))) {
            NativeImage image = new NativeImage(width, height, false);


            // Corners
            // top left
            texture.copyRect(image, this.getTopLeftCornerU(), this.getTopLeftCornerV(), 0, 0,
                    this.cornerWidth(), this.cornerHeight(), false, false);

            // top right
            texture.copyRect(image, this.getTopRightCornerU(), this.getTopRightCornerV(),
                    width - this.cornerWidth(), 0, this.cornerWidth(), this.cornerHeight(),
                    false, false);

            // bottom left
            texture.copyRect(image, this.getBottomLeftCornerU(), this.getBottomLeftCornerV(), 0,
                    height - this.cornerHeight(), this.cornerWidth(), this.cornerHeight(),
                    false, false);

            // bottom right
            texture.copyRect(image, this.getBottomRightCornerU(), this.getBottomRightCornerV(),
                    width - this.cornerWidth(), height - this.cornerHeight(), this.cornerWidth(),
                    this.cornerHeight(), false, false);

            // Top and bottom edges
            for (int x = this.cornerWidth(); x < width - this.cornerWidth(); x += this.edgeSize()) {
                // top
                texture.copyRect(image, this.getTopEdgeU(), this.getTopEdgeV(), x, 0,
                        this.edgeSize(), this.edgeHeight(), false, false);
                // bottom
                texture.copyRect(image, this.getBottomEdgeU(), this.getBottomEdgeV(), x,
                        height - this.edgeHeight(), this.edgeSize(), this.edgeHeight(),
                        false, false);
            }

            // Left and right edges
            for (int y = this.cornerHeight(); y < height - this.cornerHeight(); y += this.edgeSize()) {
                // left
                texture.copyRect(image, this.getLeftEdgeU(), this.getLeftEdgeV(), 0, y,
                        this.edgeWidth(), this.edgeSize(), false, false);
                // right
                texture.copyRect(image, this.getRightEdgeU(), this.getRightEdgeV(),
                        width - this.edgeWidth(), y, this.edgeWidth(), this.edgeSize(),
                        false, false);
            }

            // Center
            // we just want to draw a square the same color as our background pixel
            int backgroundColor = texture.getPixelRGBA(this.getBackgroundU(), this.getBackgroundV());
            image.fillRect(this.cornerWidth(), this.cornerHeight(), width - this.cornerWidth() * 2,
                    height - this.cornerHeight() * 2, backgroundColor);


            return ClientUtil.getMinecraft().getTextureManager().register(
                    getSlicedTexturePath(width, height, type),
                    new DynamicTexture(image)
            );
        }
    }

    private ResourceLocation getTexture(TextureType type) {
        return switch (type) {
            case INVERTED -> inverted();
            case INACTIVE -> inactive();
            default -> texture();
        };
    }

    private ResourceLocation inactive() {
        if (inactiveTexture == null || ClientUtil.getMinecraft().getResourceManager().getResource(inactiveTexture).isEmpty()) {
            inactiveTexture = inactiveTexture();
        }
        return texture();
    }

    private ResourceLocation inverted() {
        if (invertedTexture == null || ClientUtil.getMinecraft().getResourceManager().getResource(invertedTexture).isEmpty()) {
            invertedTexture = invertTexture();
        }
        return invertedTexture;
    }

    private ResourceLocation invertTexture() {
        Minecraft minecraft = ClientUtil.getMinecraft();
        TextureManager textureManager = minecraft.getTextureManager();
        try (InputStream is = minecraft.getResourceManager().open(texture())) {
            NativeImage image = NativeImage.read(is);
            // Invert the texture colors
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int color = image.getPixelRGBA(x, y);
                    int alpha = color & 0xFF000000;
                    int rgb = color & 0x00FFFFFF;
                    int inverted = alpha | (~rgb & 0x00FFFFFF);
                    image.setPixelRGBA(x, y, inverted);
                }
            }
            invertedImage = image;
            // Create a dynamic texture
            return textureManager.register(texture().getPath() + "_" + TextureType.INACTIVE.name().toLowerCase(), new DynamicTexture(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ResourceLocation inactiveTexture() {
        Minecraft minecraft = ClientUtil.getMinecraft();
        TextureManager textureManager = minecraft.getTextureManager();
        try (InputStream is = minecraft.getResourceManager().open(texture())) {
            NativeImage image = NativeImage.read(is);
            // Inactive should be grayscale
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int color = image.getPixelRGBA(x, y);
                    int alpha = color & 0xFF000000;
                    int rgb = color & 0x00FFFFFF;
                    int gray = (int) (0.21 * ((rgb >> 16) & 0xFF) + 0.72 * ((rgb >> 8) & 0xFF) + 0.07 * (rgb & 0xFF));
                    int grayColor = alpha | (gray << 16) | (gray << 8) | gray;
                    image.setPixelRGBA(x, y, grayColor);
                }
            }
            inactiveImage = image;
            // Create a dynamic texture
            return textureManager.register(texture().getPath() + "_inverted", new DynamicTexture(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String name() {
        return name;
    }

    public ResourceLocation texture() {
        return texture;
    }

    public int cornerWidth() {
        return cornerWidth;
    }

    public int cornerHeight() {
        return cornerHeight;
    }

    public int edgeWidth() {
        return edgeWidth;
    }

    public int edgeHeight() {
        return edgeHeight;
    }

    public int edgeSize() {
        return edgeSize;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BoxTheme) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.texture, that.texture) &&
                this.cornerWidth == that.cornerWidth &&
                this.cornerHeight == that.cornerHeight &&
                this.edgeWidth == that.edgeWidth &&
                this.edgeHeight == that.edgeHeight &&
                this.edgeSize == that.edgeSize &&
                this.uWidth == that.uWidth &&
                this.vHeight == that.vHeight &&
                this.uOffset == that.uOffset &&
                this.vOffset == that.vOffset &&
                this.textureWidth == that.textureWidth &&
                this.textureHeight == that.textureHeight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, texture, cornerWidth, cornerHeight, edgeWidth, edgeHeight, edgeSize, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
    }

    @Override
    public String toString() {
        return "BoxTheme[" +
                "name=" + name + ", " +
                "texture=" + texture + ", " +
                "cornerWidth=" + cornerWidth + ", " +
                "cornerHeight=" + cornerHeight + ", " +
                "edgeWidth=" + edgeWidth + ", " +
                "edgeHeight=" + edgeHeight + ", " +
                "edgeSize=" + edgeSize + ", " +
                "uWidth=" + uWidth + ", " +
                "vHeight=" + vHeight + ", " +
                "uOffset=" + uOffset + ", " +
                "vOffset=" + vOffset + ", " +
                "textureWidth=" + textureWidth + ", " +
                "textureHeight=" + textureHeight + ']';
    }


    public enum Size {
        SMALL,
        MEDIUM,
        LARGE
    }

    public enum TextureType {
        NORMAL,
        INVERTED,
        INACTIVE
    }
}
