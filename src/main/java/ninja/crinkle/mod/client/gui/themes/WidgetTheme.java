package ninja.crinkle.mod.client.gui.themes;

import com.mojang.logging.LogUtils;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.gui.builders.GenericBuilder;
import ninja.crinkle.mod.client.gui.properties.Box;
import ninja.crinkle.mod.client.gui.properties.Status;
import ninja.crinkle.mod.client.gui.renderers.ThemeGraphics;
import ninja.crinkle.mod.client.gui.textures.Texture;
import ninja.crinkle.mod.client.gui.themes.loader.WidgetData;
import ninja.crinkle.mod.client.gui.widgets.AbstractWidget;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record WidgetTheme(String id, Map<Status, WidgetAppearance> appearances, Theme theme) {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final WidgetTheme EMPTY = WidgetTheme.builder("empty")
            .addAppearance(Status.active, WidgetAppearance.EMPTY)
            .addAppearance(Status.hover, WidgetAppearance.EMPTY)
            .addAppearance(Status.inactive, WidgetAppearance.EMPTY)
            .addAppearance(Status.pressed, WidgetAppearance.EMPTY)
            .addAppearance(Status.focused, WidgetAppearance.EMPTY)
            .theme(Theme.EMPTY).build();

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static WidgetTheme fromConfig(WidgetData widgetData, Theme theme) {
        Map<Status, WidgetAppearance> appearances = new HashMap<>();
        for (var entry : widgetData.statuses().entrySet()) {
            Status status = entry.getKey();
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
            appearances.put(status, widgetAppearance);
        }
        return new WidgetTheme(widgetData.id(), appearances, theme);
    }

    public static WidgetTheme getDefault() {
        return ThemeRegistry.getDefault().getWidgetTheme("default");
    }

    public WidgetAppearance getAppearance(Status status) {
        return appearances.get(status);
    }

    public void render(ThemeGraphics graphics, Box pBox, AbstractWidget widget) {
        WidgetAppearance widgetAppearance = appearances.get(widget.status());
        if (widgetAppearance == null) {
            widgetAppearance = appearances.get(Status.active);
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
        private final Map<Status, WidgetAppearance> appearances = new HashMap<>();
        private final String id;
        private Theme theme;

        public Builder(String id) {
            this.id = id;
        }

        public Builder addAppearance(Status status, WidgetAppearance appearance) {
            appearances.put(status, appearance);
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
