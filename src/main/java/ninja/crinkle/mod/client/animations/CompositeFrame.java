package ninja.crinkle.mod.client.animations;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class CompositeFrame {
    private final List<Sprite> sprites = new ArrayList<>();

    public CompositeFrame(List<Sprite> sprites) {
        this.sprites.addAll(sprites);
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Sprite> getSprites() {
        return sprites;
    }

    public void render(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        for (Sprite sprite : sprites) {
            sprite.render(guiGraphics, xOffset, yOffset);
        }
    }

    public static class Builder {
        private final List<Sprite> sprites = new ArrayList<>();

        public Builder addSprite(Sprite sprite) {
            sprites.add(sprite);
            return this;
        }

        public CompositeFrame build() {
            return new CompositeFrame(sprites);
        }

        public Builder addSprites(List<Sprite> sprites) {
            for (Sprite sprite : sprites) {
                addSprite(sprite);
            }
            return this;
        }
    }
}
