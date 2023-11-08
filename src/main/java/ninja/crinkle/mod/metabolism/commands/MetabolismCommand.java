package ninja.crinkle.mod.metabolism.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import ninja.crinkle.mod.metabolism.capabilities.Metabolism;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.commands.Commands.*;
import static net.minecraft.commands.arguments.EntityArgument.getPlayer;
import static net.minecraft.commands.arguments.EntityArgument.player;

public class MetabolismCommand {
    public MetabolismCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
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
                        .then(literal("solids")
                                .then(argument("target", player())
                                        .then(argument("value", DoubleArgumentType.doubleArg(Metabolism.MIN_SOLIDS, Metabolism.MAX_SOLIDS))
                                                .executes(c -> setSolids(c.getSource(), c.getSource().getPlayerOrException(),
                                                        getPlayer(c, "target"), DoubleArgumentType.getDouble(c, "value"))))))
                        .then(literal("liquids")
                                .then(argument("target", player())
                                        .then(argument("value", DoubleArgumentType.doubleArg(Metabolism.MIN_SOLIDS, Metabolism.MAX_SOLIDS))
                                                .executes(c -> setLiquids(c.getSource(), c.getSource().getPlayerOrException(),
                                                        getPlayer(c, "target"), DoubleArgumentType.getDouble(c, "value"))))))
                ));
        dispatcher.register(literal("met").redirect(node));
    }

    private int resetData(CommandSourceStack source, ServerPlayer instigator, Player target) throws CommandSyntaxException {
        target.getCapability(Metabolism.INSTANCE).ifPresent(m -> m.reset(target));
        if (target instanceof ServerPlayer player && !player.isLocalPlayer() && !instigator.equals(target)) {
            player.sendSystemMessage(Component.translatable("command.crinklemod.metabolism.reset.target.success"));
        }
        source.sendSuccess(() -> instigator.equals(target) ?
                        Component.translatable("command.crinklemod.metabolism.reset.self.success") :
                        Component.translatable("command.crinklemod.metabolism.reset.instigator.success", target.getDisplayName()),
                true);
        return Command.SINGLE_SUCCESS;
    }

    private int setSolids(CommandSourceStack source, ServerPlayer instigator, Player target, double value) throws CommandSyntaxException {
        target.getCapability(Metabolism.INSTANCE).ifPresent(metabolism -> metabolism.setSolids(value));
        if (target instanceof ServerPlayer player && !player.isLocalPlayer() && !instigator.equals(target)) {
            player.sendSystemMessage(Component.translatable("command.crinklemod.metabolism.set.target.success"));
        }
        source.sendSuccess(() -> instigator.equals(target) ?
                        Component.translatable("command.crinklemod.metabolism.set.self.success", "solids", value) :
                        Component.translatable("command.crinklemod.metabolism.set.instigator.success", target.getDisplayName(), "solids", value),
                true);
        return Command.SINGLE_SUCCESS;
    }

    private int setLiquids(CommandSourceStack source, ServerPlayer instigator, Player target, double value) throws CommandSyntaxException {
        target.getCapability(Metabolism.INSTANCE).ifPresent(metabolism -> metabolism.setLiquids(value));
        if (target instanceof ServerPlayer player && !player.isLocalPlayer() && !instigator.equals(target)) {
            player.sendSystemMessage(Component.translatable("command.crinklemod.metabolism.set.target.success"));
        }
        source.sendSuccess(() -> instigator.equals(target) ?
                        Component.translatable("command.crinklemod.metabolism.set.self.success", "liquids", value) :
                        Component.translatable("command.crinklemod.metabolism.set.instigator.success", target.getDisplayName(), "liquids", value),
                true);
        return Command.SINGLE_SUCCESS;
    }

    private int status(CommandSourceStack source, ServerPlayer instigator, Player target) throws CommandSyntaxException {
        target.getCapability(Metabolism.INSTANCE).ifPresent(m -> {
            List<Component> components = new ArrayList<>();
            components.add(Component.literal(String.format("Solids: %.2f", m.getSolids())));
            components.add(Component.literal(String.format("Liquids: %.2f", m.getLiquids())));
            components.add(Component.literal(String.format("Bladder: %.2f", m.getBladder())));
            components.add(Component.literal(String.format("Bowels: %.2f", m.getBowels())));
            components.add(Component.literal(String.format("Bladder Desperation: %f", m.getBladderDesperation())));
            components.add(Component.literal(String.format("Bowels Desperation: %f", m.getBowelsDesperation())));
            source.sendSuccess(() -> Component.literal(target.getDisplayName().getString()), false);
            components.forEach(instigator::sendSystemMessage);
        });
        return Command.SINGLE_SUCCESS;
    }
}
