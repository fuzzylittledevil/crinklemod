package ninja.crinkle.mod.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import ninja.crinkle.mod.capabilities.MetabolismImpl;

/**
 * An accident event that is fired when a player has an accident.
 *
 * @author Galen
 * @see Event
 * @see MetabolismImpl
 */
public abstract class DesperationEvent extends CrinkleEvent {
    private final int level;

    public DesperationEvent(Player player, int level, Side side) {
        super(side, player);
        this.level = level;
    }

    /**
     * Get the amount of liquids or solids that were lost
     *
     * @return The amount of liquids or solids that were lost
     */
    public int getLevel() {
        return level;
    }

    /**
     * An accident event that is fired when a player has a bladder accident.
     */
    public static class Bladder extends DesperationEvent {
        public Bladder(Player player, int level, Side side) {
            super(player, level, side);
        }
    }

    /**
     * An accident event that is fired when a player has a bowel accident.
     */
    public static class Bowels extends DesperationEvent {
        public Bowels(Player player, int level, Side side) {
            super(player, level, side);
        }
    }

    @Override
    public String toString() {
        return "DesperationEvent{" +
                "side=" + getSide() +
                ", level=" + getLevel() +
                ", player=" + getPlayer() +
                '}';
    }
}
