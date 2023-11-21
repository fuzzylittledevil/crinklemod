package ninja.crinkle.mod.config;

import com.electronwill.nightconfig.core.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.util.ConfigUtil;

import java.util.HashMap;
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
        public int solids;
        public int liquids;

        public ConsumableData(String name, int solids, int liquids) {
            this.name = name;
            this.solids = solids;
            this.liquids = liquids;
        }

        /**
         * Convert the consumable data to a config object
         *
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
                            new ConsumableData("minecraft:potato", 200, 25).toConfig(),
                            new ConsumableData("minecraft:apple", 175, 50).toConfig(),
                            new ConsumableData("minecraft:milk_bucket", 200, 800).toConfig()
                    ),
                    ConfigUtil.getItemNameValidator("name")
            );
    private static final ForgeConfigSpec SPEC = BUILDER.build();

    public static Map<Item, ConsumableData> consumables = new HashMap<>();

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, CONFIG_FILE_NAME);
    }

    /**
     * Load the config
     *
     * @param event The event
     * @see ModConfigEvent
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
