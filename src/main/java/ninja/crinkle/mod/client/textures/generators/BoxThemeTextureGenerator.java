package ninja.crinkle.mod.client.textures.generators;

import com.mojang.blaze3d.platform.NativeImage;
import ninja.crinkle.mod.client.ui.themes.BoxTheme;
import org.jetbrains.annotations.NotNull;

public class BoxThemeTextureGenerator implements TextureGenerator<BoxTheme> {
    public record Data(BoxTheme boxTheme, BoxTheme.TextureType type, int width, int height) implements TextureData {
            @Override
            public String getName() {
                if (type != BoxTheme.TextureType.NORMAL)
                    return String.format("%s_%s_%dx%d", boxTheme.name(), type.name().toLowerCase(), width, height);
                return String.format("%s_%dx%d", boxTheme.name(), width, height);
            }
        }

    @Override
    public @NotNull NativeImage apply(@NotNull NativeImage pImage, @NotNull TextureData pData) {
        if(pData instanceof Data data) {
            NativeImage image = new NativeImage(data.width(), data.height(), true);
            BoxTheme box = data.boxTheme();
            // Corners
            // top left
            pImage.copyRect(image, 0, 0, 0, 0, box.cornerWidth(), box.cornerHeight(),
                    false, false);

            // top right
            pImage.copyRect(image, pImage.getWidth() - box.cornerWidth(), 0,
                    data.width() - box.cornerWidth(), 0, box.cornerWidth(), box.cornerHeight(),
                    false, false);

            // bottom left
            pImage.copyRect(image, 0, pImage.getHeight() - box.cornerHeight(), 0,
                    data.height() - box.cornerHeight(), box.cornerWidth(), box.cornerHeight(), false,
                    false);

            // bottom right
            pImage.copyRect(image, pImage.getWidth() - box.cornerWidth(),
                    pImage.getHeight() - box.cornerHeight(), data.width() - box.cornerWidth(),
                    data.height() - box.cornerHeight(), box.cornerWidth(), box.cornerHeight(), false,
                    false);

            // Top and bottom edges
            for (int x = box.cornerWidth(); x < data.width() - box.cornerWidth(); x += box.edgeSize()) {
                // top
                pImage.copyRect(image, box.cornerWidth(), 0, x, 0, box.edgeSize(), box.edgeHeight(),
                        false, false);
                // bottom
                pImage.copyRect(image, box.cornerWidth(), pImage.getHeight() - box.edgeHeight(), x,
                        data.height() - box.edgeHeight(), box.edgeSize(), box.edgeHeight(),
                        false, false);
            }

            // Left and right edges
            for (int y = box.cornerHeight(); y < data.height() - box.cornerHeight(); y += box.edgeSize()) {
                // left
                pImage.copyRect(image, 0, box.cornerHeight(), 0, y, box.edgeWidth(), box.edgeSize(),
                        false, false);
                // right
                pImage.copyRect(image, pImage.getWidth() - box.edgeWidth(), box.cornerHeight(),
                        data.width() - box.edgeWidth(), y, box.edgeWidth(), box.edgeSize(), false,
                        false);
            }

            // Center
            // we just want to draw a square the same color as our background pixel
            int backgroundColor = pImage.getPixelRGBA(box.cornerWidth(), box.cornerHeight());
            image.fillRect(box.cornerWidth(), box.cornerHeight(), data.width() - box.cornerWidth() * 2,
                    data.height() - box.cornerHeight() * 2, backgroundColor);
            return image;
        }
        throw new IllegalArgumentException("expected pData to be of type BoxThemeTextureGenerator.Data instead given " +
                pData.getClass().getName());
    }
}
