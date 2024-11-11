package ninja.crinkle.mod.config.sections.client;

import net.minecraftforge.common.ForgeConfigSpec;
import ninja.crinkle.mod.config.ScreenRegion;

public class MetabolismOverlay {
    public final ForgeConfigSpec.ConfigValue<Boolean> visible;
    public final ForgeConfigSpec.ConfigValue<Boolean> animated;
    public final ForgeConfigSpec.ConfigValue<ScreenRegion> region;
    public final ForgeConfigSpec.ConfigValue<Integer> x;
    public final ForgeConfigSpec.ConfigValue<Integer> y;

    public MetabolismOverlay(ForgeConfigSpec.Builder builder) {
        builder.comment("Settings for the metabolism overlay.")
                .push("metabolism");
        visible = builder
                .comment("Whether the overlay is visible.")
                .translation("config.crinklemod.overlay.visible")
                .define("visible", true);
        animated = builder
                .comment("Whether the overlay is animated.")
                .define("animated", true);
        region = builder
                .comment("The region of the screen where the overlay is displayed.")
                .defineEnum("region", ScreenRegion.TOP_LEFT);
        x = builder
                .comment("The x-coordinate offset of the overlay, position to the center of the region.")
                .defineInRange("x", 0, 0, Integer.MAX_VALUE);
        y = builder
                .comment("The y-coordinate offset of the overlay, position to the center of the region.")
                .defineInRange("y", 0, 0, Integer.MAX_VALUE);
        builder.pop();
    }
}
