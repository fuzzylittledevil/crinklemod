package ninja.crinkle.mod.client.gui.themes;

import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.gui.builders.GenericBuilder;
import ninja.crinkle.mod.client.gui.properties.Box;
import ninja.crinkle.mod.client.gui.properties.Status;
import ninja.crinkle.mod.client.gui.renderers.ThemeGraphics;
import ninja.crinkle.mod.client.gui.textures.Texture;
import ninja.crinkle.mod.client.gui.widgets.AbstractWidget;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class WidgetAppearance {
    public static final WidgetAppearance EMPTY = WidgetAppearance.builder()
            .backgroundColor(Color.of("#cecece"))
            .backgroundTexture(Texture.EMPTY)
            .foregroundColor(Color.DEFAULT_TEXT)
            .foregroundTexture(Texture.EMPTY)
            .build();
    private final Color backgroundColor;
    private final Texture backgroundTexture;
    private final Color foregroundColor;
    private final Texture foregroundTexture;
    private final boolean shadow;

    public WidgetAppearance(Texture backgroundTexture, Texture foregroundTexture, Color backgroundColor,
                            Color foregroundColor, boolean shadow) {
        this.backgroundTexture = backgroundTexture;
        this.foregroundTexture = foregroundTexture;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.shadow = shadow;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static WidgetAppearance getDefault(String widgetTheme) {
        return Optional.of(ThemeRegistry.getDefault().getWidgetTheme(widgetTheme))
                .map(wt -> wt.getAppearance(Status.active))
                .orElse(WidgetAppearance.EMPTY);
    }

    public @NotNull Color getBackgroundColor() {
        return backgroundColor != null ? backgroundColor : Color.RAINBOW;
    }

    public @NotNull Texture getBackgroundTexture() {
        return backgroundTexture != null ? backgroundTexture : Texture.EMPTY;
    }

    public @NotNull Color getForegroundColor() {
        return foregroundColor != null ? foregroundColor : Color.RAINBOW;
    }

    public @NotNull Texture getForegroundTexture() {
        return foregroundTexture != null ? foregroundTexture : Texture.EMPTY;
    }

    public boolean hasShadow() {
        return shadow;
    }

    public void render(ThemeGraphics pGuiGraphics, Box pBox, AbstractWidget widget) {
        if (backgroundTexture != null) {
            backgroundTexture.render(pGuiGraphics, widget);
        } else if (backgroundColor != null) {
            pGuiGraphics.fill(pBox, backgroundColor, widget.zIndex());
        }

        if (foregroundTexture != null) {
            foregroundTexture.render(pGuiGraphics, widget);
        }
    }

    @Override
    public String toString() {
        return "WidgetAppearance{" +
                "backgroundColor=" + backgroundColor +
                ", backgroundTexture=" + backgroundTexture +
                ", foregroundColor=" + foregroundColor +
                ", foregroundTexture=" + foregroundTexture +
                ", shadow=" + shadow +
                '}';
    }

    public static class Builder extends GenericBuilder<Builder, WidgetAppearance> {
        private Color backgroundColor;
        private Texture backgroundTexture;
        private Color foregroundColor;
        private Texture foregroundTexture;
        private boolean shadow;

        public Builder backgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return self();
        }

        public Builder backgroundTexture(Texture backgroundTexture) {
            this.backgroundTexture = backgroundTexture;
            return self();
        }

        public WidgetAppearance build() {
            return new WidgetAppearance(backgroundTexture, foregroundTexture, backgroundColor, foregroundColor, shadow);
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
