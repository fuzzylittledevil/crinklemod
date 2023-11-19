package ninja.crinkle.mod.misc.events;

import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.undergarment.common.Undergarment;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = CrinkleMod.MODID)
public class CrinkleMinecraftEvents {
    private static final Logger LOGGER = LogUtils.getLogger();
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockState state = event.getLevel().getBlockState(event.getPos());
        Block block = state.getBlock();
        if (block.equals(Blocks.CAULDRON) && Undergarment.of(event.getItemStack()).get().isPresent() && event.getSide() == LogicalSide.CLIENT) {
            event.getEntity().sendSystemMessage(
                    Component.translatable("event.crinklemod.undergarment.use.cauldron.no_water.text",
                            event.getItemStack().getHoverName()));
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
        if (block.equals(Blocks.WATER_CAULDRON)) {
            Undergarment undergarment = Undergarment.of(event.getItemStack());
            if(undergarment.get().isPresent()) {
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
        Undergarment undergarment = Undergarment.of(stack);
        if (undergarment.getLiquids() > 0 || undergarment.getSolids() > 0) {
            event.getTooltipElements().add(Either.right(undergarment.getLiquidsTooltip()));
            event.getTooltipElements().add(Either.right(undergarment.getSolidsTooltip()));
        }
    }
}
