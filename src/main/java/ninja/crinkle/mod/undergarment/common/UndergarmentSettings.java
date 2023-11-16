package ninja.crinkle.mod.undergarment.common;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import ninja.crinkle.mod.lib.common.settings.Setting;

public class UndergarmentSettings {
    public static final Setting<Integer, Player> LIQUIDS = Setting.intBuilder("liquids", Player.class)
            .range(p -> 0, p -> Undergarment.of(p).getMaxLiquids())
            .label(Component.translatable("setting.crinklemod.undergarment.liquids.label"))
            .tooltip(Component.translatable("setting.crinklemod.undergarment.liquids.tooltip"))
            .defaultValue(0)
            .getter(p -> Undergarment.of(p).getLiquids())
            .setter((p, v) -> Undergarment.of(p).setLiquids(v))
            .build();

    public static final Setting<Integer, Player> SOLIDS = Setting.intBuilder("solids", Player.class)
            .range(p -> 0, p -> Undergarment.of(p).getMaxSolids())
            .label(Component.translatable("setting.crinklemod.undergarment.solids.label"))
            .tooltip(Component.translatable("setting.crinklemod.undergarment.solids.tooltip"))
            .defaultValue(0)
            .getter(p -> Undergarment.of(p).getSolids())
            .setter((p, v) -> Undergarment.of(p).setSolids(v))
            .build();

    public static final Setting<Integer, Player> MAX_LIQUIDS = Setting.intBuilder("maxLiquids", Player.class)
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.undergarment.max_liquids.label"))
            .tooltip(Component.translatable("setting.crinklemod.undergarment.max_liquids.tooltip"))
            .defaultValue(0)
            .getter(p -> Undergarment.of(p).getMaxLiquids())
            .setter((p, v) -> Undergarment.of(p).setMaxLiquids(v))
            .build();

    public static final Setting<Integer, Player> MAX_SOLIDS = Setting.intBuilder("maxSolids", Player.class)
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.undergarment.max_solids.label"))
            .tooltip(Component.translatable("setting.crinklemod.undergarment.max_solids.tooltip"))
            .defaultValue(0)
            .getter(p -> Undergarment.of(p).getMaxSolids())
            .setter((p, v) -> Undergarment.of(p).setMaxSolids(v))
            .build();
}
