package ninja.crinkle.mod.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import ninja.crinkle.mod.capabilities.versioning.MetabolismVersions;
import ninja.crinkle.mod.metabolism.MetabolismSettings;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * Metabolism capability implementation.
 * This class is used to store the metabolism of a player in a compound NBT tag.
 *
 * @author Galen
 * @see IMetabolism
 */
public class MetabolismImpl implements IMetabolism {
    private MetabolismVersions version;
    private boolean enabled;
    private int timer;
    private int numberOneRolls;
    private int numberOneSafeRolls;
    private double numberOneChance;
    private int numberTwoRolls;
    private int numberTwoSafeRolls;
    private double numberTwoChance;

    public MetabolismImpl() {
        enabled = false;
        timer = MetabolismSettings.TIMER.getDefault();
        numberOneRolls = MetabolismSettings.NUMBER_ONE_ROLLS.getDefault();
        numberOneSafeRolls = MetabolismSettings.NUMBER_ONE_SAFE_ROLLS.getDefault();
        numberOneChance = MetabolismSettings.NUMBER_ONE_CHANCE.getDefault();
        numberTwoRolls = MetabolismSettings.NUMBER_TWO_ROLLS.getDefault();
        numberTwoSafeRolls = MetabolismSettings.NUMBER_TWO_SAFE_ROLLS.getDefault();
        numberTwoChance = MetabolismSettings.NUMBER_TWO_CHANCE.getDefault();
    }

    /**
     * Serialize the metabolism to NBT.
     *
     * @return The serialized metabolism
     */
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString(MetabolismVersions.TAG_VERSION, version.name());
        tag.putBoolean(MetabolismSettings.ENABLED.key(), isEnabled());
        tag.putInt(MetabolismSettings.TIMER.key(), getTimer());
        tag.putInt(MetabolismSettings.NUMBER_ONE_ROLLS.key(), getNumberOneRolls());
        tag.putInt(MetabolismSettings.NUMBER_ONE_SAFE_ROLLS.key(), this.getNumberOneSafeRolls());
        tag.putDouble(MetabolismSettings.NUMBER_ONE_CHANCE.key(), getNumberOneChance());
        tag.putInt(MetabolismSettings.NUMBER_TWO_ROLLS.key(), getNumberTwoRolls());
        tag.putInt(MetabolismSettings.NUMBER_TWO_SAFE_ROLLS.key(), getNumberTwoSafeRolls());
        tag.putDouble(MetabolismSettings.NUMBER_TWO_CHANCE.key(), getNumberTwoChance());
        return tag;
    }

    /**
     * Deserialize the metabolism from NBT.
     *
     * @param nbt The NBT to deserialize
     */
    @Override
    public void deserializeNBT(@NotNull CompoundTag nbt) {
        version = MetabolismVersions.fromNBT(nbt);
        safeSet(nbt, MetabolismSettings.ENABLED.key(), (tag, key) -> setEnabled(tag.getBoolean(key)));
        safeSet(nbt, MetabolismSettings.TIMER.key(), (tag, key) -> setTimer(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.NUMBER_ONE_ROLLS.key(), (tag, key) -> setNumberOneRolls(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.NUMBER_ONE_SAFE_ROLLS.key(), (tag, key) -> setNumberOneSafeRolls(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.NUMBER_ONE_CHANCE.key(), (tag, key) -> setNumberOneChance(tag.getDouble(key)));
        safeSet(nbt, MetabolismSettings.NUMBER_TWO_ROLLS.key(), (tag, key) -> setNumberTwoRolls(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.NUMBER_TWO_SAFE_ROLLS.key(), (tag, key) -> setNumberTwoSafeRolls(tag.getInt(key)));
        safeSet(nbt, MetabolismSettings.NUMBER_TWO_CHANCE.key(), (tag, key) -> setNumberTwoChance(tag.getDouble(key)));
    }

    private void safeSet(CompoundTag nbt, String key, BiConsumer<CompoundTag, String> setter) {
        if (nbt.contains(key)) {
            setter.accept(nbt, key);
        }
    }

    @Override
    public int getTimer() {
        return timer;
    }

    @Override
    public void setTimer(int timer) {
        this.timer = timer;
    }

    /**
     * To string method for debugging
     */
    @Override
    public String toString() {
        return "MetabolismImpl{" +
                "timer=" + getTimer() +
                ", numberOneRolls=" + getNumberOneRolls() +
                ", numberOneSafeRolls=" + this.getNumberOneSafeRolls() +
                ", numberOneChance=" + getNumberOneChance() +
                ", numberTwoChance=" + getNumberTwoChance() +
                '}';
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(getTimer());
        buffer.writeInt(getNumberOneRolls());
        buffer.writeInt(this.getNumberOneSafeRolls());
        buffer.writeDouble(getNumberOneChance());
        buffer.writeDouble(getNumberTwoChance());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        setTimer(additionalData.readInt());
        setNumberOneRolls(additionalData.readInt());
        setNumberOneSafeRolls(additionalData.readInt());
        setNumberOneChance(additionalData.readDouble());
        setNumberTwoChance(additionalData.readDouble());
    }

    @Override
    public int getNumberOneSafeRolls() {
        return numberOneSafeRolls;
    }

    @Override
    public void setNumberOneSafeRolls(int safeRolls) {
        this.numberOneSafeRolls = safeRolls;
    }

    @Override
    public int getNumberOneRolls() {
        return numberOneRolls;
    }

    @Override
    public void setNumberOneRolls(int rolls) {
        this.numberOneRolls = rolls;
    }

    @Override
    public double getNumberOneChance() {
        return numberOneChance;
    }

    @Override
    public void setNumberOneChance(double numberOneChance) {
        this.numberOneChance = numberOneChance;
    }

    @Override
    public double getNumberTwoChance() {
        return numberTwoChance;
    }

    @Override
    public void setNumberTwoChance(double numberTwoChance) {
        this.numberTwoChance = numberTwoChance;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int getNumberTwoRolls() {
        return numberTwoRolls;
    }

    @Override
    public void setNumberTwoRolls(int numberTwoRolls) {
        this.numberTwoRolls = numberTwoRolls;
    }

    @Override
    public int getNumberTwoSafeRolls() {
        return numberTwoSafeRolls;
    }

    @Override
    public void setNumberTwoSafeRolls(int numberTwoSafeRolls) {
        this.numberTwoSafeRolls = numberTwoSafeRolls;
    }
}
