package ninja.crinkle.mod.client.gui.textures;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import ninja.crinkle.mod.CrinkleMod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Atlas extends TextureAtlasHolder {
    public Atlas(TextureManager pTextureManager) {
        super(pTextureManager,
                new ResourceLocation(CrinkleMod.MODID, "textures/atlas/theme.png"),
                new ResourceLocation(CrinkleMod.MODID, "theme"));
    }

    public List<ResourceLocation> getSprites() {
        return this.textureAtlas.getTextureLocations().stream().toList();
    }

    @Override
    public @NotNull TextureAtlasSprite getSprite(@NotNull ResourceLocation pLocation) {
        return super.getSprite(pLocation);
    }
}
