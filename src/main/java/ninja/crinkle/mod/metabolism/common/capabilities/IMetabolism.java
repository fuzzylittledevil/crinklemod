package ninja.crinkle.mod.metabolism.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import ninja.crinkle.mod.lib.common.settings.Setting;

/**
 * Metabolism capability interface.
 * This interface is used to store the metabolism of a player in a compound NBT tag.
 *
 * @author Galen
 * @see MetabolismImpl
 * @see net.minecraftforge.common.util.INBTSerializable
 */
@AutoRegisterCapability
public interface IMetabolism extends INBTSerializable<CompoundTag>, IEntityAdditionalSpawnData {
    /**
     * Get the amount of liquids in the player's stomach
     *
     * @return The amount of liquids in the player's stomach
     */
    int getLiquids();

    /**
     * Set the amount of liquids in the player's stomach
     *
     * @param value The amount of liquids in the player's stomach
     */
    void setLiquids(int value);

    /**
     * Get the amount of solids in the player's stomach
     *
     * @return The amount of solids in the player's stomach
     */
    int getSolids();

    /**
     * Set the amount of solids in the player's stomach
     *
     * @param value The amount of solids in the player's stomach
     */
    void setSolids(int value);


    /**
     * Get the amount of fluid in the player's bladder
     *
     * @return The amount of fluid in the player's bladder
     */
    int getBladder();

    /**
     * Set the amount of fluid in the player's bladder
     *
     * @param value The amount of fluid in the player's bladder
     */
    void setBladder(int value);

    /**
     * Get the amount of solids in the player's bowels
     *
     * @return The amount of solids in the player's bowels
     */
    int getBowels();

    /**
     * Set the amount of solids in the player's bowels
     *
     * @param value The amount of solids in the player's bowels
     */
    void setBowels(int value);

    /**
     * Get the capacity of the player's bladder
     *
     * @return The capacity of the player's bladder
     * @implNote The maximum is determined by {@link #getMaxLiquids()}
     */
    int getBladderCapacity();

    /**
     * Set the capacity of the player's bladder
     *
     * @param value The capacity of the player's bladder
     * @implNote The maximum is determined by {@link #getMaxLiquids()}
     */
    void setBladderCapacity(int value);

    /**
     * Get the capacity of the player's bowels
     *
     * @return The capacity of the player's bowels
     * @implNote The maximum is determined by {@link #getMaxSolids()}
     */
    int getBowelCapacity();

    /**
     * Set the capacity of the player's bowels
     *
     * @param value The capacity of the player's bowels
     * @implNote The maximum is determined by {@link #getMaxSolids()}
     */
    void setBowelCapacity(int value);

    /**
     * Get the rate at which solids are digested
     *
     * @return The rate at which solids are digested
     */
    int getSolidsRate();

    /**
     * Set the rate at which solids are digested
     *
     * @param value The rate at which solids are digested
     */
    void setSolidsRate(int value);

    /**
     * Get the rate at which liquids are digested
     *
     * @return The rate at which liquids are digested
     */
    int getLiquidsRate();

    /**
     * Set the rate at which liquids are digested
     *
     * @param value The rate at which liquids are digested
     */
    void setLiquidsRate(int value);

    /**
     * Get the maximum amount of solids the stomach can hold
     *
     * @return The maximum amount of solids the stomach can hold
     * @implNote This also controls the maximum size of the bowels
     */
    int getMaxSolids();

    /**
     * Set the maximum amount of solids the stomach can hold
     *
     * @param value The maximum amount of solids the stomach can hold
     * @implNote This also controls the maximum size of the bowels
     */
    void setMaxSolids(int value);

    /**
     * Get the maximum amount of liquids the stomach can hold
     *
     * @return The maximum amount of liquids the stomach can hold
     * @implNote This also controls the maximum size of the bladder
     */
    int getMaxLiquids();

    /**
     * Set the maximum amount of liquids the stomach can hold
     *
     * @param value The maximum amount of liquids the stomach can hold
     * @implNote This also controls the maximum size of the bladder
     */
    void setMaxLiquids(int value);

    /**
     * Get the bladder continence of the player
     *
     * @return The bladder continence of the player
     * @implNote This is a value between 0 and 1, where 0 is no continence and 1 is full continence
     */
    double getBladderContinence();

    /**
     * Set the bladder continence of the player
     *
     * @param value The bladder continence of the player
     * @implNote This is a value between 0 and 1, where 0 is no continence and 1 is full continence
     */
    void setBladderContinence(double value);

    /**
     * Get the bowels continence of the player
     *
     * @return The bowels continence of the player
     * @implNote This is a value between 0 and 1, where 0 is no continence and 1 is full continence
     */
    double getBowelContinence();

    /**
     * Set the bladder continence of the player
     *
     * @param value The bladder continence of the player
     * @implNote This is a value between 0 and 1, where 0 is no continence and 1 is full continence
     */
    void setBowelContinence(double value);
}
