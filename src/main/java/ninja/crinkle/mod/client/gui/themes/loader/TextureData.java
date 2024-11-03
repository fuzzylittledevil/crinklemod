package ninja.crinkle.mod.client.gui.themes.loader;

import ninja.crinkle.mod.client.gui.textures.Texture;

import java.util.Map;

public record TextureData(String id, String location, Map<Texture.Slice.Location, Texture.Slice> slices) {
}
