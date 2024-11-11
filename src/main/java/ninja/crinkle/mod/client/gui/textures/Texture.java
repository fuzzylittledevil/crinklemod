package ninja.crinkle.mod.client.gui.textures;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.gui.builders.GenericBuilder;
import ninja.crinkle.mod.client.gui.properties.*;
import ninja.crinkle.mod.client.gui.renderers.ThemeGraphics;
import ninja.crinkle.mod.client.gui.themes.Theme;
import ninja.crinkle.mod.client.gui.themes.WidgetAppearance;
import ninja.crinkle.mod.client.gui.widgets.AbstractWidget;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public record Texture(String id, String location, Map<Slice.Location, Slice> slices, Theme theme) {
    public static final Texture EMPTY = new Texture("empty", "missingno", Slice.Location.emptySliceMap(), ninja.crinkle.mod.client.gui.themes.Theme.EMPTY);
    private static final Logger LOGGER = LogUtils.getLogger();

    public Size boundsOf(Slice.Location location) {
        if (slices().containsKey(location)) {
            return slices().get(location).size();
        }
        return Size.ZERO;
    }

    public boolean isSliced() {
        return slices().size() > 1;
    }

    public ResourceLocation resourceLocation() {
        return ThemeAtlas.getTextureLocation(theme.getId(), id())
                .orElse(new ResourceLocation("minecraft", "missingno"));
    }

    private boolean isOutOfBounds(Box a, Slice b, String key) {
        if (!a.contains(b.box().bottomRight())) {
            LOGGER.warn("Texture '{}' could not be created since the slice '{}' is out of bounds. a bounds: {}, b bounds: {}", key, b, a, b.box());
            return true;
        }
        return false;
    }

    @SuppressWarnings("resource")
    public @NotNull ResourceLocation generate(String key, @NotNull Size size, @NotNull Theme theme) {
        if (!isLoadedFor(theme)) {
            LOGGER.warn("Texture '{}' could not be created since '{}' is not loaded.", key, ThemeAtlas.getTextureLocation(theme.getId(), id()));
            return new ResourceLocation("minecraft", "missingno");
        }
        TextureAtlasSprite sprite = ThemeAtlas.getSprite(resourceLocation());
        NativeImage original = new NativeImage(sprite.contents().width(), sprite.contents().height(), true);
        Box originalBox = new Box(0, 0, original.getWidth(), original.getHeight());
        original.copyFrom(sprite.contents().getOriginalImage());
        NativeImage image = new NativeImage(size.width(), size.height(), true);

        for (var entry : slices.entrySet()) {
            Slice.Location location = entry.getKey();
            Slice slice = entry.getValue();
            switch (location) {
                case topLeft, topRight, bottomLeft, bottomRight -> {
                    // The x,y coords of the original image
                    final Point from = slice.start();
                    // The x,y coords of the destination image
                    final Point to = switch (location) {
                        case topLeft -> new ImmutablePoint(0, 0);
                        case topRight -> new ImmutablePoint(size.width() - slice.size().width(), 0);
                        case bottomLeft -> new ImmutablePoint(0, size.height() - slice.size().height());
                        case bottomRight -> new ImmutablePoint(size.width() - slice.size().width(),
                                size.height() - slice.size().height());
                        default -> throw new IllegalStateException("Unexpected value: " + location);
                    };
                    if (isOutOfBounds(originalBox, slice, key)) {
                        return new ResourceLocation("minecraft", "missingno");
                    }
                    original.copyRect(image, (int) from.x(), (int) from.y(), (int) to.x(), (int) to.y(), slice.size().width(),
                            slice.size().height(), false, false);
                }
                case top, bottom -> {
                    Slice corner = slices.get(location == Slice.Location.top ?
                            Slice.Location.topLeft : Slice.Location.bottomLeft);
                    double startX = corner.start().add(corner.size()).x();
                    for (double x = startX; x < size.subtract(corner.size()).width(); x += slice.size().width()) {
                        // The x,y coords of the original image
                        final Point from = slice.start();
                        // The x,y coords of the destination image
                        final Point to = switch (location) {
                            case top -> new ImmutablePoint(x, 0);
                            case bottom -> new ImmutablePoint(x, size.height() - slice.size().height());
                            default -> throw new IllegalStateException("Unexpected value: " + location);
                        };
                        if (isOutOfBounds(originalBox, slice, key)) {
                            return new ResourceLocation("minecraft", "missingno");
                        }
                        original.copyRect(image, (int) from.x(), (int) from.y(), (int) to.x(), (int) to.y(), slice.size().width(),
                                slice.size().height(), false, false);
                    }
                }
                case left, right -> {
                    Slice corner = slices.get(location == Slice.Location.left ?
                            Slice.Location.topLeft : Slice.Location.topRight);
                    double startY = corner.start().add(corner.size()).y();
                    for (double y = startY; y < size.subtract(corner.size()).height(); y += slice.size().height()) {
                        // The x,y coords of the original image
                        final Point from = slice.start();
                        // The x,y coords of the destination image
                        final Point to = switch (location) {
                            case left -> new ImmutablePoint(0, y);
                            case right -> new ImmutablePoint(size.width() - slice.size().width(), y);
                            default -> throw new IllegalStateException("Unexpected value: " + location);
                        };
                        if (isOutOfBounds(originalBox, slice, key)) {
                            return new ResourceLocation("minecraft", "missingno");
                        }
                        original.copyRect(image, (int) from.x(), (int) from.y(), (int) to.x(), (int) to.y(), slice.size().width(),
                                slice.size().height(), false, false);
                    }
                }
                case center -> {
                    Slice top = slices.get(Slice.Location.top);
                    Slice left = slices.get(Slice.Location.left);
                    Slice bottom = slices.get(Slice.Location.bottom);
                    Slice right = slices.get(Slice.Location.right);
                    double startX = left.start().add(left.size()).x();
                    double startY = top.start().add(top.size()).y();
                    for (double x = startX; x < size.subtract(right.size()).width(); x += slice.size().width()) {
                        for (double y = startY; y < size.subtract(bottom.size()).height(); y += slice.size().height()) {
                            // The x,y coords of the original image
                            final Point from = slice.start();
                            // The x,y coords of the destination image
                            final Point to = new ImmutablePoint(x, y);
                            if (isOutOfBounds(originalBox, slice, key)) {
                                return new ResourceLocation("minecraft", "missingno");
                            }
                            original.copyRect(image, (int) from.x(), (int) from.y(), (int) to.x(), (int) to.y(), slice.size().width(),
                                    slice.size().height(), false, false);
                        }
                    }
                }
            }
        }
        // We can't close the new image or the sprite contents or Minecraft will lose reference to them
        original.close();
        return ThemeAtlas.register(key, new DynamicTexture(image));
    }

    public boolean isLoadedFor(Theme theme) {
        return ThemeAtlas.hasTexture(theme.getId(), id());
    }

    public void render(@NotNull ThemeGraphics graphics, @NotNull AbstractWidget widget) {
        if (this == EMPTY) {
            return;
        }
        WidgetAppearance widgetAppearance = widget.widgetTheme().getAppearance(widget.status());
        if (widgetAppearance == null) {
            widgetAppearance = widget.widgetTheme().getAppearance(Status.active);
        }
        Color color = widgetAppearance.getBackgroundColor().get();
        graphics.setColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(),
                widget.alpha());
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        Box background = widget.layout().boxes().rendered().backgroundBox();
        ResourceLocation texture = theme.generateTexture(this, background.size());
        graphics.blit(texture, (int) background.topLeft().x(), (int) background.topLeft().y(),
                widget.zIndex(), 0, 0,
                background.size().width(), background.size().height(), background.size().width(),
                background.size().height());
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
    }

    public static class Builder extends GenericBuilder<Builder, Texture> {
        private final Map<Slice.Location, Slice> slices = new HashMap<>(Slice.Location.emptySliceMap());
        private final String id;
        private final Theme theme;
        private String location = "missingno";

        public Builder(String id, Theme theme) {
            this.id = id;
            this.theme = theme;
        }

        public Builder addSlice(Slice.Location location, Slice slice) {
            slices.put(location, slice);
            return self();
        }

        public Builder addSlice(Slice.Location location, Point start, Size size) {
            return addSlice(location, new Slice(ImmutablePoint.from(start), size));
        }

        public Builder location(String location) {
            this.location = location;
            return self();
        }

        @Override
        public Texture build() {
            return new Texture(id, location, slices, theme);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public record Slice(ImmutablePoint start, Size size) {
        public Slice(int x, int y, int width, int height) {
            this(new ImmutablePoint(x, y), new Size(width, height));
        }

        public Box box() {
            return new Box(start, size);
        }
        public enum Location {
            topLeft,
            top,
            topRight,
            left,
            center,
            right,
            bottomLeft,
            bottom,
            bottomRight;

            public static Map<Location, Slice> emptySliceMap() {
                return Map.of(
                        topLeft, new Slice(new ImmutablePoint(0, 0), new Size(0, 0)),
                        top, new Slice(new ImmutablePoint(0, 0), new Size(0, 0)),
                        topRight, new Slice(new ImmutablePoint(0, 0), new Size(0, 0)),
                        left, new Slice(new ImmutablePoint(0, 0), new Size(0, 0)),
                        center, new Slice(new ImmutablePoint(0, 0), new Size(0, 0)),
                        right, new Slice(new ImmutablePoint(0, 0), new Size(0, 0)),
                        bottomLeft, new Slice(new ImmutablePoint(0, 0), new Size(0, 0)),
                        bottom, new Slice(new ImmutablePoint(0, 0), new Size(0, 0)),
                        bottomRight, new Slice(new ImmutablePoint(0, 0), new Size(0, 0))
                );
            }
        }
    }
}
