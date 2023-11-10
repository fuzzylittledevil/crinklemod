package ninja.crinkle.mod.metabolism.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import ninja.crinkle.mod.metabolism.common.config.MetabolismConfig;
import org.jetbrains.annotations.NotNull;

import static ninja.crinkle.mod.util.MathUtil.clamp;
import static ninja.crinkle.mod.util.MathUtil.round;

/**
 * Metabolism capability implementation.
 * This class is used to store the metabolism of a player in a compound NBT tag.
 * @author Galen
 * @see IMetabolism
 */
public class MetabolismImpl implements IMetabolism {
    private static final String NBT_KEY_LIQUIDS = "liquids";
    private static final String NBT_KEY_SOLIDS = "solids";
    private static final String NBT_KEY_BLADDER = "bladder";
    private static final String NBT_KEY_BOWELS = "bowels";
    private static final String NBT_KEY_BLADDER_DESPERATION = "bladderDesperation";
    private static final String NBT_KEY_BOWEL_DESPERATION = "bowelDesperation";
    private static final String NBT_KEY_BLADDER_CAPACITY = "bladderCapacity";
    private static final String NBT_KEY_BOWEL_CAPACITY = "bowelCapacity";
    private static final String NBT_KEY_SOLIDS_RATE = "solidsRate";
    private static final String NBT_KEY_LIQUIDS_RATE = "liquidsRate";
    private static final String NBT_KEY_MAX_LIQUIDS = "maxLiquids";
    private static final String NBT_KEY_MAX_SOLIDS = "maxSolids";
    private static final String NBT_KEY_BLADDER_CONTINENCE = "bladderContinence";
    private static final String NBT_KEY_BOWEL_CONTINENCE = "bowelContinence";

    private double liquids;
    private double solids;
    private double bladder;
    private double bowels;
    private double bladderDesperation;
    private double bowelDesperation;
    private double bladderCapacity;
    private double bowelCapacity;
    private double solidsRate;
    private double liquidsRate;
    private double maxSolids;
    private double maxLiquids;
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
     * @return The serialized metabolism
     */
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble(NBT_KEY_LIQUIDS, getLiquids());
        tag.putDouble(NBT_KEY_SOLIDS, getSolids());
        tag.putDouble(NBT_KEY_BLADDER, getBladder());
        tag.putDouble(NBT_KEY_BOWELS, getBowels());
        tag.putDouble(NBT_KEY_BLADDER_DESPERATION, getBladderDesperation());
        tag.putDouble(NBT_KEY_BOWEL_DESPERATION, getBowelDesperation());
        tag.putDouble(NBT_KEY_BLADDER_CAPACITY, getBladderCapacity());
        tag.putDouble(NBT_KEY_BOWEL_CAPACITY, getBowelCapacity());
        tag.putDouble(NBT_KEY_SOLIDS_RATE, getSolidsRate());
        tag.putDouble(NBT_KEY_LIQUIDS_RATE, getLiquidsRate());
        tag.putDouble(NBT_KEY_MAX_LIQUIDS, getMaxLiquids());
        tag.putDouble(NBT_KEY_MAX_SOLIDS, getMaxSolids());
        tag.putDouble(NBT_KEY_BLADDER_CONTINENCE, getBladderContinence());
        tag.putDouble(NBT_KEY_BOWEL_CONTINENCE, getBowelContinence());
        return tag;
    }

    /**
     * Deserialize the metabolism from NBT.
     * @param nbt The NBT to deserialize
     */
    @Override
    public void deserializeNBT(@NotNull CompoundTag nbt) {
        setLiquids(nbt.getDouble(NBT_KEY_LIQUIDS));
        setSolids(nbt.getDouble(NBT_KEY_SOLIDS));
        setBladder(nbt.getDouble(NBT_KEY_BLADDER));
        setBowels(nbt.getDouble(NBT_KEY_BOWELS));
        setBladderDesperation(nbt.getDouble(NBT_KEY_BLADDER_DESPERATION));
        setBowelDesperation(nbt.getDouble(NBT_KEY_BOWEL_DESPERATION));
        setBladderCapacity(nbt.getDouble(NBT_KEY_BLADDER_CAPACITY));
        setBowelCapacity(nbt.getDouble(NBT_KEY_BOWEL_CAPACITY));
        setSolidsRate(nbt.getDouble(NBT_KEY_SOLIDS_RATE));
        setLiquidsRate(nbt.getDouble(NBT_KEY_LIQUIDS_RATE));
        setMaxLiquids(nbt.getDouble(NBT_KEY_MAX_LIQUIDS));
        setMaxSolids(nbt.getDouble(NBT_KEY_MAX_SOLIDS));
        setBladderContinence(nbt.getDouble(NBT_KEY_BLADDER_CONTINENCE));
        setBowelContinence(nbt.getDouble(NBT_KEY_BOWEL_CONTINENCE));
    }

    /**
     * Get the amount of liquids in the player's stomach.
     * @return The amount of liquids in the player's stomach.
     */
    @Override
    public double getLiquids() {
        return round(liquids, 2);
    }

    /**
     * Set the amount of liquids in the player's stomach.
     * Sets the sync flag to true.
     * @param liquids The amount of liquids in the player's stomach
     */
    @Override
    public void setLiquids(double liquids) {
        this.liquids = clamp(liquids, 0, maxLiquids);
    }

    /**
     * Get the amount of solids in the player's stomach.
     * @return The amount of solids in the player's stomach.
     */
    @Override
    public double getSolids() {
        return round(solids, 2);
    }

    /**
     * Set the amount of solids in the player's stomach.
     * Sets the sync flag to true.
     * @param solids The amount of solids in the player's stomach
     */
    @Override
    public void setSolids(double solids) {
        this.solids = clamp(solids, 0, maxSolids);
    }

    /**
     * Get the amount of fluid in the player's bladder.
     * @return The amount of fluid in the player's bladder.
     */
    @Override
    public double getBladder() {
        return round(bladder, 2);
    }

    /**
     * Set the amount of fluid in the player's bladder.
     * Sets the sync flag to true.
     * @param bladder The amount of fluid in the player's bladder
     */
    @Override
    public void setBladder(double bladder) {
        this.bladder = clamp(bladder, 0, bladderCapacity);
    }

    /**
     * Get the amount of solids in the player's bowels.
     * @return The amount of solids in the player's bowels.
     */
    @Override
    public double getBowels() {
        return round(bowels, 2);
    }

    /**
     * Set the amount of solids in the player's bowels.
     * Sets the sync flag to true.
     * @param bowels The amount of solids in the player's bowels
     */
    @Override
    public void setBowels(double bowels) {
        this.bowels = clamp(bowels, 0, bowelCapacity);
    }

    /**
     * Get the desperation of the player's bladder.
     * @return The desperation of the player's bladder.
     */
    @Override
    public double getBladderDesperation() {
        return round(bladderDesperation, 4);
    }

    /**
     * Set the desperation of the player's bladder.
     * Sets the sync flag to true.
     * @param bladderDesperation The desperation of the player's bladder
     */
    @Override
    public void setBladderDesperation(double bladderDesperation) {
        this.bladderDesperation = clamp(bladderDesperation, 0, 1);
    }

    /**
     * Get the desperation of the player's bowels.
     * @return The desperation of the player's bowels.
     */
    @Override
    public double getBowelDesperation() {
        return round(bowelDesperation, 4);
    }

    /**
     * Set the desperation of the player's bowels.
     * Sets the sync flag to true.
     * @param bowelsDesperation The desperation of the player's bowels
     */
    @Override
    public void setBowelDesperation(double bowelsDesperation) {
        this.bowelDesperation = clamp(bowelsDesperation, 0, 1);
    }

    @Override
    public double getBladderCapacity() {
        return round(bladderCapacity, 2);
    }

    @Override
    public void setBladderCapacity(double bladderCapacity) {
        this.bladderCapacity = clamp(bladderCapacity, 0, maxLiquids);
    }

    @Override
    public double getBowelCapacity() {
        return round(bowelCapacity, 2);
    }

    @Override
    public void setBowelCapacity(double bowelsCapacity) {
        this.bowelCapacity = clamp(bowelsCapacity, 0, maxSolids);
    }

    @Override
    public double getSolidsRate() {
        return round(solidsRate, 4);
    }

    @Override
    public void setSolidsRate(double solidsRate) {
        this.solidsRate = solidsRate;
    }

    @Override
    public double getLiquidsRate() {
        return round(liquidsRate, 4);
    }

    @Override
    public void setLiquidsRate(double liquidsRate) {
        this.liquidsRate = liquidsRate;
    }

    @Override
    public double getMaxSolids() {
        return round(maxSolids, 2);
    }

    public void setMaxSolids(double maxSolids) {
        this.maxSolids = maxSolids;
    }

    @Override
    public double getMaxLiquids() {
        return round(maxLiquids, 2);
    }

    public void setMaxLiquids(double maxLiquids) {
        this.maxLiquids = maxLiquids;
    }


    @Override
    public double getBladderContinence() {
        return round(bladderContinence, 4);
    }

    @Override
    public void setBladderContinence(double bladderContinence) {
        this.bladderContinence = bladderContinence;
    }

    @Override
    public double getBowelContinence() {
        return round(bowelContinence, 4);
    }

    @Override
    public void setBowelContinence(double bowelsContinence) {
        this.bowelContinence = bowelsContinence;
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
                ", bladderDesperation=" + getBladderDesperation() +
                ", bowelDesperation=" + getBowelDesperation() +
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
}
