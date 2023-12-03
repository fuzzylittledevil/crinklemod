package ninja.crinkle.mod.client.textures;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CrinkleSpriteLoader extends TextureAtlasHolder {
    public CrinkleSpriteLoader(TextureManager pTextureManager, SpriteLoaderType pType) {
        super(pTextureManager, pType.getAtlasLocation(), pType.getAtlasInfoLocation());
    }

    @Override
    public @NotNull TextureAtlasSprite getSprite(@NotNull ResourceLocation pLocation) {
        return super.getSprite(pLocation);
    }
}
