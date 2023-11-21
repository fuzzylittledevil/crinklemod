package ninja.crinkle.mod.util;

import com.electronwill.nightconfig.core.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Predicate;

public class ConfigUtil {
    public static Predicate<Object> getItemNameValidator(final String key) {
        return o -> {
            if (o instanceof final Config data)
                return ForgeRegistries.ITEMS.containsKey(new ResourceLocation(data.get(key)));
            if (o instanceof final String itemName)
                return ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
            return false;
        };
    }
}
