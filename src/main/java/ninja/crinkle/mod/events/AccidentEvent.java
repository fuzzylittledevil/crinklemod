package ninja.crinkle.mod.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import ninja.crinkle.mod.capabilities.MetabolismImpl;

/**
 * An accident event that is fired when a player has an accident.
 *
 * @author Galen
 * @see net.minecraftforge.eventbus.api.Event
 * @see MetabolismImpl
 */
public abstract class AccidentEvent extends Event {
    private final int amount;
    private final Player player;

    public AccidentEvent(Player player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    /**
     * Get the amount of liquids or solids that were lost
     *
     * @return The amount of liquids or solids that were lost
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Get the player that lost the liquids or solids
     *
     * @return The player that lost the liquids or solids
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * An accident event that is fired when a player has a bladder accident.
     */
    public static class Bladder extends AccidentEvent {
        public Bladder(Player player, int amount) {
            super(player, amount);
        }
    }

    /**
     * An accident event that is fired when a player has a bowel accident.
     */
    public static class Bowels extends AccidentEvent {
        public Bowels(Player player, int amount) {
            super(player, amount);
        }
    }

    @Override
    public String toString() {
        return "AccidentEvent{" +
                "amount=" + amount +
                ", player=" + player +
                '}';
    }
}
