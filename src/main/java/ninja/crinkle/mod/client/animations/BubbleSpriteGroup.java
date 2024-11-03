package ninja.crinkle.mod.client.animations;

import ninja.crinkle.mod.metabolism.Metabolism;

public class BubbleSpriteGroup extends SpriteGroup {
    public static final BubbleSpriteGroup NORMAL = new BubbleSpriteGroup(2, "normal");
    public static final BubbleSpriteGroup WET = new BubbleSpriteGroup(2, "wet");
    public static final BubbleSpriteGroup MESSY = new BubbleSpriteGroup(2, "messy");
    public static final BubbleSpriteGroup BOTH = new BubbleSpriteGroup("wet1", "wet2", "messy1", "messy2");

    private static final int OFFSET_X = 1;
    private static final int OFFSET_Y = -3;
    private static final String GROUP_NAME = "bubble";

    BubbleSpriteGroup(int frameCount, String spriteName) {
        super(frameCount, OFFSET_X, OFFSET_Y, SPRITE_SIZE, GROUP_NAME, spriteName);
    }

    BubbleSpriteGroup(String... spriteNames) {
        super(OFFSET_X, OFFSET_Y, SPRITE_SIZE, GROUP_NAME, spriteNames);
    }

    public static BubbleSpriteGroup of(Metabolism metabolism) {
        if (metabolism.isNumberOneDesperate() && metabolism.isNumberTwoDesperate()) {
            return BOTH;
        } else if (metabolism.isNumberOneDesperate()) {
            return WET;
        } else if (metabolism.isNumberTwoDesperate()) {
            return MESSY;
        } else {
            return NORMAL;
        }
    }
}
