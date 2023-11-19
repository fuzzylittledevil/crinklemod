package ninja.crinkle.mod.lib.client.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import ninja.crinkle.mod.undergarment.common.Undergarment;

public abstract class UndergarmentInteractEvent extends Event {
    private final Player player;
    private final ItemStack itemStack;
    private final Undergarment undergarment;

    public UndergarmentInteractEvent(Player player, ItemStack itemStack, Undergarment undergarment) {
        super();
        this.player = player;
        this.itemStack = itemStack;
        this.undergarment = undergarment;
    }

    public Player getPlayer() {
        return player;
    }

    public Undergarment getUndergarment() {
        return undergarment;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public static class Washed extends UndergarmentInteractEvent {
        private final int liquidsAmount;
        private final int solidsAmount;
        public Washed(Player player, ItemStack itemStack, Undergarment undergarment, int liquidsAmount, int solidsAmount) {
            super(player, itemStack, undergarment);
            this.liquidsAmount = liquidsAmount;
            this.solidsAmount = solidsAmount;
        }

        public int getLiquidsAmount() {
            return liquidsAmount;
        }

        public int getSolidsAmount() {
            return solidsAmount;
        }
    }
}
