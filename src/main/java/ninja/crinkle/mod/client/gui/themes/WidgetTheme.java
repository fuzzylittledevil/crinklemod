package ninja.crinkle.mod.client.gui.themes;

import com.mojang.logging.LogUtils;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.gui.GenericBuilder;
import ninja.crinkle.mod.client.gui.properties.Box;
import ninja.crinkle.mod.client.gui.renderers.ThemeGraphics;
import ninja.crinkle.mod.client.gui.textures.Texture;
import ninja.crinkle.mod.client.gui.themes.loader.WidgetData;
import ninja.crinkle.mod.client.gui.widgets.AbstractWidget;
import ninja.crinkle.mod.client.gui.widgets.WidgetState;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record WidgetTheme(String id, Map<WidgetState, WidgetAppearance> appearances, Theme theme) {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final WidgetTheme EMPTY = WidgetTheme.builder("empty").theme(Theme.EMPTY).build();

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static WidgetTheme fromConfig(WidgetData widgetData, Theme theme) {
        Map<WidgetState, WidgetAppearance> appearances = new HashMap<>();
        for (var entry : widgetData.states().entrySet()) {
            WidgetState state = entry.getKey();
            WidgetData.Appearance appearance = entry.getValue();
            Texture background = Optional.ofNullable(appearance.background())
                    .filter(data -> data.texture() != null)
                    .map(data -> theme.getTexture(data.texture()))
                    .orElse(Texture.EMPTY);
            Texture foreground = Optional.ofNullable(appearance.foreground())
                    .filter(data -> data.texture() != null)
                    .map(data -> theme.getTexture(data.texture()))
                    .orElse(Texture.EMPTY);
            Color backgroundColor = Optional.ofNullable(appearance.background())
                    .filter(data -> data.color() != null)
                    .map(data -> theme.getColor(data.color()))
                    .orElse(Color.RAINBOW);
            Color foregroundColor = Optional.ofNullable(appearance.foreground())
                    .filter(data -> data.color() != null)
                    .map(data -> theme.getColor(data.color()))
                    .orElse(Color.RAINBOW);
            WidgetAppearance widgetAppearance = new WidgetAppearance(background, foreground, backgroundColor,
                    foregroundColor, appearance.foreground() != null && appearance.foreground().shadow());
            appearances.put(state, widgetAppearance);
        }
        return new WidgetTheme(widgetData.id(), appearances, theme);
    }

    public static WidgetTheme getDefault() {
        return ThemeRegistry.getDefault().getWidgetTheme("default");
    }

    public WidgetAppearance getAppearance(WidgetState state) {
        return appearances.get(state);
    }

    public void render(ThemeGraphics graphics, Box pBox, AbstractWidget widget) {
        WidgetAppearance widgetAppearance = appearances.get(widget.state());
        if (widgetAppearance == null) {
            LOGGER.warn("No appearance found for widget {} with state {}", widget.name(), widget.state());
            widgetAppearance = appearances.get(WidgetState.active);
        }
        if (widgetAppearance != null) {
            widgetAppearance.render(graphics, pBox, widget);
        } else {
            LOGGER.warn("No appearance found for widget {} at all", widget.name());
        }
    }

    @Override
    public String toString() {
        return "WidgetTheme{" +
                "id='" + id + '\'' +
                ", appearances=" + appearances +
                ", theme=" + theme +
                '}';
    }

    public static class Builder extends GenericBuilder<Builder, WidgetTheme> {
        private final Map<WidgetState, WidgetAppearance> appearances = new HashMap<>();
        private final String id;
        private Theme theme;

        public Builder(String id) {
            this.id = id;
        }

        public Builder addAppearance(WidgetState state, WidgetAppearance appearance) {
            appearances.put(state, appearance);
            return self();
        }

        @Override
        public WidgetTheme build() {
            return new WidgetTheme(id, appearances, theme);
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Builder theme(Theme theme) {
            this.theme = theme;
            return self();
        }
    }
}
