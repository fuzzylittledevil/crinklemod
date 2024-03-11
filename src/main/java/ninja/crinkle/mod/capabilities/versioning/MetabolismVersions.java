package ninja.crinkle.mod.capabilities.versioning;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.function.BiFunction;

public enum MetabolismVersions {
    V1((provider, tag) -> tag),
    V2((provider, tag) -> new CompoundTag());

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String TAG_VERSION = "version";
    private final BiFunction<ICapabilityProvider, CompoundTag, CompoundTag> upgrade;

    MetabolismVersions(BiFunction<ICapabilityProvider, CompoundTag, CompoundTag> upgrade) {
        this.upgrade = upgrade;
    }


    public static MetabolismVersions fromNBT(@NotNull CompoundTag tag) {
        return tag.contains(TAG_VERSION) ? MetabolismVersions.valueOf(tag.getString(TAG_VERSION)) : V1;
    }

    @Contract("_ -> param1")
    public @NotNull CompoundTag updateVersion(@NotNull CompoundTag tag) {
        tag.putString(TAG_VERSION, name());
        return tag;
    }

    public CompoundTag upgrade(ICapabilityProvider provider, CompoundTag tag) {
        if (this == fromNBT(tag))
            return tag;
        LOGGER.info("Upgrading metabolism capability from {} to {}", fromNBT(tag), this);
        return updateVersion(upgrade.apply(provider, tag));
    }

    public static CompoundTag performUpgrades(ICapabilityProvider provider, CompoundTag tag) {
        MetabolismVersions version = fromNBT(tag);
        for(MetabolismVersions v : values()) {
            if (v.ordinal() > version.ordinal())
                tag = v.upgrade(provider, tag);
        }
        return tag;
    }

}
