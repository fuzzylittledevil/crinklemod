package ninja.crinkle.mod.client.textures.generators;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.models.DiaperArmorModel;
import ninja.crinkle.mod.undergarment.Undergarment;
import ninja.crinkle.mod.util.MathUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoVertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;

public class DiaperTextureGenerator implements TextureGenerator<Undergarment> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final EnumSet<Part> parts = EnumSet.noneOf(Part.class);
    private final Color fillColor;
    private final Function<Undergarment, Double> percentFunction;

    public enum Part {
        FRONT_TOP("front", 1.0),
        FRONT_BOTTOM("front_bottom", 0.95),
        BACK_TOP("back", 1.0),
        BACK_BOTTOM("back_bottom", 0.95),
        BOTTOM("bottom", 0.9),
        ;

        private final String bone;
        private final double brightness;

        Part(String pBone, double brightness) {
            this.bone = pBone;
            this.brightness = brightness;
        }

        public String getBone() {
            return bone;
        }

        public double getBrightness() {
            return brightness;
        }
    }

    public record Data(String name, DiaperArmorModel model, Undergarment undergarment) implements TextureData {
        @Override
        public String getName() {
            int pctL = MathUtil.clamp(
                    MathUtil.twenties((int) (undergarment.getLiquidsPercent() * 100)), 0, 100);
            int pctS = MathUtil.clamp(
                    MathUtil.twenties((int) (undergarment.getSolidsPercent() * 100)), 0, 100);
            return String.format("%s_l%d_s%d", name, pctL, pctS);
        }
    }

    public DiaperTextureGenerator(Function<Undergarment, Double> percentFunction, Color fillColor, Part... parts) {
        this.percentFunction = percentFunction;
        this.parts.addAll(List.of(parts));
        this.fillColor = fillColor;
    }


    @Override
    public @NotNull NativeImage apply(@NotNull NativeImage pImage, @NotNull TextureData pData) {
        NativeImage image = new NativeImage(pImage.getWidth(), pImage.getHeight(), false);
        image.copyFrom(pImage);
        Data data = (Data) pData;
        double fullness = percentFunction.apply(data.undergarment());
        BakedGeoModel model = data.model().getBakedModel(data.model().getModelResource(null));
        if (fullness == 0) return image;
        parts.forEach(part -> model.getBone(part.getBone()).ifPresentOrElse(b -> b.getCubes().forEach(c ->
                List.of(c.quads()).forEach(f -> {
                    List<GeoVertex> vertices = new ArrayList<>(List.of(f.vertices()));
                    vertices.sort(Comparator.comparingDouble(GeoVertex::texU).
                            thenComparingDouble(GeoVertex::texV));
                    GeoVertex v1 = vertices.get(0);
                    GeoVertex v2 = vertices.get(vertices.size() - 1);
                    int x = (int) (v1.texU() * image.getWidth());
                    int y = (int) (v1.texV() * image.getHeight());
                    int width = (int) ((v2.texU() - v1.texU()) * image.getWidth());
                    int height = (int) ((v2.texV() - v1.texV()) * image.getHeight());
                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            image.blendPixel(x + i, y + j, fillColor.brightness(part.getBrightness())
                                    .withAlpha(fullness).ABGR());
                        }
                    }
                })), () -> LOGGER.warn("Could not find bone: {}", part.getBone())));
        return image;
    }
}
