package ninja.crinkle.mod.client.animations;

import java.util.ArrayList;
import java.util.List;

public abstract class SpriteGroup {
    public static final int SPRITE_SIZE = 32;

    private final List<Sprite> sprites = new ArrayList<>();
    SpriteGroup(int frameCount, int offsetX, int offsetY, int size, String groupName, String spriteName) {
        for (int i = 1; i <= frameCount; i++) {
            sprites.add(Sprite.builder()
                    .resource("sprites/" + groupName + "/" + spriteName + i)
                    .position(offsetX, offsetY)
                    .size(size)
                    .build());
        }
    }

    SpriteGroup(int offsetX, int offsetY, int size, String groupName, String... spriteNames) {
        for (String spriteName : spriteNames) {
            sprites.add(Sprite.builder()
                    .resource("sprites/" + groupName + "/" + spriteName)
                    .position(offsetX, offsetY)
                    .size(size)
                    .build());
        }
    }

    public List<Sprite> getSprites() {
        return sprites;
    }
}
