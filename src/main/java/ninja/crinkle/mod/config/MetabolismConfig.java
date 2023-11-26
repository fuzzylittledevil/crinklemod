package ninja.crinkle.mod.config;

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

    private static final ForgeConfigSpec.DoubleValue BLADDER_ACCIDENT_AMOUNT_PERCENT = BUILDER
            .comment("The percentage of bladder fullness that is lost per accident. Higher values mean more loss.")
            .defineInRange("bladderAccidentAmountModifier", 0.5d, 0d, 1d);

    private static final ForgeConfigSpec.DoubleValue BOWEL_ACCIDENT_AMOUNT_PERCENT = BUILDER
            .comment("The percentage of bowel fullness that is lost per accident. Higher values mean more loss.")
            .defineInRange("bowelAccidentAmountModifier", 0.5d, 0d, 1d);
    private static final ForgeConfigSpec.IntValue BLADDER_ACCIDENT_FREQUENCY = BUILDER
            .comment("The frequency of bladder accidents in Minecraft hour, which is 1000 ticks or 50 seconds.")
            .defineInRange("bladderAccidentFrequency", 4, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue BOWEL_ACCIDENT_FREQUENCY = BUILDER
            .comment("The frequency of bowel accidents in Minecraft hour, which is 1000 ticks or 50 seconds.")
            .defineInRange("bowelAccidentFrequency", 9, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.DoubleValue BLADDER_ACCIDENT_WARNING = BUILDER
            .comment("The bladder continence of the player. Higher values mean more continence.")
            .defineInRange("bladderContinence", 0.9d, 0, 1d);
    private static final ForgeConfigSpec.DoubleValue BOWEL_ACCIDENT_WARNING = BUILDER
            .comment("The bowel continence of the player. Higher values mean more continence.")
            .defineInRange("bowelContinence", 0.9d, 0, 1d);
    private static final ForgeConfigSpec.IntValue BLADDER_CAPACITY = BUILDER
            .comment("The maximum amount of liquids the bladder can hold in mB.")
            .defineInRange("bladderCapacity", 500, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue BOWEL_CAPACITY = BUILDER
            .comment("The maximum amount of solids the bowels can hold in mB.")
            .defineInRange("bowelCapacity", 400, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue SOLIDS_RATE = BUILDER
            .comment("The rate at which solids are digested in mB.")
            .defineInRange("solidsRate", 3, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue LIQUIDS_RATE = BUILDER
            .comment("The rate at which liquids are digested in mB.")
            .defineInRange("liquidsRate", 3, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue MAX_LIQUIDS = BUILDER
            .comment("The maximum amount of liquids the stomach can hold in mB.")
            .defineInRange("maxLiquids", 475, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue MAX_SOLIDS = BUILDER
            .comment("The maximum amount of solids the stomach can hold in mB.")
            .defineInRange("maxSolids", 475, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double bladderAccidentAmountPercent;
    public static double bowelAccidentAmountPercent;
    public static int bladderAccidentFrequency;
    public static int bowelAccidentFrequency;
    public static double bladderAccidentWarning;
    public static double bowelAccidentWarning;
    public static int bladderCapacity;
    public static int bowelCapacity;
    public static int solidsRate;
    public static int liquidsRate;
    public static int maxLiquids;
    public static int maxSolids;


    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, CONFIG_FILE_NAME);
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (!event.getConfig().getFileName().equals(CONFIG_FILE_NAME)) return;
        bladderAccidentAmountPercent = BLADDER_ACCIDENT_AMOUNT_PERCENT.get();
        bowelAccidentAmountPercent = BOWEL_ACCIDENT_AMOUNT_PERCENT.get();
        bladderAccidentFrequency = BLADDER_ACCIDENT_FREQUENCY.get();
        bowelAccidentFrequency = BOWEL_ACCIDENT_FREQUENCY.get();
        bladderAccidentWarning = BLADDER_ACCIDENT_WARNING.get();
        bowelAccidentWarning = BOWEL_ACCIDENT_WARNING.get();
        bladderCapacity = BLADDER_CAPACITY.get();
        bowelCapacity = BOWEL_CAPACITY.get();
        solidsRate = SOLIDS_RATE.get();
        liquidsRate = LIQUIDS_RATE.get();
        maxLiquids = MAX_LIQUIDS.get();
        maxSolids = MAX_SOLIDS.get();
    }
}
