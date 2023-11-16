package ninja.crinkle.mod.undergarment.common.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class UndergarmentCapabilities {
    public static final Capability<IUndergarment> UNDERGARMENT = CapabilityManager.get(new CapabilityToken<>() {
    });

    private UndergarmentCapabilities() {}
}
