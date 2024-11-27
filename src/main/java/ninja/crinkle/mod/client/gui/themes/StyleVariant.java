package ninja.crinkle.mod.client.gui.themes;

import com.mojang.logging.LogUtils;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.gui.builders.GenericBuilder;
import ninja.crinkle.mod.client.gui.properties.Box;
import ninja.crinkle.mod.client.gui.renderers.ThemeGraphics;
import ninja.crinkle.mod.client.gui.textures.ColorFilters;
import ninja.crinkle.mod.client.gui.textures.Texture;
import ninja.crinkle.mod.client.gui.widgets.AbstractWidget;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.IntUnaryOperator;

public class StyleVariant {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final StyleVariant EMPTY = StyleVariant.builder().build();
    private final Color backgroundColor;
    private final List<ColorFilters> backgroundColorFilters = new ArrayList<>();
    private final Texture backgroundTexture;
    private final Color foregroundColor;
    private final List<ColorFilters> foregroundColorFilters = new ArrayList<>();
    private final Texture foregroundTexture;
    private final boolean shadow;

    public StyleVariant(Texture backgroundTexture, Texture foregroundTexture, Color backgroundColor,
                        Color foregroundColor, boolean shadow, List<ColorFilters> foregroundColorFilters,
                        List<ColorFilters> backgroundColorFilters) {
        this.backgroundTexture = backgroundTexture;
        this.foregroundTexture = foregroundTexture;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.shadow = shadow;
        this.foregroundColorFilters.addAll(foregroundColorFilters);
        this.backgroundColorFilters.addAll(backgroundColorFilters);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static StyleVariant getDefault(String widgetTheme) {
        return Optional.of(ThemeRegistry.getDefault().getWidgetTheme(widgetTheme)).map(wt -> wt.getAppearance(Style.Variant.active)).orElse(StyleVariant.EMPTY);
    }

    public List<ColorFilters> backgroundColorFilters() {
        return backgroundColorFilters;
    }

    public List<ColorFilters> foregroundColorFilters() {
        return foregroundColorFilters;
    }

    public @Nullable Color getBackgroundColor() {
        return backgroundColor;
    }

    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    public List<IntUnaryOperator> getColorFilters() {
        return List.of();
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Texture getForegroundTexture() {
        return foregroundTexture;
    }

    public boolean hasShadow() {
        return shadow;
    }

    public void render(ThemeGraphics pGuiGraphics, Box pBox, AbstractWidget widget) {
        if (backgroundTexture != null) {
            backgroundTexture.render(pGuiGraphics, widget, backgroundColorFilters);
        } else if (backgroundColor != null) {
            pGuiGraphics.fill(pBox, backgroundColor, widget.zIndex());
        }

        if (foregroundTexture != null) {
            foregroundTexture.render(pGuiGraphics, widget, foregroundColorFilters);
        }
    }

    public StyleVariant coalesceWith(StyleVariant other) {
        if (other == null) {
            return this;
        }
        return new StyleVariant(
                Optional.ofNullable(other.getBackgroundTexture()).orElse(getBackgroundTexture()),
                Optional.ofNullable(other.getForegroundTexture()).orElse(getForegroundTexture()),
                Optional.ofNullable(other.getBackgroundColor()).orElse(getBackgroundColor()),
                Optional.ofNullable(other.getForegroundColor()).orElse(getForegroundColor()),
                other.hasShadow() || shadow,
                other.foregroundColorFilters.isEmpty() ? foregroundColorFilters : other.foregroundColorFilters,
                other.backgroundColorFilters.isEmpty() ? backgroundColorFilters : other.backgroundColorFilters
        );
    }

    public static StyleVariant coalesce(StyleVariant a, StyleVariant b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (a == b) {
            return a;
        }

        StyleVariant variant = a.coalesceWith(b);
        LOGGER.debug("Coalesced {} with {} to {}", a, b, variant);
        return variant;
    }

    @Override
    public String toString() {
        return "StyleVariant{" + "backgroundColor=" + backgroundColor + ", backgroundTexture=" + backgroundTexture + ", foregroundColor=" + foregroundColor + ", foregroundTexture=" + foregroundTexture + ", shadow=" + shadow + '}';
    }

    public static class Builder extends GenericBuilder<Builder, StyleVariant> {
        private final List<ColorFilters> foregroundColorFilters = new ArrayList<>();
        private final List<ColorFilters> backgroundColorFilters = new ArrayList<>();
        private Color backgroundColor;
        private Texture backgroundTexture;
        private Color foregroundColor;
        private Texture foregroundTexture;
        private boolean shadow;

        public Builder addForegroundColorFilter(ColorFilters filter) {
            foregroundColorFilters.add(filter);
            return self();
        }

        public Builder addForegroundColorFilters(List<ColorFilters> filters) {
            foregroundColorFilters.addAll(filters);
            return self();
        }

        public Builder addBackgroundColorFilter(ColorFilters filter) {
            backgroundColorFilters.add(filter);
            return self();
        }

        public Builder addBackgroundColorFilters(List<ColorFilters> filters) {
            backgroundColorFilters.addAll(filters);
            return self();
        }

        public Builder backgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return self();
        }

        public Builder backgroundTexture(Texture backgroundTexture) {
            this.backgroundTexture = backgroundTexture;
            return self();
        }

        public StyleVariant build() {
            return new StyleVariant(backgroundTexture, foregroundTexture, backgroundColor, foregroundColor,
                    shadow, foregroundColorFilters, backgroundColorFilters);
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Builder foregroundColor(Color foregroundColor) {
            this.foregroundColor = foregroundColor;
            return self();
        }

        public Builder foregroundTexture(Texture foregroundTexture) {
            this.foregroundTexture = foregroundTexture;
            return self();
        }

        public Color getBackgroundColor() {
            return backgroundColor;
        }

        public Texture getBackgroundTexture() {
            return backgroundTexture;
        }

        public Color getForegroundColor() {
            return foregroundColor;
        }

        public Texture getForegroundTexture() {
            return foregroundTexture;
        }

        public boolean isShadow() {
            return shadow;
        }

        public Builder shadow(boolean shadow) {
            this.shadow = shadow;
            return self();
        }
    }
}
