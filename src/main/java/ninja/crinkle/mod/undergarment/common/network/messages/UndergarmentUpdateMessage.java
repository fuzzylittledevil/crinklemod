package ninja.crinkle.mod.undergarment.common.network.messages;

import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import ninja.crinkle.mod.undergarment.common.Undergarment;
import ninja.crinkle.mod.undergarment.common.capabilities.IUndergarment;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Supplier;

public class UndergarmentUpdateMessage {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final int liquids;
    private final int solids;
    private final int maxLiquids;
    private final int maxSolids;

    public UndergarmentUpdateMessage(@NotNull IUndergarment undergarment) {
        this.liquids = undergarment.getLiquids();
        this.solids = undergarment.getSolids();
        this.maxLiquids = undergarment.getMaxLiquids();
        this.maxSolids = undergarment.getMaxSolids();
    }

    public UndergarmentUpdateMessage(@NotNull FriendlyByteBuf buffer) {
        this.liquids = buffer.readInt();
        this.solids = buffer.readInt();
        this.maxLiquids = buffer.readInt();
        this.maxSolids = buffer.readInt();
    }

    public static @NotNull UndergarmentUpdateMessage decoder(@NotNull FriendlyByteBuf buffer) {
        return new UndergarmentUpdateMessage(buffer);
    }

    public void encoder(@NotNull FriendlyByteBuf buffer) {
        buffer.writeInt(liquids);
        buffer.writeInt(solids);
        buffer.writeInt(maxLiquids);
        buffer.writeInt(maxSolids);
    }

    public void messageConsumer(@NotNull Supplier<NetworkEvent.Context> ctx) {
        Optional.ofNullable(ctx.get().getSender())
                .flatMap(player -> {
                    LOGGER.debug("Received undergarment update message from {}", player.getName().getString());
                    return Undergarment.of(player).getUndergarment();
                })
                .ifPresent(u -> {
                    u.setLiquids(liquids);
                    u.setSolids(solids);
                    u.setMaxLiquids(maxLiquids);
                    u.setMaxSolids(maxSolids);
                });
    }

}
