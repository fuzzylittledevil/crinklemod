package ninja.crinkle.mod.client.ui.animations;

import ninja.crinkle.mod.metabolism.Metabolism;

public class CharacterSpriteGroup extends SpriteGroup {
    public static final CharacterSpriteGroup NORMAL = new CharacterSpriteGroup(2, "normal");
    public static final CharacterSpriteGroup DESPERATE = new CharacterSpriteGroup(2, "desperate");
    public static final CharacterSpriteGroup VERY_DESPERATE =
            new CharacterSpriteGroup(2, "very_desperate");
    public static final CharacterSpriteGroup EXTREMELY_DESPERATE =
            new CharacterSpriteGroup("desperate1", "desperate2", "very_desperate1", "very_desperate2");
    public static final CharacterSpriteGroup ACCIDENT = new CharacterSpriteGroup("accident1", "accident2",
            "accident1", "accident2", "accident1", "accident2");
    public static final CharacterSpriteGroup RELIEF = new CharacterSpriteGroup(2, "relief");

    private static final int OFFSET_X = 0;
    private static final int OFFSET_Y = 17;
    private static final String GROUP_NAME = "character";

    CharacterSpriteGroup(int frameCount, String spriteName) {
        super(frameCount, OFFSET_X, OFFSET_Y, SPRITE_SIZE, GROUP_NAME, spriteName);
    }

    CharacterSpriteGroup(String... spriteNames) {
        super(OFFSET_X, OFFSET_Y, SPRITE_SIZE, GROUP_NAME, spriteNames);
    }

    public static CharacterSpriteGroup of(Metabolism metabolism) {
        if (!metabolism.isNumberOneDesperate() && !metabolism.isNumberTwoDesperate()) {
            return NORMAL;
        }

        Metabolism.DesperationLevel numberOne = metabolism.getNumberOneDesperationLevel();
        Metabolism.DesperationLevel numberTwo = metabolism.getNumberTwoDesperationLevel();
        Metabolism.DesperationLevel desperation = numberOne.compareTo(numberTwo) > 0 ? numberOne : numberTwo;

        return switch (desperation) {
            case MEDIUM_LOW, MEDIUM -> DESPERATE;
            case MEDIUM_HIGH, HIGH -> VERY_DESPERATE;
            default -> NORMAL;
        };
    }
}
