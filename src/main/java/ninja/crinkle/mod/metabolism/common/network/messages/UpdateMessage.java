package ninja.crinkle.mod.metabolism.common.network.messages;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import ninja.crinkle.mod.metabolism.common.capabilities.Capabilities;
import ninja.crinkle.mod.metabolism.common.capabilities.IMetabolism;
import ninja.crinkle.mod.metabolism.common.capabilities.MetabolismImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * A message that is used to sync the metabolism of a player to the client or server.
 * @see MetabolismImpl
 */
public class UpdateMessage {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final double liquids;
    private final double solids;
    private final double bladder;
    private final double bowels;
    private final double bladderDesperation;
    private final double bowelDesperation;
    private final double bladderCapacity;
    private final double bowelCapacity;
    private final double solidsRate;
    private final double liquidsRate;
    private final double solidsFullAmount;
    private final double liquidsFullAmount;
    private final double bladderContinence;
    private final double bowelContinence;

    /**
     * Create a new update message from a metabolism
     * @see IMetabolism
     * @param metabolism The metabolism to create the message from
     */
    public UpdateMessage(@NotNull IMetabolism metabolism) {
        this.liquids = metabolism.getLiquids();
        this.solids = metabolism.getSolids();
        this.bladder = metabolism.getBladder();
        this.bowels = metabolism.getBowels();
        this.bladderDesperation = metabolism.getBladderDesperation();
        this.bowelDesperation = metabolism.getBowelDesperation();
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
     * @see FriendlyByteBuf
     * @param buffer The buffer to create the message from
     */
    public UpdateMessage(@NotNull FriendlyByteBuf buffer) {
        this.liquids = buffer.readDouble();
        this.solids = buffer.readDouble();
        this.bladder = buffer.readDouble();
        this.bowels = buffer.readDouble();
        this.bladderDesperation = buffer.readDouble();
        this.bowelDesperation = buffer.readDouble();
        this.bladderCapacity = buffer.readDouble();
        this.bowelCapacity = buffer.readDouble();
        this.solidsRate = buffer.readDouble();
        this.liquidsRate = buffer.readDouble();
        this.solidsFullAmount = buffer.readDouble();
        this.liquidsFullAmount = buffer.readDouble();
        this.bladderContinence = buffer.readDouble();
        this.bowelContinence = buffer.readDouble();
    }

    /**
     * Decode a message from a buffer
     * @param buffer The buffer to decode the message from
     * @return The decoded message
     */
    @Contract("_ -> new")
    public static @NotNull UpdateMessage decoder(FriendlyByteBuf buffer) {
        return new UpdateMessage(buffer);
    }

    /**
     * Encode a message to a buffer
     * @implSpec The order of the encoded values must match the order of the decoded values found in the constructor
     * @param buffer The buffer to encode the message to
     */
    public void encoder(@NotNull FriendlyByteBuf buffer) {
        buffer.writeDouble(liquids);
        buffer.writeDouble(solids);
        buffer.writeDouble(bladder);
        buffer.writeDouble(bowels);
        buffer.writeDouble(bladderDesperation);
        buffer.writeDouble(bowelDesperation);
        buffer.writeDouble(bladderCapacity);
        buffer.writeDouble(bowelCapacity);
        buffer.writeDouble(solidsRate);
        buffer.writeDouble(liquidsRate);
        buffer.writeDouble(solidsFullAmount);
        buffer.writeDouble(liquidsFullAmount);
        buffer.writeDouble(bladderContinence);
        buffer.writeDouble(bowelContinence);
    }

    /**
     * Consume a message and update the player's metabolism capability
     * @param ctx The context of the message
     */
    public void messageConsumer(@NotNull Supplier<NetworkEvent.Context> ctx) {
        Player player = Optional.ofNullable((Player) ctx.get().getSender()).orElse(Minecraft.getInstance().player);
        Optional.ofNullable(player).ifPresent(p -> {
                    p.getCapability(Capabilities.METABOLISM).ifPresent(m -> {
                        m.setLiquids(liquids);
                        m.setSolids(solids);
                        m.setBladder(bladder);
                        m.setBowels(bowels);
                        m.setBladderDesperation(bladderDesperation);
                        m.setBowelDesperation(bowelDesperation);
                        m.setBladderCapacity(bladderCapacity);
                        m.setBowelCapacity(bowelCapacity);
                        m.setSolidsRate(solidsRate);
                        m.setLiquidsRate(liquidsRate);
                        m.setMaxSolids(solidsFullAmount);
                        m.setMaxLiquids(liquidsFullAmount);
                        m.setBladderContinence(bladderContinence);
                        m.setBowelContinence(bowelContinence);
                        LOGGER.debug("Updated metabolism of player {}: {}", p.getDisplayName().getString(), m);
                    });
                }
        );
    }
}