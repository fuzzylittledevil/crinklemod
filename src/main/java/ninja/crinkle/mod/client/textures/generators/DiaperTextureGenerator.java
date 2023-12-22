package ninja.crinkle.mod.client.textures.generators;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.models.DiaperArmorModel;
import ninja.crinkle.mod.undergarment.Undergarment;
import ninja.crinkle.mod.util.MathUtil;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoVertex;

import java.util.*;
import java.util.function.Function;

/**
 * The DiaperTextureGenerator class is responsible for generating diaper textures based on the fullness of the diaper.
 * It implements the TextureGenerator interface and specifically generates textures for Undergarment objects.
 */
public class DiaperTextureGenerator implements TextureGenerator<Undergarment> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final EnumSet<Part> parts = EnumSet.noneOf(Part.class);
    private final Function<Undergarment, Double> percentFunction;
    private final Map<Integer, Color> fillColors;
    private final Set<Color> toReplace;
    public static final Map<Integer, Color> WET_COLORS = Map.of(
            0, Color.TRANSPARENT,
            20, Color.of("#f6f5af"),
            40, Color.of("#f9f7b8"),
            60, Color.of("#fdfcc2"),
            80, Color.of("#fefdcb"),
            100, Color.of("#fefdda")
    );
    public static final Map<Integer, Color> MESS_COLORS = Map.of(
            0, Color.TRANSPARENT,
            20, Color.of("#c2b98e"),
            40, Color.of("#c9c19a"),
            60, Color.of("#d0c8a2"),
            80, Color.of("#e2d9b1"),
            100, Color.of("#f4edcb")
    );

    public enum Part {
        FRONT_TOP("front"),
        FRONT_BOTTOM("front_bottom"),
        BACK_TOP("back"),
        BACK_BOTTOM("back_bottom"),
        BOTTOM("bottom"),
        ;

        private final String bone;

        Part(String pBone) {
            this.bone = pBone;
        }

        public String getBone() {
            return bone;
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

    public DiaperTextureGenerator(Function<Undergarment, Double> percentFunction, Map<Integer, Color> fillColors,
                                  Set<Color> toReplace, Part... parts) {
        this.percentFunction = percentFunction;
        this.parts.addAll(List.of(parts));
        this.fillColors = fillColors;
        this.toReplace = toReplace;
    }

    private void applyPixels(NativeImage pImage, Data pData,
                             TriConsumer<NativeImage, Integer, Integer> applyFunction) {
        BakedGeoModel model = pData.model().getBakedModel(pData.model().getModelResource(null));
        parts.forEach(part -> model.getBone(part.getBone()).ifPresentOrElse(b -> b.getCubes().forEach(c ->
                List.of(c.quads()).forEach(f -> {
                    List<GeoVertex> vertices = new ArrayList<>(List.of(f.vertices()));
                    vertices.sort(Comparator.comparingDouble(GeoVertex::texU).
                            thenComparingDouble(GeoVertex::texV));
                    GeoVertex v1 = vertices.get(0);
                    GeoVertex v2 = vertices.get(vertices.size() - 1);
                    int x = (int) (v1.texU() * pImage.getWidth());
                    int y = (int) (v1.texV() * pImage.getHeight());
                    int width = (int) ((v2.texU() - v1.texU()) * pImage.getWidth());
                    int height = (int) ((v2.texV() - v1.texV()) * pImage.getHeight());
                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            applyFunction.accept(pImage, x + i, y + j);
                        }
                    }
                })), () -> LOGGER.warn("Could not find bone: {}", part.getBone())));
    }

    @Override
    public @NotNull NativeImage apply(@NotNull NativeImage pImage, @NotNull TextureData pData) {
        Data data = (Data) pData;
        double fullness = percentFunction.apply(data.undergarment());
        if (fullness == 0) return pImage;

        // First we create a NativeImage of just the diaper usage
        NativeImage image;
        try (NativeImage base = new NativeImage(pImage.getWidth(), pImage.getHeight(), false)) {
            Color fillColor = fillColors.get(MathUtil.twenties((int) (fullness * 100)));
            applyPixels(base, data, (img, x, y) -> img.setPixelRGBA(x, y, fillColor.ABGR()));

            // Next, we copy the incoming texture over the diaper usage, white pixels only
            image = new NativeImage(pImage.getWidth(), pImage.getHeight(), false);
            image.copyFrom(pImage);
            applyPixels(image, data, (img, x, y) -> {
                int pixel = img.getPixelRGBA(x, y);
                if (toReplace.stream().anyMatch(c -> c.ABGR() == pixel)) {
                    Color basePixel = Color.of(base.getPixelRGBA(x, y));
                    img.setPixelRGBA(x, y, basePixel.withAlpha(1.0).color());
                }
            });
        }
        return image;
    }
}
