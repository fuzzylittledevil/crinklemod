package ninja.crinkle.mod.config.sections.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class OverlaySection {

    public final MetabolismOverlay metabolism;

    public OverlaySection(ForgeConfigSpec.Builder builder) {
        builder.push("overlay");
        metabolism = new MetabolismOverlay(builder);
    }

}
