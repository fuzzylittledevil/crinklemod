package ninja.crinkle.mod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import ninja.crinkle.mod.metabolism.Metabolism;
import ninja.crinkle.mod.metabolism.MetabolismSettings;
import ninja.crinkle.mod.settings.Setting;
import ninja.crinkle.mod.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.commands.Commands.*;
import static net.minecraft.commands.arguments.EntityArgument.getPlayer;
import static net.minecraft.commands.arguments.EntityArgument.player;

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
                        .then(command(MetabolismSettings.LIQUIDS))
                        .then(command(MetabolismSettings.SOLIDS))
                        .then(command(MetabolismSettings.BLADDER))
                        .then(command(MetabolismSettings.BOWELS))
                        .then(command(MetabolismSettings.MAX_LIQUIDS))
                        .then(command(MetabolismSettings.MAX_SOLIDS))
                        .then(command(MetabolismSettings.LIQUIDS_RATE))
                        .then(command(MetabolismSettings.SOLIDS_RATE))
                        .then(command(MetabolismSettings.BLADDER_CAPACITY))
                        .then(command(MetabolismSettings.BOWEL_CAPACITY))
                        .then(command(MetabolismSettings.BLADDER_CONTINENCE))
                        .then(command(MetabolismSettings.BOWEL_CONTINENCE))
                ));
        dispatcher.register(literal("met").redirect(node));
    }

    private <T extends Comparable<? super T>> int setValue(CommandSourceStack source, Setting<T> setting,
                                                           ServerPlayer instigator, @NotNull ServerPlayer target,
                                                           String value) {
        List<Component> errors = setting.errors(target, setting.valueOf(value));
        if (!errors.isEmpty()) {
            errors.forEach(source::sendFailure);
            return 0;
        }
        setting.set(target, value);
        Metabolism.of(target).syncClient();
        String command = StringUtil.snake(setting.key());
        source.sendSuccess(() -> instigator.equals(target) ?
                        Component.translatable("command.crinklemod.metabolism.set.self.success",
                                command, value) :
                        Component.translatable("command.crinklemod.metabolism.set.instigator.success",
                                target.getDisplayName(), command, value),
                true);

        return Command.SINGLE_SUCCESS;
    }

    private <T extends Comparable<? super T>> ArgumentBuilder<CommandSourceStack, ?> command(Setting<T> setting) {
        return literal(StringUtil.snake(setting.key()))
                .then(argument("target", player())
                        .then(argument("value", StringArgumentType.greedyString())
                                .executes(c -> setValue(c.getSource(), setting, c.getSource().getPlayerOrException(),
                                        getPlayer(c, "target"), StringArgumentType.getString(c, "value")))));
    }

    private int resetData(@NotNull CommandSourceStack source, ServerPlayer instigator, @NotNull ServerPlayer target) {
        Metabolism m = Metabolism.of(target);
        m.setBowels(0);
        m.setBladder(0);
        m.setSolids(0);
        m.setLiquids(0);
        m.syncClient();
        source.sendSuccess(() -> instigator.equals(target) ?
                        Component.translatable("command.crinklemod.metabolism.reset.self.success") :
                        Component.translatable("command.crinklemod.metabolism.reset.instigator.success", target.getDisplayName()),
                true);
        return Command.SINGLE_SUCCESS;
    }

    private int status(CommandSourceStack source, ServerPlayer instigator, @NotNull Player target) {
        Metabolism m = Metabolism.of(target);
        List<Component> components = new ArrayList<>();
        components.add(Component.literal(String.format("Liquids: %d / %d (Rate: %d)", m.getLiquids(),
                m.getMaxLiquids(), m.getLiquidsRate())));
        components.add(Component.literal(String.format("Solids: %d / %d (Rate: %d)", m.getSolids(),
                m.getMaxSolids(), m.getSolidsRate())));
        components.add(Component.literal(String.format("Liquids: %d / %d (Cont: %.4f)",
                m.getBladder(), m.getBladderCapacity(), m.getBladderContinence())));
        components.add(Component.literal(String.format("Bowels: %d / %d (Cont: %.4f)",
                m.getBowels(), m.getBowelCapacity(), m.getBowelContinence())));
        source.sendSuccess(() -> Component.literal(target.getDisplayName().getString()), false);
        components.forEach(instigator::sendSystemMessage);
        return Command.SINGLE_SUCCESS;
    }
}
