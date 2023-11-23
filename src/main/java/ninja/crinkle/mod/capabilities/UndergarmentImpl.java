package ninja.crinkle.mod.capabilities;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.config.UndergarmentConfig;
import ninja.crinkle.mod.undergarment.Undergarment;
import ninja.crinkle.mod.util.MathUtil;
import org.slf4j.Logger;

public class UndergarmentImpl implements IUndergarment {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String NBT_KEY_LIQUIDS = "liquids";
    private static final String NBT_KEY_SOLIDS = "solids";
    private static final String NBT_KEY_MAX_LIQUIDS = "maxLiquids";
    private static final String NBT_KEY_MAX_SOLIDS = "maxSolids";
    private int liquids;
    private int solids;
    private int maxLiquids;
    private int maxSolids;

    public UndergarmentImpl(Item item) {
        if (!UndergarmentConfig.undergarments.containsKey(item)) return;
        maxLiquids = UndergarmentConfig.undergarments.get(item).maxLiquids;
        maxSolids = UndergarmentConfig.undergarments.get(item).maxSolids;
    }

    @Override
    public int getLiquids() {
        return liquids;
    }

    @Override
    public void setLiquids(int value) {
        liquids = MathUtil.clamp(value, 0, maxLiquids);
    }

    @Override
    public int getSolids() {
        return solids;
    }

    @Override
    public void setSolids(int value) {
        solids = MathUtil.clamp(value, 0, maxSolids);
    }

    @Override
    public int getMaxLiquids() {
        return maxLiquids;
    }

    @Override
    public void setMaxLiquids(int value) {
        maxLiquids = MathUtil.clamp(value, 0, Integer.MAX_VALUE);
    }

    @Override
    public int getMaxSolids() {
        return maxSolids;
    }

    @Override
    public void setMaxSolids(int value) {
        maxSolids = MathUtil.clamp(value, 0, Integer.MAX_VALUE);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        // LOGGER.debug("Serializing undergarment data: {}", this);
        nbt.putInt(NBT_KEY_LIQUIDS, liquids);
        nbt.putInt(NBT_KEY_SOLIDS, solids);
        nbt.putInt(NBT_KEY_MAX_LIQUIDS, maxLiquids);
        nbt.putInt(NBT_KEY_MAX_SOLIDS, maxSolids);
        return nbt;
    }

    public void save(ItemStack itemStack) {
        CompoundTag nbt = itemStack.getOrCreateTag();
        nbt.put(Undergarment.NBT_KEY, serializeNBT());
        itemStack.setTag(nbt);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        liquids = nbt.getInt(NBT_KEY_LIQUIDS);
        solids = nbt.getInt(NBT_KEY_SOLIDS);
        if (nbt.contains(NBT_KEY_MAX_LIQUIDS))
            maxLiquids = nbt.getInt(NBT_KEY_MAX_LIQUIDS);
        if (nbt.contains(NBT_KEY_MAX_SOLIDS))
            maxSolids = nbt.getInt(NBT_KEY_MAX_SOLIDS);
    }

    @Override
    public String toString() {
        return String.format("UndergarmentImpl{liquids=%d, solids=%d, maxLiquids=%d, maxSolids=%d}", liquids, solids, maxLiquids, maxSolids);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UndergarmentImpl that = (UndergarmentImpl) o;

        if (liquids != that.liquids) return false;
        if (solids != that.solids) return false;
        if (maxLiquids != that.maxLiquids) return false;
        return maxSolids == that.maxSolids;
    }

    @Override
    public int hashCode() {
        int result = liquids;
        result = 31 * result + solids;
        result = 31 * result + maxLiquids;
        result = 31 * result + maxSolids;
        return result;
    }
}
