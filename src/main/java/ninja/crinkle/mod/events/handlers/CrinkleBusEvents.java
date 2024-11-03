package ninja.crinkle.mod.events.handlers;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.client.animations.Animation;
import ninja.crinkle.mod.client.animations.AnimationController;
import ninja.crinkle.mod.client.animations.BubbleSpriteGroup;
import ninja.crinkle.mod.client.animations.CharacterSpriteGroup;
import ninja.crinkle.mod.events.AccidentEvent;
import ninja.crinkle.mod.events.CrinkleEvent;
import ninja.crinkle.mod.events.DesperationEvent;
import ninja.crinkle.mod.events.LeakEvent;
import ninja.crinkle.mod.metabolism.Metabolism;
import ninja.crinkle.mod.network.CrinkleChannel;
import ninja.crinkle.mod.network.messages.AccidentEventMessage;
import ninja.crinkle.mod.undergarment.Undergarment;
import org.slf4j.Logger;


public class CrinkleBusEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public void propagateAccident(AccidentEvent event) {
        CrinkleEvent.Side currentSide = event.getPlayer() instanceof ServerPlayer ?
                CrinkleEvent.Side.SERVER : CrinkleEvent.Side.CLIENT;
        if (currentSide != event.getSide()) {
            return;
        }
        switch (event.getSide()) {
            case CLIENT -> CrinkleChannel.INSTANCE.sendToServer(
                    new AccidentEventMessage(event.getType(), event.getAmount()));
            case SERVER -> CrinkleChannel.INSTANCE.send(PacketDistributor.PLAYER.with(
                            () -> (ServerPlayer) event.getPlayer()),
                    new AccidentEventMessage(event.getType(), event.getAmount()));
        }
    }

    @SubscribeEvent
    public void onDesperation(DesperationEvent event) {
        if (event.getPlayer() instanceof ServerPlayer) {
            event.getPlayer().sendSystemMessage(Component.translatable("event.crinklemod.undergarment."
                    + event.getType() + ".desperation.text." + event.getLevel().getLevel())
                    .withStyle(event.getType().getStyle()));
        }
    }

    private void handleServerLeak(AccidentEvent event, Undergarment undergarment) {
        CrinkleEvent.Type type = switch(event.getType()) {
            case BLADDER -> CrinkleEvent.Type.LIQUIDS;
            case BOWEL -> CrinkleEvent.Type.SOLIDS;
            case BOTH -> CrinkleEvent.Type.BOTH;
            default -> throw new IllegalStateException("Unexpected value: " + event.getType());
        };
        CrinkleMod.EVENT_BUS.post(new LeakEvent(event.getPlayer(), event.getAmount(), undergarment.getItemStack(),
                AccidentEvent.Side.SERVER, type));
    }

    private void handleBladderServerAccident(AccidentEvent event, Undergarment undergarment) {

        if (event.getAmount() + undergarment.getLiquids() > undergarment.getMaxLiquids()) {
            undergarment.setLiquids(undergarment.getMaxLiquids());
        } else {
            undergarment.modifyLiquids(event.getAmount());
        }
        Metabolism.of(event.getPlayer()).setNumberOneRolls(0);
    }

    private void handleBowelsServerAccident(AccidentEvent event, Undergarment undergarment) {
        if (event.getAmount() + undergarment.getSolids() > undergarment.getMaxSolids()) {
            undergarment.setSolids(undergarment.getMaxSolids());
        } else {
            undergarment.modifySolids(event.getAmount());
        }
        Metabolism.of(event.getPlayer()).setNumberTwoRolls(0);
    }

    @SubscribeEvent
    public void onServerAccident(AccidentEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer)) return;
        ItemStack itemStack = Undergarment.getWornUndergarment(event.getPlayer());
        if (itemStack.isEmpty()) return; //TODO: handle accidents without undergarments
        event.getPlayer().sendSystemMessage(Component.translatable("event.crinklemod.undergarment."
                        + event.getType() + ".accident.text")
                .withStyle(event.getType().getStyle()));
        Undergarment undergarment = Undergarment.of(itemStack);
        LOGGER.debug("Player {} had a {} accident with {} amount", event.getPlayer().getName().getString(),
                event.getType(), event.getAmount());
        switch (event.getType()) {
            case BLADDER -> handleBladderServerAccident(event, undergarment);
            case BOWEL -> handleBowelsServerAccident(event, undergarment);
            case BOTH -> {
                handleBladderServerAccident(event, undergarment);
                handleBowelsServerAccident(event, undergarment);
            }
        }
        if (undergarment.isLeaking())
            handleServerLeak(event, undergarment);
    }

    @SubscribeEvent
    public void onClientAccident(AccidentEvent event) {
        if (event.getPlayer() instanceof ServerPlayer) return;
        Metabolism metabolism = Metabolism.of(event.getPlayer());
        BubbleSpriteGroup bubbleSpriteGroup = switch(event.getType()) {
            case BLADDER -> BubbleSpriteGroup.WET;
            case BOWEL -> BubbleSpriteGroup.MESSY;
            case BOTH -> BubbleSpriteGroup.BOTH;
            default -> throw new IllegalStateException("Unexpected value: " + event.getType());
        };
        SoundEvent soundEvent = switch(event.getType()) {
            case BLADDER -> SoundEvents.BOTTLE_EMPTY;
            case BOWEL -> SoundEvents.SLIME_BLOCK_STEP;
            case BOTH -> SoundEvents.BUCKET_EMPTY;
            default -> throw new IllegalStateException("Unexpected value: " + event.getType());
        };
        event.getPlayer().playSound(soundEvent, 0.5F, (float)(0.5 + Math.random()));
        AnimationController.INSTANCE.queuePriorityAnimation(Animation.builder()
                .addSpriteGroups(
                        CharacterSpriteGroup.ACCIDENT,
                        bubbleSpriteGroup)
                .position(metabolism.getIndicatorPositionX(), metabolism.getIndicatorPositionY())
                .speed(6.0)
                .build());
        AnimationController.INSTANCE.queuePriorityAnimation(Animation.builder()
                .addSpriteGroups(
                        CharacterSpriteGroup.RELIEF,
                        BubbleSpriteGroup.NORMAL)
                .speed(0.75)
                .position(metabolism.getIndicatorPositionX(), metabolism.getIndicatorPositionY())
                .build());
    }

    @SubscribeEvent
    public void onLeak(LeakEvent event) {
        event.getPlayer().sendSystemMessage(Component.translatable("event.crinklemod.undergarment."
                        + event.getType() + ".leak.text")
                .withStyle(event.getType().getStyle()));
    }
}
