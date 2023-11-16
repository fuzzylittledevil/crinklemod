package ninja.crinkle.mod.undergarment.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.metabolism.common.capabilities.MetabolismProvider;
import ninja.crinkle.mod.undergarment.common.config.UndergarmentConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UndergarmentProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final String NAME = "undergarment";
    public static final ResourceLocation IDENTIFIER = new ResourceLocation(CrinkleMod.MODID, NAME);
    private final IUndergarment storage;
    private final Item item;
    private final LazyOptional<IUndergarment> instance;

    private UndergarmentProvider(Item item) {
        this.item = item;
        storage = new UndergarmentImpl(item);
        instance = LazyOptional.of(() -> storage);
    }

    /**
     * Get the capability of an entity.
     *
     * @param cap  The capability to check
     * @param side The Side to check from,
     *             <strong>CAN BE NULL</strong>. Null is defined to represent 'internal' or 'self'
     * @param <T>  The type of the capability
     * @return The capability if it exists, or an empty optional if it does not
     */
    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (!UndergarmentConfig.undergarments.containsKey(item)) {
            return LazyOptional.empty();
        }
        return UndergarmentCapabilities.UNDERGARMENT.orEmpty(cap, instance);
    }

    /**
     * Serialize the capability to NBT.
     *
     * @return The serialized capability
     */
    @Override
    public CompoundTag serializeNBT() {
        return storage.serializeNBT();
    }

    /**
     * Deserialize the capability from NBT.
     *
     * @param nbt The serialized capability
     */
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        storage.deserializeNBT(nbt);
    }

    /**
     * Attach the metabolism capability to an entity.
     *
     * @param event The event that is fired when an entity is created and ready for attachment.
     * @see net.minecraftforge.event.AttachCapabilitiesEvent
     */
    public static void attach(final @NotNull AttachCapabilitiesEvent<ItemStack> event) {
        if (!UndergarmentConfig.undergarments.containsKey(event.getObject().getItem())) {
            return;
        }
        event.addCapability(IDENTIFIER, new UndergarmentProvider(event.getObject().getItem()));
    }
}
