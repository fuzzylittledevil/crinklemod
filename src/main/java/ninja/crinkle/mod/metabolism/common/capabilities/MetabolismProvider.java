package ninja.crinkle.mod.metabolism.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import ninja.crinkle.mod.CrinkleMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Metabolism provider class.
 * This class is used to provide the metabolism capability to an entity.
 * @see IMetabolism
 * @see net.minecraftforge.common.capabilities.ICapabilityProvider
 * @see net.minecraftforge.common.util.INBTSerializable
 */
public class MetabolismProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final String NAME = "metabolism";
    public static final ResourceLocation IDENTIFIER = new ResourceLocation(CrinkleMod.MODID, NAME);
    private final IMetabolism storage = new MetabolismImpl();
    private final LazyOptional<IMetabolism> instance = LazyOptional.of(() -> storage);

    /**
     * Get the capability of an entity.
     * @param cap The capability to check
     * @param side The Side to check from,
     *   <strong>CAN BE NULL</strong>. Null is defined to represent 'internal' or 'self'
     * @return The capability if it exists, or an empty optional if it does not
     * @param <T> The type of the capability
     */
    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return Capabilities.METABOLISM.orEmpty(cap, instance);
    }

    /**
     * Serialize the capability to NBT.
     * @return The serialized capability
     */
    @Override
    public CompoundTag serializeNBT() {
        return storage.serializeNBT();
    }

    /**
     * Deserialize the capability from NBT.
     * @param nbt The serialized capability
     */
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        storage.deserializeNBT(nbt);
    }

    /**
     * Attach the metabolism capability to an entity.
     * @param event The event that is fired when an entity is created and ready for attachment.
     * @see net.minecraftforge.event.AttachCapabilitiesEvent
     */
    public static void attach(final @NotNull AttachCapabilitiesEvent<Player> event) {
        event.addCapability(IDENTIFIER, new MetabolismProvider());
    }
}
