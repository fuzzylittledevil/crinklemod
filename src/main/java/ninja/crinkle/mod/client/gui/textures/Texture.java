package ninja.crinkle.mod.client.gui.textures;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.gui.GenericBuilder;
import ninja.crinkle.mod.client.gui.properties.Bounds;
import ninja.crinkle.mod.client.gui.properties.Box;
import ninja.crinkle.mod.client.gui.properties.Point;
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

    public Bounds boundsOf(Slice.Location location) {
        if (slices.containsKey(location)) {
            return slices.get(location).bounds();
        }
        return Bounds.ZERO;
    }

    public ResourceLocation resourceLocation() {
        return ThemeAtlas.getTextureLocation(theme.getId(), id())
                .orElse(new ResourceLocation("minecraft", "missingno"));
    }

    @SuppressWarnings("resource")
    public @NotNull ResourceLocation generate(String key, @NotNull Bounds bounds, @NotNull Theme theme) {
        if (!isLoadedFor(theme)) {
            LOGGER.warn("Texture '{}' could not be created since '{}' is not loaded.", key, ThemeAtlas.getTextureLocation(theme.getId(), id()));
            return new ResourceLocation("minecraft", "missingno");
        }
        TextureAtlasSprite sprite = ThemeAtlas.getSprite(resourceLocation());
        NativeImage original = new NativeImage(sprite.contents().width(), sprite.contents().height(), true);
        original.copyFrom(sprite.contents().getOriginalImage());
        NativeImage image = new NativeImage(bounds.width(), bounds.height(), true);

        for (var entry : slices.entrySet()) {
            Slice.Location location = entry.getKey();
            Slice slice = entry.getValue();
            switch (location) {
                case topLeft, topRight, bottomLeft, bottomRight -> {
                    // The x,y coords of the original image
                    final Point from = slice.start();
                    // The x,y coords of the destination image
                    final Point to = switch (location) {
                        case topLeft -> new Point(0, 0);
                        case topRight -> new Point(bounds.width() - slice.bounds().width(), 0);
                        case bottomLeft -> new Point(0, bounds.height() - slice.bounds().height());
                        case bottomRight -> new Point(bounds.width() - slice.bounds().width(),
                                bounds.height() - slice.bounds().height());
                        default -> throw new IllegalStateException("Unexpected value: " + location);
                    };
                    original.copyRect(image, (int) from.x(), (int) from.y(), (int) to.x(), (int) to.y(), slice.bounds().width(),
                            slice.bounds().height(), false, false);
                }
                case top, bottom -> {
                    Slice corner = slices.get(location == Slice.Location.top ?
                            Slice.Location.topLeft : Slice.Location.bottomLeft);
                    double startX = corner.start().add(corner.bounds()).x();
                    for (double x = startX; x < bounds.subtract(corner.bounds()).width(); x += slice.bounds().width()) {
                        // The x,y coords of the original image
                        final Point from = slice.start();
                        // The x,y coords of the destination image
                        final Point to = switch (location) {
                            case top -> new Point(x, 0);
                            case bottom -> new Point(x, bounds.height() - slice.bounds().height());
                            default -> throw new IllegalStateException("Unexpected value: " + location);
                        };
                        original.copyRect(image, (int) from.x(), (int) from.y(), (int) to.x(), (int) to.y(), slice.bounds().width(),
                                slice.bounds().height(), false, false);
                    }
                }
                case left, right -> {
                    Slice corner = slices.get(location == Slice.Location.left ?
                            Slice.Location.topLeft : Slice.Location.topRight);
                    double startY = corner.start().add(corner.bounds()).y();
                    for (double y = startY; y < bounds.subtract(corner.bounds()).height(); y += slice.bounds().height()) {
                        // The x,y coords of the original image
                        final Point from = slice.start();
                        // The x,y coords of the destination image
                        final Point to = switch (location) {
                            case left -> new Point(0, y);
                            case right -> new Point(bounds.width() - slice.bounds().width(), y);
                            default -> throw new IllegalStateException("Unexpected value: " + location);
                        };
                        original.copyRect(image, (int) from.x(), (int) from.y(), (int) to.x(), (int) to.y(), slice.bounds().width(),
                                slice.bounds().height(), false, false);
                    }
                }
                case center -> {
                    Slice top = slices.get(Slice.Location.top);
                    Slice left = slices.get(Slice.Location.left);
                    Slice bottom = slices.get(Slice.Location.bottom);
                    Slice right = slices.get(Slice.Location.right);
                    double startX = left.start().add(left.bounds()).x();
                    double startY = top.start().add(top.bounds()).y();
                    for (double x = startX; x < bounds.subtract(right.bounds()).width(); x += slice.bounds().width()) {
                        for (double y = startY; y < bounds.subtract(bottom.bounds()).height(); y += slice.bounds().height()) {
                            // The x,y coords of the original image
                            final Point from = slice.start();
                            // The x,y coords of the destination image
                            final Point to = new Point(x, y);
                            original.copyRect(image, (int) from.x(), (int) from.y(), (int) to.x(), (int) to.y(), slice.bounds().width(),
                                    slice.bounds().height(), false, false);
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
        WidgetAppearance widgetAppearance = widget.getWidgetTheme().getAppearance(widget.state());
        Color color = widgetAppearance.getBackgroundColor().get();
        graphics.setColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(),
                widget.alpha());
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        Box borderBox = widget.getBorderTextureBox();
        ResourceLocation texture = theme.generateTexture(this, borderBox.bounds());
        graphics.blit(texture, (int) borderBox.topLeft().x(), (int) borderBox.topLeft().y(), 0, 0, 0,
                borderBox.bounds().width(), borderBox.bounds().height(), borderBox.bounds().width(),
                borderBox.bounds().height());
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

        public Builder addSlice(Slice.Location location, Point start, Bounds bounds) {
            return addSlice(location, new Slice(start, bounds));
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

    public record Slice(Point start, Bounds bounds) {
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
                        topLeft, new Slice(new Point(0, 0), new Bounds(0, 0)),
                        top, new Slice(new Point(0, 0), new Bounds(0, 0)),
                        topRight, new Slice(new Point(0, 0), new Bounds(0, 0)),
                        left, new Slice(new Point(0, 0), new Bounds(0, 0)),
                        center, new Slice(new Point(0, 0), new Bounds(0, 0)),
                        right, new Slice(new Point(0, 0), new Bounds(0, 0)),
                        bottomLeft, new Slice(new Point(0, 0), new Bounds(0, 0)),
                        bottom, new Slice(new Point(0, 0), new Bounds(0, 0)),
                        bottomRight, new Slice(new Point(0, 0), new Bounds(0, 0))
                );
            }
        }
    }
}
