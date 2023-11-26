package ninja.crinkle.mod.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import ninja.crinkle.mod.config.MetabolismConfig;
import ninja.crinkle.mod.metabolism.MetabolismSettings;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

import static ninja.crinkle.mod.util.MathUtil.clamp;
import static ninja.crinkle.mod.util.MathUtil.round;

/**
 * Metabolism capability implementation.
 * This class is used to store the metabolism of a player in a compound NBT tag.
 *
 * @author Galen
 * @see IMetabolism
 */
public class MetabolismImpl implements IMetabolism {
    private int liquids;
    private int solids;
    private int bladder;
    private int bowels;
    private int bladderCapacity;
    private int bowelCapacity;
    private int solidsRate;
    private int liquidsRate;
    private int maxSolids;
    private int maxLiquids;
    private double bladderAccidentWarning;
    private double bowelAccidentWarning;
    private int bladderAccidentFrequency;
    private int bowelAccidentFrequency;
    private double bladderAccidentAmountPercent;
    private double bowelAccidentAmountPercent;

    public MetabolismImpl() {
        bladderCapacity = MetabolismConfig.bladderCapacity;
        bowelCapacity = MetabolismConfig.bowelCapacity;
        solidsRate = MetabolismConfig.solidsRate;
        liquidsRate = MetabolismConfig.liquidsRate;
        maxSolids = MetabolismConfig.maxSolids;
        maxLiquids = MetabolismConfig.maxLiquids;
        bladderAccidentWarning = MetabolismConfig.bladderAccidentWarning;
        bowelAccidentWarning = MetabolismConfig.bowelAccidentWarning;
        bladderAccidentFrequency = MetabolismConfig.bladderAccidentFrequency;
        bowelAccidentFrequency = MetabolismConfig.bowelAccidentFrequency;
        bladderAccidentAmountPercent = MetabolismConfig.bladderAccidentAmountPercent;
        bowelAccidentAmountPercent = MetabolismConfig.bowelAccidentAmountPercent;
    }

    /**
     * Serialize the metabolism to NBT.
     *
     * @return The serialized metabolism
     */
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(MetabolismSettings.LIQUIDS.key(), getLiquids());
        tag.putInt(MetabolismSettings.SOLIDS.key(), getSolids());
        tag.putInt(MetabolismSettings.BLADDER.key(), getBladder());
        tag.putInt(MetabolismSettings.BOWELS.key(), getBowels());
        tag.putInt(MetabolismSettings.BLADDER_CAPACITY.key(), getBladderCapacity());
        tag.putInt(MetabolismSettings.BOWEL_CAPACITY.key(), getBowelCapacity());
        tag.putInt(MetabolismSettings.SOLIDS_RATE.key(), getSolidsRate());
        tag.putInt(MetabolismSettings.LIQUIDS_RATE.key(), getLiquidsRate());
        tag.putInt(MetabolismSettings.MAX_LIQUIDS.key(), getMaxLiquids());
        tag.putInt(MetabolismSettings.MAX_SOLIDS.key(), getMaxSolids());
        tag.putDouble(MetabolismSettings.BLADDER_ACCIDENT_WARNING.key(), getBladderAccidentWarning());
        tag.putDouble(MetabolismSettings.BOWEL_ACCIDENT_WARNING.key(), getBowelAccidentWarning());
        tag.putInt(MetabolismSettings.BLADDER_ACCIDENT_FREQUENCY.key(), getBladderAccidentFrequency());
        tag.putInt(MetabolismSettings.BOWEL_ACCIDENT_FREQUENCY.key(), getBowelAccidentFrequency());
        tag.putDouble(MetabolismSettings.BLADDER_ACCIDENT_AMOUNT_PERCENT.key(), getBladderAccidentAmountPercent());
        tag.putDouble(MetabolismSettings.BOWEL_ACCIDENT_AMOUNT_PERCENT.key(), getBowelAccidentAmountPercent());
        return tag;
    }

    /**
     * Deserialize the metabolism from NBT.
     *
     * @param nbt The NBT to deserialize
     */
    @Override
    public void deserializeNBT(@NotNull CompoundTag nbt) {
        safeSet(nbt, MetabolismSettings.LIQUIDS.key(), (tag, key) -> setLiquids(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.SOLIDS.key(), (tag, key) -> setSolids(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.BLADDER.key(), (tag, key) -> setBladder(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.BOWELS.key(), (tag, key) -> setBowels(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.BLADDER_CAPACITY.key(), (tag, key) -> setBladderCapacity(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.BOWEL_CAPACITY.key(), (tag, key) -> setBowelCapacity(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.SOLIDS_RATE.key(), (tag, key) -> setSolidsRate(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.LIQUIDS_RATE.key(), (tag, key) -> setLiquidsRate(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.MAX_LIQUIDS.key(), (tag, key) -> setMaxLiquids(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.MAX_SOLIDS.key(), (tag, key) -> setMaxSolids(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.BLADDER_ACCIDENT_WARNING.key(), (tag, key) -> setBladderAccidentWarning(tag.getDouble(key)));
        safeSet(nbt, MetabolismSettings.BOWEL_ACCIDENT_WARNING.key(), (tag, key) -> setBowelAccidentWarning(tag.getDouble(key)));
        safeSet(nbt, MetabolismSettings.BLADDER_ACCIDENT_FREQUENCY.key(), (tag, key) -> setBladderAccidentFrequency(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.BOWEL_ACCIDENT_FREQUENCY.key(), (tag, key) -> setBowelAccidentFrequency(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.BLADDER_ACCIDENT_AMOUNT_PERCENT.key(), (tag, key) -> setBladderAccidentAmountPercent(tag.getDouble(key)));
        safeSet(nbt, MetabolismSettings.BOWEL_ACCIDENT_AMOUNT_PERCENT.key(), (tag, key) -> setBowelAccidentAmountPercent(tag.getDouble(key)));
    }

    private void safeSet(CompoundTag nbt, String key, BiConsumer<CompoundTag, String> setter) {
        if (nbt.contains(key)) {
            setter.accept(nbt, key);
        }
    }

    /**
     * Get the amount of liquids in the player's stomach.
     *
     * @return The amount of liquids in the player's stomach.
     */
    @Override
    public int getLiquids() {
        return liquids;
    }

    /**
     * Set the amount of liquids in the player's stomach.
     * Sets the sync flag to true.
     *
     * @param liquids The amount of liquids in the player's stomach
     */
    @Override
    public void setLiquids(int liquids) {
        this.liquids = clamp(liquids, 0, maxLiquids);
    }

    /**
     * Get the amount of solids in the player's stomach.
     *
     * @return The amount of solids in the player's stomach.
     */
    @Override
    public int getSolids() {
        return solids;
    }

    /**
     * Set the amount of solids in the player's stomach.
     * Sets the sync flag to true.
     *
     * @param solids The amount of solids in the player's stomach
     */
    @Override
    public void setSolids(int solids) {
        this.solids = clamp(solids, 0, maxSolids);
    }

    /**
     * Get the amount of fluid in the player's bladder.
     *
     * @return The amount of fluid in the player's bladder.
     */
    @Override
    public int getBladder() {
        return bladder;
    }

    /**
     * Set the amount of fluid in the player's bladder.
     * Sets the sync flag to true.
     *
     * @param bladder The amount of fluid in the player's bladder
     */
    @Override
    public void setBladder(int bladder) {
        this.bladder = clamp(bladder, 0, bladderCapacity);
    }

    /**
     * Get the amount of solids in the player's bowels.
     *
     * @return The amount of solids in the player's bowels.
     */
    @Override
    public int getBowels() {
        return bowels;
    }

    /**
     * Set the amount of solids in the player's bowels.
     * Sets the sync flag to true.
     *
     * @param bowels The amount of solids in the player's bowels
     */
    @Override
    public void setBowels(int bowels) {
        this.bowels = clamp(bowels, 0, bowelCapacity);
    }


    @Override
    public int getBladderCapacity() {
        return bladderCapacity;
    }

    @Override
    public void setBladderCapacity(int bladderCapacity) {
        this.bladderCapacity = clamp(bladderCapacity, 0, maxLiquids);
    }

    @Override
    public int getBowelCapacity() {
        return bowelCapacity;
    }

    @Override
    public void setBowelCapacity(int bowelsCapacity) {
        this.bowelCapacity = clamp(bowelsCapacity, 0, maxSolids);
    }

    @Override
    public int getSolidsRate() {
        return solidsRate;
    }

    @Override
    public void setSolidsRate(int solidsRate) {
        this.solidsRate = solidsRate;
    }

    @Override
    public int getLiquidsRate() {
        return liquidsRate;
    }

    @Override
    public void setLiquidsRate(int liquidsRate) {
        this.liquidsRate = liquidsRate;
    }

    @Override
    public int getMaxSolids() {
        return maxSolids;
    }

    public void setMaxSolids(int maxSolids) {
        this.maxSolids = maxSolids;
    }

    @Override
    public int getMaxLiquids() {
        return maxLiquids;
    }

    public void setMaxLiquids(int maxLiquids) {
        this.maxLiquids = maxLiquids;
    }


    @Override
    public double getBladderAccidentWarning() {
        return round(bladderAccidentWarning, 4);
    }

    @Override
    public void setBladderAccidentWarning(double bladderAccidentWarning) {
        this.bladderAccidentWarning = clamp(bladderAccidentWarning, 0.1, 1);
    }

    @Override
    public double getBowelAccidentWarning() {
        return round(bowelAccidentWarning, 4);
    }

    @Override
    public void setBowelAccidentWarning(double bowelAccidentWarning) {
        this.bowelAccidentWarning = clamp(bowelAccidentWarning, 0.1, 1);
    }

    @Override
    public int getBladderAccidentFrequency() {
        return bladderAccidentFrequency;
    }

    @Override
    public void setBladderAccidentFrequency(int v) {
        bladderAccidentFrequency = clamp(v, 1, Integer.MAX_VALUE);
    }

    @Override
    public int getBowelAccidentFrequency() {
        return bowelAccidentFrequency;
    }

    @Override
    public void setBowelAccidentFrequency(int v) {
        bowelAccidentFrequency = clamp(v, 1, Integer.MAX_VALUE);
    }

    @Override
    public double getBladderAccidentAmountPercent() {
        return bladderAccidentAmountPercent;
    }

    @Override
    public void setBladderAccidentAmountPercent(double v) {
        bladderAccidentAmountPercent = v;
    }

    @Override
    public double getBowelAccidentAmountPercent() {
        return bowelAccidentAmountPercent;
    }

    @Override
    public void setBowelAccidentAmountPercent(double v) {
        bowelAccidentAmountPercent = v;
    }

    /**
     * To string method for debugging
     */
    @Override
    public String toString() {
        return "MetabolismImpl{" +
                "liquids=" + getLiquids() +
                ", solids=" + getSolids() +
                ", bladder=" + getBladder() +
                ", bowels=" + getBowels() +
                ", bladderCapacity=" + getBladderCapacity() +
                ", bowelCapacity=" + getBowelCapacity() +
                ", solidsRate=" + getSolidsRate() +
                ", liquidsRate=" + getLiquidsRate() +
                ", maxSolids=" + getMaxSolids() +
                ", maxLiquids=" + getMaxLiquids() +
                ", bladderAccidentWarning=" + getBladderAccidentWarning() +
                ", bowelAccidentWarning=" + getBowelAccidentWarning() +
                ", bladderAccidentFrequency=" + getBladderAccidentFrequency() +
                ", bowelAccidentFrequency=" + getBowelAccidentFrequency() +
                ", bladderAccidentAmountPercent=" + getBladderAccidentAmountPercent() +
                ", bowelAccidentAmountPercent=" + getBowelAccidentAmountPercent() +
                '}';
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(getLiquids());
        buffer.writeInt(getMaxLiquids());
        buffer.writeInt(getLiquidsRate());
        buffer.writeInt(getSolids());
        buffer.writeInt(getMaxSolids());
        buffer.writeInt(getSolidsRate());
        buffer.writeInt(getBladder());
        buffer.writeInt(getBladderCapacity());
        buffer.writeDouble(getBladderAccidentWarning());
        buffer.writeInt(getBowels());
        buffer.writeInt(getBowelCapacity());
        buffer.writeDouble(getBowelAccidentWarning());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        setLiquids(additionalData.readInt());
        setMaxLiquids(additionalData.readInt());
        setLiquidsRate(additionalData.readInt());
        setSolids(additionalData.readInt());
        setMaxSolids(additionalData.readInt());
        setSolidsRate(additionalData.readInt());
        setBladder(additionalData.readInt());
        setBladderCapacity(additionalData.readInt());
        setBladderAccidentWarning(additionalData.readDouble());
        setBowels(additionalData.readInt());
        setBowelCapacity(additionalData.readInt());
        setBowelAccidentWarning(additionalData.readDouble());
    }
}
