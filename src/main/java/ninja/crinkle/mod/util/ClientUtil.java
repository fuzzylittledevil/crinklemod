package ninja.crinkle.mod.util;

import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import ninja.crinkle.mod.client.ClientHooks;

import java.util.Optional;

public class ClientUtil {
    public static LocalPlayer getPlayer() {
        return Optional.ofNullable(DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHooks::getMinecraft))
                .map(minecraft -> minecraft.player).orElse(null);
    }
}
