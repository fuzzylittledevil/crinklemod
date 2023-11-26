package ninja.crinkle.mod.events.handlers;

import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.commands.MetabolismCommand;
import ninja.crinkle.mod.items.CrinkleItems;
import ninja.crinkle.mod.metabolism.Metabolism;
import ninja.crinkle.mod.undergarment.Undergarment;
import ninja.crinkle.mod.util.ClientUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = CrinkleMod.MODID)
public class CrinkleForgeBusEvents {
    private static final Logger LOGGER = LogUtils.getLogger();
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getLevel().getBlockState(event.getPos());
        Block block = state.getBlock();
        if (block.equals(Blocks.CAULDRON) && Undergarment.hasUndergarmentData(event.getItemStack()) && event.getSide() == LogicalSide.CLIENT) {
            event.getEntity().sendSystemMessage(
                    Component.translatable("event.crinklemod.undergarment.use.cauldron.no_water.text",
                            event.getItemStack().getHoverName()));
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
        if (block.equals(Blocks.WATER_CAULDRON)) {
            Undergarment undergarment = Undergarment.of(event.getItemStack());
            if(Undergarment.hasUndergarmentData(event.getItemStack())) {
                if (undergarment.getLiquids() > 0 || undergarment.getSolids() > 0) {
                    int amount = (undergarment.getLiquids() + undergarment.getSolids()) / 2;
                    undergarment.setLiquids(0);
                    undergarment.setSolids(0);
                    int numberToSpawn = Math.max(amount / 250, 1);
                    for (int i = 0; i < numberToSpawn; i++) {
                        event.getEntity().spawnAtLocation(new ItemStack(Items.CLAY_BALL));
                    }
                    if (event.getSide() == LogicalSide.CLIENT) {
                        event.getEntity().sendSystemMessage(
                                Component.translatable("event.crinklemod.undergarment.use.cauldron.text",
                                        event.getItemStack().getHoverName()));
                    }
                    LayeredCauldronBlock.lowerFillLevel(state, event.getLevel(), event.getPos());
                } else {
                    if (event.getSide() == LogicalSide.CLIENT) {
                        event.getEntity().sendSystemMessage(
                                Component.translatable("event.crinklemod.undergarment.use.cauldron.nothing.text",
                                        event.getItemStack().getHoverName()));
                    }
                }
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    @SubscribeEvent
    public static void registerTooltips(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() != CrinkleItems.DIAPER.get()) {
            return;
        }
        Undergarment undergarment = Undergarment.of(stack);
        if (undergarment.getLiquids() > 0 || undergarment.getSolids() > 0) {
            event.getTooltipElements().add(Either.right(undergarment.getLiquidsTooltip()));
            event.getTooltipElements().add(Either.right(undergarment.getSolidsTooltip()));
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Metabolism.of(player).syncClient();
        }
    }

    /**
     * Hook on when commands are registered. This is used to register the metabolism command.
     *
     * @param event The event
     * @see RegisterCommandsEvent
     */
    @SubscribeEvent
    public static void onCommandsRegister(@NotNull RegisterCommandsEvent event) {
        new MetabolismCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    /**
     * Hook on when a living entity uses an item. This is used to consume items that are used as food or drink.
     *
     * @param event The event
     * @see LivingEntityUseItemEvent.Finish
     */
    @SubscribeEvent
    public static void onLivingEntityUseItem(LivingEntityUseItemEvent.@NotNull Finish event) {
        if (event.getEntity().level().isClientSide) return;
        if (event.getEntity() instanceof Player player)
            Metabolism.of(player).consume(event.getItem());
    }

    @SubscribeEvent
    public static void onMousePressedPre(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        Metabolism metabolism = Metabolism.of(player);
        if (stack.isEdible() && !player.getFoodData().needsFood()) {
            if (metabolism.getStomachFullness() > 0.9f) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.FAIL);
                if (event.getSide() == LogicalSide.CLIENT)
                    player.sendSystemMessage(Component.translatable("action.crinklemod.metabolism.stomach.full.text"));
                return;
            }
            player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 1);
        }

    }

    /**
     * Hook on when a player ticks. This is used to tick the metabolism of a player.
     * It only ticks the metabolism every 100 ticks, or 5 seconds.
     *
     * @param event The event
     * @see TickEvent.PlayerTickEvent
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.@NotNull PlayerTickEvent event) {
        if (event.side.isClient()) return;
        if (event.phase == TickEvent.Phase.END) {
            Metabolism.of(event.player).tick(event.player.tickCount);
        }
    }
}
