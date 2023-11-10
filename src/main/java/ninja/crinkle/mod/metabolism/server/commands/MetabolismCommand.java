package ninja.crinkle.mod.metabolism.server.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import ninja.crinkle.mod.metabolism.common.capabilities.Capabilities;
import ninja.crinkle.mod.metabolism.common.capabilities.IMetabolism;
import ninja.crinkle.mod.metabolism.common.network.MetabolismChannel;
import ninja.crinkle.mod.metabolism.common.network.messages.UpdateMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static net.minecraft.commands.Commands.*;
import static net.minecraft.commands.arguments.EntityArgument.getPlayer;
import static net.minecraft.commands.arguments.EntityArgument.player;

@FunctionalInterface
interface SetFunction {
    void apply(IMetabolism metabolism, double value);
}

enum SetType {
    SOLIDS("solids", IMetabolism::setSolids, IMetabolism::getMaxSolids),
    LIQUIDS("liquids", IMetabolism::setLiquids, IMetabolism::getMaxLiquids),
    BLADDER("bladder", IMetabolism::setBladder, IMetabolism::getBladderCapacity),
    BOWELS("bowels", IMetabolism::setBowels, IMetabolism::getBowelCapacity),
    BLADDER_CAPACITY("bladder_capacity", IMetabolism::setBladderCapacity, m -> Double.MAX_VALUE),
    BOWELS_CAPACITY("bowel_capacity", IMetabolism::setBowelCapacity, m -> Double.MAX_VALUE),
    BLADDER_CONTINENCE("bladder_continence", IMetabolism::setBladderContinence, m -> 1.0),
    BOWEL_CONTINENCE("bowel_continence", IMetabolism::setBowelContinence, m -> 1.0);

    private final String name;
    private final SetFunction setter;
    private final Function<IMetabolism, Double> maxGetter;

    SetType(String name, SetFunction setter, Function<IMetabolism, Double> maxGetter) {
        this.name = name;
        this.setter = setter;
        this.maxGetter = maxGetter;
    }

    public ArgumentBuilder<CommandSourceStack, ?> getCommand() {
        return literal(name)
                .then(argument("target", player())
                        .then(argument("value", DoubleArgumentType.doubleArg(0, Double.MAX_VALUE))
                                .executes(c -> setValue(c.getSource(), c.getSource().getPlayerOrException(),
                                        getPlayer(c, "target"), DoubleArgumentType.getDouble(c, "value")))));
    }

    private boolean isInRange(double value, double max) {
        return value >= 0 && value <= max;
    }

    @Contract(value = "_, _ -> new", pure = true)
    private @NotNull Component getOutOfRangeMessage(double value, double max) {
        return Component.translatable("command.crinklemod.metabolism.set.instigator.failure.out_of_range", value, 0, max);
    }

    private int setValue(CommandSourceStack source, ServerPlayer instigator, @NotNull ServerPlayer target, double value) {
        IMetabolism metabolism = target.getCapability(Capabilities.METABOLISM)
                .orElseThrow(() -> new IllegalStateException("No metabolism for player " + target.getDisplayName().getString()));
        if (!isInRange(value, maxGetter.apply(metabolism))) {
            source.sendFailure(getOutOfRangeMessage(value, maxGetter.apply(metabolism)));
            return 0;
        }
        setter.apply(metabolism, value);
        MetabolismChannel.INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new UpdateMessage(metabolism));
        source.sendSuccess(() -> instigator.equals(target) ?
                        Component.translatable("command.crinklemod.metabolism.set.self.success", name, value) :
                        Component.translatable("command.crinklemod.metabolism.set.instigator.success", target.getDisplayName(), name, value),
                true);
        return Command.SINGLE_SUCCESS;
    }
}

public class MetabolismCommand {

    public MetabolismCommand(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> node = dispatcher.register(literal("metabolism")
                .then(literal("status")
                        .executes(c -> status(c.getSource(), c.getSource().getPlayerOrException(), c.getSource().getPlayerOrException()))
                        .then(argument("target", player())
                                .executes(c -> status(c.getSource(), c.getSource().getPlayerOrException(), getPlayer(c, "target")))))
                .then(literal("reset")
                        .requires(source -> source.hasPermission(LEVEL_GAMEMASTERS))
                        .executes(c -> resetData(c.getSource(), c.getSource().getPlayerOrException(), c.getSource().getPlayerOrException()))
                        .then(argument("target", player())
                                .executes(c -> resetData(c.getSource(), c.getSource().getPlayerOrException(), getPlayer(c, "target")))))
                .then(literal("set")
                        .requires(source -> source.hasPermission(LEVEL_GAMEMASTERS))
                        .then(SetType.SOLIDS.getCommand())
                        .then(SetType.LIQUIDS.getCommand())
                        .then(SetType.BLADDER.getCommand())
                        .then(SetType.BOWELS.getCommand())
                        .then(SetType.BLADDER_CAPACITY.getCommand())
                        .then(SetType.BOWELS_CAPACITY.getCommand())
                        .then(SetType.BLADDER_CONTINENCE.getCommand())
                        .then(SetType.BOWEL_CONTINENCE.getCommand())
                ));
        dispatcher.register(literal("met").redirect(node));
    }

    private int resetData(@NotNull CommandSourceStack source, ServerPlayer instigator, @NotNull ServerPlayer target) {
        target.getCapability(Capabilities.METABOLISM).ifPresent(m -> {
            m.setBowels(0);
            m.setBladder(0);
            m.setSolids(0);
            m.setLiquids(0);
            m.setBladderDesperation(0);
            m.setBowelDesperation(0);
            MetabolismChannel.INSTANCE.send(PacketDistributor.PLAYER.with(() -> target), new UpdateMessage(m));
        });
        source.sendSuccess(() -> instigator.equals(target) ?
                        Component.translatable("command.crinklemod.metabolism.reset.self.success") :
                        Component.translatable("command.crinklemod.metabolism.reset.instigator.success", target.getDisplayName()),
                true);
        return Command.SINGLE_SUCCESS;
    }

    private int status(CommandSourceStack source, ServerPlayer instigator, @NotNull Player target) {
        target.getCapability(Capabilities.METABOLISM).ifPresent(m -> {
            List<Component> components = new ArrayList<>();
            components.add(Component.literal(String.format("Solids: %.2f / %.2f", m.getSolids(), m.getMaxSolids())));
            components.add(Component.literal(String.format("Liquids: %.2f / %.2f", m.getLiquids(), m.getMaxLiquids())));
            components.add(Component.literal(String.format("Bladder: %.2f / %.2f", m.getBladder(), m.getBladderCapacity())));
            components.add(Component.literal(String.format("Bowels: %.2f / %.2f", m.getBowels(), m.getBowelCapacity())));
            components.add(Component.literal(String.format("Bladder Desperation: %.4f", m.getBladderDesperation())));
            components.add(Component.literal(String.format("Bowel Desperation: %.4f", m.getBowelDesperation())));
            components.add(Component.literal(String.format("Bladder Continence: %.4f", m.getBladderContinence())));
            components.add(Component.literal(String.format("Bowel Continence: %.4f", m.getBowelContinence())));
            source.sendSuccess(() -> Component.literal(target.getDisplayName().getString()), false);
            components.forEach(instigator::sendSystemMessage);
        });
        return Command.SINGLE_SUCCESS;
    }
}
