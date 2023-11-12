package ninja.crinkle.mod.metabolism.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Metabolism capability interface.
 * This interface is used to store the metabolism of a player in a compound NBT tag.
 *
 * @author Galen
 * @see MetabolismImpl
 * @see net.minecraftforge.common.util.INBTSerializable
 */
@AutoRegisterCapability
public interface IMetabolism extends INBTSerializable<CompoundTag> {
    /**
     * Get the amount of liquids in the player's stomach
     *
     * @return The amount of liquids in the player's stomach
     */
    double getLiquids();

    /**
     * Set the amount of liquids in the player's stomach
     *
     * @param value The amount of liquids in the player's stomach
     */
    void setLiquids(double value);

    /**
     * Get the amount of solids in the player's stomach
     *
     * @return The amount of solids in the player's stomach
     */
    double getSolids();

    /**
     * Set the amount of solids in the player's stomach
     *
     * @param value The amount of solids in the player's stomach
     */
    void setSolids(double value);


    /**
     * Get the amount of fluid in the player's bladder
     *
     * @return The amount of fluid in the player's bladder
     */
    double getBladder();

    /**
     * Set the amount of fluid in the player's bladder
     *
     * @param value The amount of fluid in the player's bladder
     */
    void setBladder(double value);

    /**
     * Get the amount of solids in the player's bowels
     *
     * @return The amount of solids in the player's bowels
     */
    double getBowels();

    /**
     * Set the amount of solids in the player's bowels
     *
     * @param value The amount of solids in the player's bowels
     */
    void setBowels(double value);

    /**
     * Get the capacity of the player's bladder
     *
     * @return The capacity of the player's bladder
     * @implNote The maximum is determined by {@link #getMaxLiquids()}
     */
    double getBladderCapacity();

    /**
     * Set the capacity of the player's bladder
     *
     * @param value The capacity of the player's bladder
     * @implNote The maximum is determined by {@link #getMaxLiquids()}
     */
    void setBladderCapacity(double value);

    /**
     * Get the capacity of the player's bowels
     *
     * @return The capacity of the player's bowels
     * @implNote The maximum is determined by {@link #getMaxSolids()}
     */
    double getBowelCapacity();

    /**
     * Set the capacity of the player's bowels
     *
     * @param value The capacity of the player's bowels
     * @implNote The maximum is determined by {@link #getMaxSolids()}
     */
    void setBowelCapacity(double value);

    /**
     * Get the rate at which solids are digested
     *
     * @return The rate at which solids are digested
     */
    double getSolidsRate();

    /**
     * Set the rate at which solids are digested
     *
     * @param value The rate at which solids are digested
     */
    void setSolidsRate(double value);

    /**
     * Get the rate at which liquids are digested
     *
     * @return The rate at which liquids are digested
     */
    double getLiquidsRate();

    /**
     * Set the rate at which liquids are digested
     *
     * @param value The rate at which liquids are digested
     */
    void setLiquidsRate(double value);

    /**
     * Get the maximum amount of solids the stomach can hold
     *
     * @return The maximum amount of solids the stomach can hold
     * @implNote This also controls the maximum size of the bowels
     */
    double getMaxSolids();

    /**
     * Set the maximum amount of solids the stomach can hold
     *
     * @param value The maximum amount of solids the stomach can hold
     * @implNote This also controls the maximum size of the bowels
     */
    void setMaxSolids(double value);

    /**
     * Get the maximum amount of liquids the stomach can hold
     *
     * @return The maximum amount of liquids the stomach can hold
     * @implNote This also controls the maximum size of the bladder
     */
    double getMaxLiquids();

    /**
     * Set the maximum amount of liquids the stomach can hold
     *
     * @param value The maximum amount of liquids the stomach can hold
     * @implNote This also controls the maximum size of the bladder
     */
    void setMaxLiquids(double value);

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
