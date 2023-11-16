package ninja.crinkle.mod.metabolism.common.network.messages;

import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import ninja.crinkle.mod.lib.client.ClientHooks;
import ninja.crinkle.mod.metabolism.common.Metabolism;
import ninja.crinkle.mod.metabolism.common.capabilities.IMetabolism;
import ninja.crinkle.mod.metabolism.common.capabilities.MetabolismCapabilities;
import ninja.crinkle.mod.metabolism.common.capabilities.MetabolismImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * A message that is used to sync the metabolism of a player to the client or server.
 *
 * @see MetabolismImpl
 */
public class MetabolismUpdateMessage {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final int liquids;
    private final int solids;
    private final int bladder;
    private final int bowels;
    private final int bladderCapacity;
    private final int bowelCapacity;
    private final int solidsRate;
    private final int liquidsRate;
    private final int solidsFullAmount;
    private final int liquidsFullAmount;
    private final double bladderContinence;
    private final double bowelContinence;

    /**
     * Create a new update message from a metabolism
     *
     * @param metabolism The metabolism to create the message from
     * @see IMetabolism
     */
    public MetabolismUpdateMessage(@NotNull IMetabolism metabolism) {
        this.liquids = metabolism.getLiquids();
        this.solids = metabolism.getSolids();
        this.bladder = metabolism.getBladder();
        this.bowels = metabolism.getBowels();
        this.bladderCapacity = metabolism.getBladderCapacity();
        this.bowelCapacity = metabolism.getBowelCapacity();
        this.solidsRate = metabolism.getSolidsRate();
        this.liquidsRate = metabolism.getLiquidsRate();
        this.solidsFullAmount = metabolism.getMaxSolids();
        this.liquidsFullAmount = metabolism.getMaxLiquids();
        this.bladderContinence = metabolism.getBladderContinence();
        this.bowelContinence = metabolism.getBowelContinence();
    }

    /**
     * Create a new update message from a buffer
     *
     * @param buffer The buffer to create the message from
     * @see FriendlyByteBuf
     */
    public MetabolismUpdateMessage(@NotNull FriendlyByteBuf buffer) {
        this.liquids = buffer.readInt();
        this.solids = buffer.readInt();
        this.bladder = buffer.readInt();
        this.bowels = buffer.readInt();
        this.bladderCapacity = buffer.readInt();
        this.bowelCapacity = buffer.readInt();
        this.solidsRate = buffer.readInt();
        this.liquidsRate = buffer.readInt();
        this.solidsFullAmount = buffer.readInt();
        this.liquidsFullAmount = buffer.readInt();
        this.bladderContinence = buffer.readDouble();
        this.bowelContinence = buffer.readDouble();
    }

    /**
     * Decode a message from a buffer
     *
     * @param buffer The buffer to decode the message from
     * @return The decoded message
     */
    @Contract("_ -> new")
    public static @NotNull MetabolismUpdateMessage decoder(FriendlyByteBuf buffer) {
        return new MetabolismUpdateMessage(buffer);
    }

    /**
     * Encode a message to a buffer
     *
     * @param buffer The buffer to encode the message to
     * @implSpec The order of the encoded values must match the order of the decoded values found in the constructor
     */
    public void encoder(@NotNull FriendlyByteBuf buffer) {
        buffer.writeInt(liquids);
        buffer.writeInt(solids);
        buffer.writeInt(bladder);
        buffer.writeInt(bowels);
        buffer.writeInt(bladderCapacity);
        buffer.writeInt(bowelCapacity);
        buffer.writeInt(solidsRate);
        buffer.writeInt(liquidsRate);
        buffer.writeInt(solidsFullAmount);
        buffer.writeInt(liquidsFullAmount);
        buffer.writeDouble(bladderContinence);
        buffer.writeDouble(bowelContinence);
    }

    /**
     * Consume a message and update the player's metabolism capability
     *
     * @param ctx The context of the message
     */
    public void messageConsumer(@NotNull Supplier<NetworkEvent.Context> ctx) {
        Player player = ctx.get().getSender();
        if (player == null) {
            player = DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHooks::getPlayer);
        }
        if (player == null) {
            LOGGER.warn("Failed to update metabolism of player");
            ctx.get().setPacketHandled(false);
            return;
        }

        IMetabolism metabolism = player.getCapability(MetabolismCapabilities.METABOLISM).orElseThrow(() ->
                new IllegalStateException("Player does not have a metabolism capability"));
        metabolism.setLiquids(liquids);
        metabolism.setSolids(solids);
        metabolism.setBladder(bladder);
        metabolism.setBowels(bowels);
        metabolism.setBladderCapacity(bladderCapacity);
        metabolism.setBowelCapacity(bowelCapacity);
        metabolism.setSolidsRate(solidsRate);
        metabolism.setLiquidsRate(liquidsRate);
        metabolism.setMaxSolids(solidsFullAmount);
        metabolism.setMaxLiquids(liquidsFullAmount);
        metabolism.setBladderContinence(bladderContinence);
        metabolism.setBowelContinence(bowelContinence);
        ctx.get().setPacketHandled(true);
        LOGGER.debug("Updated metabolism of player {}: {}", player.getDisplayName().getString(), metabolism);
    }
}