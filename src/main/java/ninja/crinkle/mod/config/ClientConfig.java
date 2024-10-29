package ninja.crinkle.mod.config;

import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.config.sections.client.OverlaySection;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Mod.EventBusSubscriber(modid = CrinkleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientConfig {
    private static final ForgeConfigSpec CONFIG;
    public static final ClientConfig INSTANCE;

    static {
        Pair<ClientConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        INSTANCE = pair.getLeft();
        CONFIG = pair.getRight();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CONFIG);
    }

    public final OverlaySection overlay;

    ClientConfig(ForgeConfigSpec.Builder builder) {
        overlay = new OverlaySection(builder);
    }

    public static Object get(List<String> path) {
        return CONFIG.get(path);
    }

    public static String getTranslation(List<String> path) {
        return CONFIG.getLevelTranslationKey(path);
    }
}
