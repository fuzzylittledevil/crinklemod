package ninja.crinkle.mod.undergarment.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import ninja.crinkle.mod.lib.common.util.MathUtil;
import ninja.crinkle.mod.undergarment.common.config.UndergarmentConfig;

public class UndergarmentImpl implements IUndergarment {
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
        nbt.putInt(NBT_KEY_LIQUIDS, liquids);
        nbt.putInt(NBT_KEY_SOLIDS, solids);
        nbt.putInt(NBT_KEY_MAX_LIQUIDS, maxLiquids);
        nbt.putInt(NBT_KEY_MAX_SOLIDS, maxSolids);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        liquids = nbt.getInt(NBT_KEY_LIQUIDS);
        solids = nbt.getInt(NBT_KEY_SOLIDS);
        maxLiquids = nbt.getInt(NBT_KEY_MAX_LIQUIDS);
        maxSolids = nbt.getInt(NBT_KEY_MAX_SOLIDS);
    }
}
