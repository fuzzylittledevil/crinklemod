package ninja.crinkle.mod.undergarment.common.config;

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
import ninja.crinkle.mod.lib.common.util.ConfigUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = CrinkleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class UndergarmentConfig {
    private static final String CONFIG_FILE_NAME = String.format("%s-undergarments.toml", CrinkleMod.MODID);

    public static class UndergarmentData {
        public String name;
        public int maxSolids;
        public int maxLiquids;

        public UndergarmentData(String name, int maxSolids, int maxLiquids) {
            this.name = name;
            this.maxSolids = maxSolids;
            this.maxLiquids = maxLiquids;
        }

        public Config toConfig() {
            Config config = Config.inMemory();
            config.set("name", name);
            config.set("maxSolids", maxSolids);
            config.set("maxLiquids", maxLiquids);
            return config;
        }
    }

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.ConfigValue<List<? extends Config>> UNDERGARMENTS = BUILDER
            .comment("A list of undergarments and their associated data.")
            .defineListAllowEmpty("undergarment",
                    List.of(
                            new UndergarmentData("minecraft:leather_leggings", 100, 100).toConfig()
                    ),
                    ConfigUtil.getItemNameValidator("name")
            );
    private static final ForgeConfigSpec SPEC = BUILDER.build();

    public static Map<Item, UndergarmentData> undergarments = new HashMap<>();

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, CONFIG_FILE_NAME);
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (!event.getConfig().getFileName().equals(CONFIG_FILE_NAME)) return;
        undergarments = UNDERGARMENTS.get().stream()
                .collect(Collectors.toMap(
                        data -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(data.get("name"))),
                        data -> new UndergarmentData(data.get("name"), data.get("maxSolids"), data.get("maxLiquids"))
                ));
    }
}
