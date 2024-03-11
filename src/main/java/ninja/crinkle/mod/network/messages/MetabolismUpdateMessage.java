package ninja.crinkle.mod.network.messages;

import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import ninja.crinkle.mod.capabilities.IMetabolism;
import ninja.crinkle.mod.capabilities.MetabolismCapabilities;
import ninja.crinkle.mod.capabilities.MetabolismImpl;
import ninja.crinkle.mod.util.ClientUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.function.Supplier;

/**
 * A message that is used to sync the metabolism of a player to the client or server.
 *
 * @see MetabolismImpl
 */
public class MetabolismUpdateMessage {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final int timer;
    private final int rolls;
    private final int safeRolls;
    private final double numberOneChance;
    private final double numberTwoChance;

    /**
     * Create a new update message from a metabolism
     *
     * @param metabolism The metabolism to create the message from
     * @see IMetabolism
     */
    public MetabolismUpdateMessage(@NotNull IMetabolism metabolism) {
        this.timer = metabolism.getTimer();
        this.rolls = metabolism.getNumberOneRolls();
        this.safeRolls = metabolism.getNumberOneSafeRolls();
        this.numberOneChance = metabolism.getNumberOneChance();
        this.numberTwoChance = metabolism.getNumberTwoChance();
    }

    /**
     * Create a new update message from a buffer
     *
     * @param buffer The buffer to create the message from
     * @see FriendlyByteBuf
     */
    public MetabolismUpdateMessage(@NotNull FriendlyByteBuf buffer) {
        this.timer = buffer.readInt();
        this.rolls = buffer.readInt();
        this.safeRolls = buffer.readInt();
        this.numberOneChance = buffer.readDouble();
        this.numberTwoChance = buffer.readDouble();
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
        buffer.writeInt(timer);
        buffer.writeInt(rolls);
        buffer.writeInt(safeRolls);
        buffer.writeDouble(numberOneChance);
        buffer.writeDouble(numberTwoChance);
    }

    /**
     * Consume a message and update the player's metabolism capability
     *
     * @param ctx The context of the message
     */
    public void messageConsumer(@NotNull Supplier<NetworkEvent.Context> ctx) {
        Player player = ctx.get().getSender() != null ? ctx.get().getSender() : ClientUtil.getPlayer();
        if (player == null) {
            LOGGER.warn("Failed to update metabolism of player");
            ctx.get().setPacketHandled(false);
            return;
        }

        IMetabolism metabolism = player.getCapability(MetabolismCapabilities.METABOLISM).orElseThrow(() ->
                new IllegalStateException("Player does not have a metabolism capability"));
        metabolism.setTimer(timer);
        metabolism.setNumberOneRolls(rolls);
        metabolism.setNumberOneSafeRolls(safeRolls);
        metabolism.setNumberOneChance(numberOneChance);
        metabolism.setNumberTwoChance(numberTwoChance);
        ctx.get().setPacketHandled(true);
    }
}