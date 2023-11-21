package ninja.crinkle.mod.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class MetabolismCapabilities {
    public static final Capability<IMetabolism> METABOLISM = CapabilityManager.get(new CapabilityToken<>() {
    });

    private MetabolismCapabilities() {
    }
}
