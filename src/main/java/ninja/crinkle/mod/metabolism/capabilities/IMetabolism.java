package ninja.crinkle.mod.metabolism.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Metabolism capability interface.
 * This interface is used to store the metabolism of a player in a compound NBT tag.
 * @author Galen
 * @see MetabolismImpl
 * @see net.minecraftforge.common.util.INBTSerializable
 */
@AutoRegisterCapability
public interface IMetabolism extends INBTSerializable<CompoundTag> {
    /**
     * Consume an item
     * @param item The item to consume
     * @param entity The entity consuming the item
     */
    void consume(ItemStack item, LivingEntity entity);

    /**
     * Reset the metabolism of a player
     * @param player The player to reset
     */
    void reset(Player player);

    /**
     * Tick the metabolism of a player
     * @param player The player to tick
     */
    void tick(Player player);

    /**
     * Sync the metabolism of a player to the client or server
     * @param entity The player to sync
     */
    void sync(Player entity);

    /**
     * Get the amount of liquids in the player's stomach
     * @return The amount of liquids in the player's stomach
     */
    double getLiquids();

    /**
     * Set the amount of liquids in the player's stomach
     * @param value The amount of liquids in the player's stomach
     */
    void setLiquids(double value);

    /**
     * Get the amount of solids in the player's stomach
     * @param value The amount of solids in the player's stomach
     * @param sync Whether to sync the value to the client or server
     */
    void setLiquids(double value, boolean sync);

    /**
     * Get the amount of solids in the player's stomach
     * @return The amount of solids in the player's stomach
     */
    double getSolids();

    /**
     * Set the amount of solids in the player's stomach
     * @param value The amount of solids in the player's stomach
     */
    void setSolids(double value);

    /**
     * Set the amount of solids in the player's stomach
     * @param solids The amount of solids in the player's stomach
     * @param sync Whether to sync the value to the client or server
     */
    void setSolids(double solids, boolean sync);

    /**
     * Get the amount of fluid in the player's bladder
     * @return The amount of fluid in the player's bladder
     */
    double getBladder();

    /**
     * Set the amount of fluid in the player's bladder
     * @param value The amount of fluid in the player's bladder
     */
    void setBladder(double value);

    /**
     * Set the amount of fluid in the player's bladder
     * @param bladder The amount of fluid in the player's bladder
     * @param sync Whether to sync the value to the client or server
     */
    void setBladder(double bladder, boolean sync);

    /**
     * Get the amount of solids in the player's bowels
     * @return The amount of solids in the player's bowels
     */
    double getBowels();

    /**
     * Set the amount of solids in the player's bowels
     * @param value The amount of solids in the player's bowels
     */
    void setBowels(double value);

    /**
     * Set the amount of solids in the player's bowels
     * @param bowels The amount of solids in the player's bowels
     * @param sync Whether to sync the value to the client or server
     */
    void setBowels(double bowels, boolean sync);

    /**
     * Get the desperation of the player's bowels
     * @return The desperation of the player's bowels
     */
    double getBowelsDesperation();

    /**
     * Set the desperation of the player's bowels
     * @param value The desperation of the player's bowels
     */
    void setBowelsDesperation(double value);

    /**
     * Set the desperation of the player's bowels
     * @param bowelsDesperation The desperation of the player's bowels
     * @param sync Whether to sync the value to the client or server
     */
    void setBowelsDesperation(double bowelsDesperation, boolean sync);

    /**
     * Get the desperation of the player's bladder
     * @return The desperation of the player's bladder
     */
    double getBladderDesperation();

    /**
     * Set the desperation of the player's bladder
     * @param value The desperation of the player's bladder
     */
    void setBladderDesperation(double value);

    /**
     * Set the desperation of the player's bladder
     * @param bladderDesperation The desperation of the player's bladder
     * @param sync Whether to sync the value to the client or server
     */
    void setBladderDesperation(double bladderDesperation, boolean sync);
}
