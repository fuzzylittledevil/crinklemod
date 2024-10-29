package ninja.crinkle.mod.client.ui.overlays;

import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import ninja.crinkle.mod.client.ui.animations.*;
import ninja.crinkle.mod.metabolism.Metabolism;

public class SpriteOverlay {
    public static final IGuiOverlay HUD = (forgeGui, guiGraphics, v, w, h) -> {

        LocalPlayer player = forgeGui.getMinecraft().player;
        if (player == null) {
            return;
        }
        if (AnimationController.INSTANCE.getCurrentAnimation() == null) {
            LocalPlayer localPlayer = forgeGui.getMinecraft().player;
            if (localPlayer == null) {
                return;
            }
            Metabolism metabolism = Metabolism.of(localPlayer);
            Animation ani = Animation.builder()
                    .addSpriteGroups(CharacterSpriteGroup.of(metabolism), BubbleSpriteGroup.of(metabolism))
                    .speed(getSpeed(localPlayer))
                    .position(metabolism.getIndicatorPositionX(), metabolism.getIndicatorPositionY())
                    .build();
            AnimationController.INSTANCE.queueAnimation(ani);
        }
    };

    private static double getSpeed(LocalPlayer player) {
        Metabolism metabolism = Metabolism.of(player);
        Metabolism.DesperationLevel desperationLevel = metabolism.getNumberOneDesperationLevel().compareTo(metabolism.getNumberTwoDesperationLevel()) > 0 ?
                metabolism.getNumberOneDesperationLevel() : metabolism.getNumberTwoDesperationLevel();
        if (desperationLevel.getLevel() <= 0) {
            return 1.0;
        }
        return desperationLevel.getLevel() * 2.0;
    }
}
