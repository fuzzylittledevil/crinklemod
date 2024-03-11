package ninja.crinkle.mod.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class CrinkleEvent extends Event {
    public enum Side {
        CLIENT,
        SERVER
    }

    private final Player player;
    private final Side side;

    public CrinkleEvent(Side side, Player player) {
        this.side = side;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Side getSide() {
        return side;
    }
}
