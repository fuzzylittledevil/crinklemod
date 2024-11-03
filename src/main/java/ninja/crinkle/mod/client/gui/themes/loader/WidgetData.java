package ninja.crinkle.mod.client.gui.themes.loader;

import ninja.crinkle.mod.client.gui.widgets.WidgetState;

import java.util.Map;

public record WidgetData(String id, Map<WidgetState, Appearance> states) {

    public record Appearance(Appearance.Data background, Appearance.Data foreground) {
        public record Data(String texture, String color, boolean shadow) {
        }
    }
}
