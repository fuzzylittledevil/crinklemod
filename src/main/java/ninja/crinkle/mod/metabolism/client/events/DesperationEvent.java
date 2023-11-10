package ninja.crinkle.mod.metabolism.client.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import ninja.crinkle.mod.metabolism.common.capabilities.MetabolismImpl;

/**
 * A desperation event that is fired when a player's desperation changes.
 * This event is fired on a metabolism tick if a player's desperation changes.
 * @see net.minecraftforge.eventbus.api.Event
 * @see MetabolismImpl
 */
public abstract class DesperationEvent extends Event {
    private final Player player;
    private final double desperation;

    public DesperationEvent(Player player, double desperation) {
        this.player = player;
        this.desperation = desperation;
    }

    /**
     * Get the player that had their desperation changed
     * @return The player that had their desperation changed
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the player's desperation
     * @return The player's desperation
     */
    public double getDesperation() {
        return desperation;
    }

    public static class Bladder extends DesperationEvent {
        public Bladder(Player player, double desperation) {
            super(player, desperation);
        }
    }

    public static class Bowels extends DesperationEvent {
        public Bowels(Player player, double desperation) {
            super(player, desperation);
        }
    }
}
