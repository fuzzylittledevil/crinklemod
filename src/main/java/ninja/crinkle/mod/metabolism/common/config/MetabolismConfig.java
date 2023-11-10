package ninja.crinkle.mod.metabolism.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import ninja.crinkle.mod.CrinkleMod;

@Mod.EventBusSubscriber(modid = CrinkleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MetabolismConfig {
    private static final String CONFIG_FILE_NAME = String.format("%s-metabolism.toml", CrinkleMod.MODID);

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder()
            .comment("These values are defaults for new players. Changing these values will not affect existing players.",
                    "To change the values of existing players, use the /metabolism command.");

    private static final ForgeConfigSpec.DoubleValue BLADDER_CONTINENCE = BUILDER
            .comment("The bladder continence of the player. Higher values mean more continence.")
            .defineInRange("bladderContinence", 0.8d, 0, 1d);
    private static final ForgeConfigSpec.DoubleValue BOWEL_CONTINENCE = BUILDER
            .comment("The bowel continence of the player. Higher values mean more continence.")
            .defineInRange("bowelContinence", 0.8d, 0, 1d);

    private static final ForgeConfigSpec.DoubleValue BLADDER_CAPACITY = BUILDER
            .comment("The maximum amount of liquids the bladder can hold.")
            .defineInRange("bladderCapacity", 100d, 1d, Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue BOWEL_CAPACITY = BUILDER
            .comment("The maximum amount of solids the bowels can hold.")
            .defineInRange("bowelCapacity", 100d, 1d, Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue SOLIDS_RATE = BUILDER
            .comment("The rate at which solids are digested.")
            .defineInRange("solidsRate", 0.1d, 0d, Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue LIQUIDS_RATE = BUILDER
            .comment("The rate at which liquids are digested.")
            .defineInRange("liquidsRate", 0.1d, 0d, Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue MAX_LIQUIDS = BUILDER
            .comment("The maximum amount of liquids the stomach can hold.")
            .defineInRange("maxLiquids", 100d, 1d, Double.MAX_VALUE);
    private static final ForgeConfigSpec.DoubleValue MAX_SOLIDS = BUILDER
            .comment("The maximum amount of solids the stomach can hold.")
            .defineInRange("maxSolids", 100d, 1d, Double.MAX_VALUE);

    private static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double bladderContinence;
    public static double bowelContinence;
    public static double bladderCapacity;
    public static double bowelCapacity;
    public static double solidsRate;
    public static double liquidsRate;
    public static double maxLiquids;
    public static double maxSolids;


    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, CONFIG_FILE_NAME);
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (!event.getConfig().getFileName().equals(CONFIG_FILE_NAME)) return;
        bladderContinence = BLADDER_CONTINENCE.get();
        bowelContinence = BOWEL_CONTINENCE.get();
        bladderCapacity = BLADDER_CAPACITY.get();
        bowelCapacity = BOWEL_CAPACITY.get();
        solidsRate = SOLIDS_RATE.get();
        liquidsRate = LIQUIDS_RATE.get();
        maxLiquids = MAX_LIQUIDS.get();
        maxSolids = MAX_SOLIDS.get();
    }
}
