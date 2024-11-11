package ninja.crinkle.mod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import ninja.crinkle.mod.client.ClientHooks;
import ninja.crinkle.mod.client.gui.properties.Box;
import ninja.crinkle.mod.client.gui.properties.ImmutablePoint;
import ninja.crinkle.mod.client.gui.properties.MutablePoint;
import ninja.crinkle.mod.client.gui.properties.Size;

import java.util.Optional;

public class ClientUtil {
    public static Player getPlayer() {
        return Optional.ofNullable(DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHooks::getMinecraft))
                .map(minecraft -> minecraft.player).orElse(null);
    }

    public static Minecraft getMinecraft() {
        return DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHooks::getMinecraft);
    }

    public static Size screenSize() {
        int width = getMinecraft().getWindow().getGuiScaledWidth();
        int height = getMinecraft().getWindow().getGuiScaledHeight();
        return new Size(width, height);
    }

    public static Box screenBox() {
        return new Box(ImmutablePoint.ZERO, screenSize());
    }
}
