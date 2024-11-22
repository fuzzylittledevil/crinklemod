package ninja.crinkle.mod.client.gui.themes.loader;

import ninja.crinkle.mod.client.gui.textures.ColorFilters;
import ninja.crinkle.mod.client.gui.themes.Style;

import java.util.List;
import java.util.Map;

public record StyleData(String id, Map<Style.Variant, Variant> variants) {

    public record Variant(Variant.Data background, Variant.Data foreground) {
        public record Data(String texture, String color, boolean shadow, List<ColorFilters> colorFilters) {
        }
    }
}
