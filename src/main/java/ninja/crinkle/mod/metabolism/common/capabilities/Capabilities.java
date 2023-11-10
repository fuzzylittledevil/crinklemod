package ninja.crinkle.mod.metabolism.common.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class Capabilities {
    public static final Capability<IMetabolism> METABOLISM = CapabilityManager.get(new CapabilityToken<>() {});
}
