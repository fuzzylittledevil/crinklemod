package ninja.crinkle.mod.metabolism.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import ninja.crinkle.mod.CrinkleMod;

@Mod.EventBusSubscriber(modid = CrinkleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MetabolismConfig {
    private static final String CONFIG_FILE_NAME = String.format("%s-metabolism.toml", CrinkleMod.MODID);

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.DoubleValue CONTINENCE = BUILDER
            .comment("The continence of the player. Higher values mean more continence.")
            .defineInRange("continence", 0.99d, 0, 1d);
    private static final ForgeConfigSpec.DoubleValue BLADDER_CAPACITY = BUILDER
            .comment("The maximum amount of liquids the bladder can hold.")
            .defineInRange("bladderCapacity", 100d, 1d, Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue BOWELS_CAPACITY = BUILDER
            .comment("The maximum amount of solids the bowels can hold.")
            .defineInRange("bowelsCapacity", 100d, 1d, Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue SOLIDS_RATE = BUILDER
            .comment("The rate at which solids are digested.")
            .defineInRange("solidsRate", 0.1d, 0d, Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue LIQUIDS_RATE = BUILDER
            .comment("The rate at which liquids are digested.")
            .defineInRange("liquidsRate", 0.1d, 0d, Double.MAX_VALUE);

    private static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double continence;
    public static double bladderCapacity;
    public static double bowelsCapacity;
    public static double solidsRate;
    public static double liquidsRate;

    public static void register() {
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, SPEC, CONFIG_FILE_NAME);
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (!event.getConfig().getFileName().equals(CONFIG_FILE_NAME)) return;
        continence = CONTINENCE.get();
        bladderCapacity = BLADDER_CAPACITY.get();
        bowelsCapacity = BOWELS_CAPACITY.get();
        solidsRate = SOLIDS_RATE.get();
        liquidsRate = LIQUIDS_RATE.get();
    }
}
