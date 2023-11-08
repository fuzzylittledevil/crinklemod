package ninja.crinkle.mod.metabolism.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import ninja.crinkle.mod.CrinkleMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Metabolism static class.
 * This class is used to attach the metabolism capability to an entity.
 * This class also contains constants used by the metabolism capability.
 * @author Galen
 * @see IMetabolism
 * @see MetabolismProvider
 * @see net.minecraftforge.common.capabilities.ICapabilityProvider
 * @see net.minecraftforge.common.util.INBTSerializable
 * @see net.minecraftforge.common.capabilities.CapabilityManager
 * @see net.minecraftforge.common.capabilities.CapabilityToken
 */
public class Metabolism {
    /**
     * The instance of the metabolism capability.
     * @see IMetabolism
     */
    public static final Capability<IMetabolism> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {
    });

    /**
     * The name of the metabolism capability.
     */
    public static final String NAME = "metabolism";

    //TODO: Move to config
    public static final double MAX_LIQUIDS = 100;

    //TODO: Move to config
    public static final double MAX_SOLIDS = 100;

    //TODO: Move to config
    public static final double MIN_LIQUIDS = 0;

    //TODO: Move to config
    public static final double MIN_SOLIDS = 0;

    /**
     * The NBT key for the liquids value.
     */
    public static final String NBT_KEY_LIQUIDS = "liquids";

    /**
     * The NBT key for the solids value.
     */
    public static final String NBT_KEY_SOLIDS = "solids";

    /**
     * The NBT key for the bladder value.
     */
    public static final String NBT_KEY_BLADDER = "bladder";

    /**
     * The NBT key for the bowels value.
     */
    public static final String NBT_KEY_BOWELS = "bowels";

    /**
     * The NBT key for the bladder desperation value.
     */
    public static final String NBT_KEY_BLADDER_DESPERATION = "bladderDesperation";

    /**
     * The NBT key for the bowels desperation value.
     */
    public static final String NBT_KEY_BOWELS_DESPERATION = "bowelsDesperation";

    /**
     * Private constructor to prevent instantiation.
     */
    private Metabolism() {
    }

    /**
     * Attach the metabolism capability to an entity.
     * @param event The event that is fired when an entity is created and ready for attachment.
     * @see net.minecraftforge.event.AttachCapabilitiesEvent
     */
    public static void attach(final @NotNull AttachCapabilitiesEvent<Entity> event) {
        event.addCapability(MetabolismProvider.IDENTIFIER, new MetabolismProvider());
    }

    /**
     * Metabolism provider class.
     * This class is used to provide the metabolism capability to an entity.
     * @see IMetabolism
     * @see net.minecraftforge.common.capabilities.ICapabilityProvider
     * @see net.minecraftforge.common.util.INBTSerializable
     */
    private static class MetabolismProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        public static final ResourceLocation IDENTIFIER = new ResourceLocation(CrinkleMod.MODID, NAME);
        private final IMetabolism metabolism = new MetabolismImpl();
        private final LazyOptional<IMetabolism> instance = LazyOptional.of(() -> metabolism);

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
            return INSTANCE.orEmpty(cap, instance);
        }

        /**
         * Invalidate the capability.
         */
        void invalidate() {
            instance.invalidate();
        }

        /**
         * Serialize the capability to NBT.
         * @return The serialized capability
         */
        @Override
        public CompoundTag serializeNBT() {
            return metabolism.serializeNBT();
        }

        /**
         * Deserialize the capability from NBT.
         * @param nbt The serialized capability
         */
        @Override
        public void deserializeNBT(CompoundTag nbt) {
            metabolism.deserializeNBT(nbt);
        }
    }
}
