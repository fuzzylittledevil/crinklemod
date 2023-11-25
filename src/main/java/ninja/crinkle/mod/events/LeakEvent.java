package ninja.crinkle.mod.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class LeakEvent extends AccidentEvent {
    private final ItemStack itemStack;

    public LeakEvent(Player player, int amount, ItemStack itemStack, Side side) {
        super(player, amount, side);
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public static class Liquids extends LeakEvent {
        public Liquids(Player player, int amount, ItemStack itemStack, Side side) {
            super(player, amount, itemStack, side);
        }
    }

    public static class Solids extends LeakEvent {
        public Solids(Player player, int amount, ItemStack itemStack, Side side) {
            super(player, amount, itemStack, side);
        }
    }
}
