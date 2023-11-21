package ninja.crinkle.mod.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import ninja.crinkle.mod.config.MetabolismConfig;
import ninja.crinkle.mod.metabolism.MetabolismSettings;
import org.jetbrains.annotations.NotNull;

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
    private double bladderContinence;
    private double bowelContinence;


    public MetabolismImpl() {
        bladderCapacity = MetabolismConfig.bladderCapacity;
        bowelCapacity = MetabolismConfig.bowelCapacity;
        solidsRate = MetabolismConfig.solidsRate;
        liquidsRate = MetabolismConfig.liquidsRate;
        maxSolids = MetabolismConfig.maxSolids;
        maxLiquids = MetabolismConfig.maxLiquids;
        bladderContinence = MetabolismConfig.bladderContinence;
        bowelContinence = MetabolismConfig.bowelContinence;
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
        tag.putDouble(MetabolismSettings.BLADDER_CONTINENCE.key(), getBladderContinence());
        tag.putDouble(MetabolismSettings.BOWEL_CONTINENCE.key(), getBowelContinence());
        return tag;
    }

    /**
     * Deserialize the metabolism from NBT.
     *
     * @param nbt The NBT to deserialize
     */
    @Override
    public void deserializeNBT(@NotNull CompoundTag nbt) {
        setLiquids(nbt.getInt(MetabolismSettings.LIQUIDS.key()));
        setSolids(nbt.getInt(MetabolismSettings.SOLIDS.key()));
        setBladder(nbt.getInt(MetabolismSettings.BLADDER.key()));
        setBowels(nbt.getInt(MetabolismSettings.BOWELS.key()));
        setBladderCapacity(nbt.getInt(MetabolismSettings.BLADDER_CAPACITY.key()));
        setBowelCapacity(nbt.getInt(MetabolismSettings.BOWEL_CAPACITY.key()));
        setSolidsRate(nbt.getInt(MetabolismSettings.SOLIDS_RATE.key()));
        setLiquidsRate(nbt.getInt(MetabolismSettings.LIQUIDS_RATE.key()));
        setMaxLiquids(nbt.getInt(MetabolismSettings.MAX_LIQUIDS.key()));
        setMaxSolids(nbt.getInt(MetabolismSettings.MAX_SOLIDS.key()));
        setBladderContinence(nbt.getDouble(MetabolismSettings.BLADDER_CONTINENCE.key()));
        setBowelContinence(nbt.getDouble(MetabolismSettings.BOWEL_CONTINENCE.key()));
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
    public double getBladderContinence() {
        return round(bladderContinence, 4);
    }

    @Override
    public void setBladderContinence(double bladderContinence) {
        this.bladderContinence = clamp(bladderContinence, 0.1, 1);
    }

    @Override
    public double getBowelContinence() {
        return round(bowelContinence, 4);
    }

    @Override
    public void setBowelContinence(double bowelsContinence) {
        this.bowelContinence = clamp(bowelsContinence, 0.1, 1);
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
                ", bladderContinence=" + getBladderContinence() +
                ", bowelContinence=" + getBowelContinence() +
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
        buffer.writeDouble(getBladderContinence());
        buffer.writeInt(getBowels());
        buffer.writeInt(getBowelCapacity());
        buffer.writeDouble(getBowelContinence());
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
        setBladderContinence(additionalData.readDouble());
        setBowels(additionalData.readInt());
        setBowelCapacity(additionalData.readInt());
        setBowelContinence(additionalData.readDouble());
    }
}
