package ninja.crinkle.mod.metabolism.config;

import com.electronwill.nightconfig.core.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import ninja.crinkle.mod.CrinkleMod;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Consumable configuration
 * This class is used to load the consumable configuration from the config file.
 * This class also contains a list of consumables from Vanilla and their associated default data.
 */
@Mod.EventBusSubscriber(modid = CrinkleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConsumableConfig {
    /**
     * Consumable data class
     * This class is used to store the consumable data from the config file.
     * This class also contains a method to convert the consumable data to a config object.
     */
    public static class ConsumableData {
        public String name;
        public double solids;
        public double liquids;

        public ConsumableData(String name, double solids, double liquids) {
            this.name = name;
            this.solids = solids;
            this.liquids = liquids;
        }

        /**
         * Convert the consumable data to a config object
         * @return The config object
         */
        public Config toConfig() {
            Config config = Config.inMemory();
            config.set("name", name);
            config.set("solids", solids);
            config.set("liquids", liquids);
            return config;
        }
    }

    private static final String CONFIG_FILE_NAME = String.format("%s-consumables.toml", CrinkleMod.MODID);
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.ConfigValue<List<? extends Config>> CONSUMABLE_ITEMS = BUILDER
            .comment("A list of consumables and their associated data.")
            .defineListAllowEmpty("consumable",
                    List.of(
                            new ConsumableData("minecraft:potato", 2f, .25f).toConfig(),
                            new ConsumableData("minecraft:apple", 1f, 1f).toConfig(),
                            new ConsumableData("minecraft:milk_bucket", 1f, 1f).toConfig()
                    ),
                    ConsumableConfig::validateItemName
            );
    private static final ForgeConfigSpec SPEC = BUILDER.build();

    public static Map<Item, ConsumableData> consumables;

    /**
     * Ensure the item name is loaded in the registry and valid.
     * @param obj The object to validate
     * @return Whether the object is valid
     */
    private static boolean validateItemName(final Object obj) {
        return obj instanceof final Config data && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(data.get("name")));
    }

    /**
     * Register the config
     * @see ninja.crinkle.mod.metabolism.MetabolismRegistration
     */
    public static void register() {
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, SPEC, CONFIG_FILE_NAME);
    }

    /**
     * Load the config
     * @see ModConfigEvent
     * @param event The event
     */
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (!event.getConfig().getFileName().equals(CONFIG_FILE_NAME)) return;
        consumables = CONSUMABLE_ITEMS.get().stream()
                .collect(Collectors.toMap(
                        data -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(data.get("name"))),
                        data -> new ConsumableData(data.get("name"), data.get("solids"), data.get("liquids"))
                ));
    }
}
