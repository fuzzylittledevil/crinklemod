package ninja.crinkle.mod;

import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import ninja.crinkle.mod.metabolism.MetabolismRegistration;

/**
 * The main class of the mod.
 */
@Mod(CrinkleMod.MODID)
public class CrinkleMod {
    /**
     * The mod ID of the mod.
     * This must match the mod ID in the root gradle.properties file as well as the directory: <pre>src/main/resources/assets/[mod_id]</pre>
     */
    public static final String MODID = "crinklemod";
    /**
     * The event bus that is used to register internal events.
     */
    public static final IEventBus EVENT_BUS = BusBuilder.builder().build();

    public CrinkleMod() {
        MetabolismRegistration.register();
    }
}
