package ninja.crinkle.mod.metabolism.common;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import ninja.crinkle.mod.lib.common.settings.Setting;
import ninja.crinkle.mod.metabolism.common.config.MetabolismConfig;


public class MetabolismSettings {
    public static final Setting<Integer, Player> LIQUIDS = Setting.intBuilder("liquids", Player.class)
            .range(p -> 0, p -> Metabolism.of(p).getMaxLiquids())
            .label(Component.translatable("setting.crinklemod.metabolism.liquids.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.liquids.tooltip"))
            .defaultValue(0)
            .getter(p -> Metabolism.of(p).getLiquids())
            .setter((p, v) -> Metabolism.of(p).setLiquids(v))
            .build();

    public static final Setting<Integer, Player> SOLIDS = Setting.intBuilder("solids", Player.class)
            .range(p -> 0, p -> Metabolism.of(p).getMaxSolids())
            .label(Component.translatable("setting.crinklemod.metabolism.solids.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.solids.tooltip"))
            .defaultValue(0)
            .getter(p -> Metabolism.of(p).getSolids())
            .setter((p, v) -> Metabolism.of(p).setSolids(v))
            .build();

    public static final Setting<Integer, Player> BLADDER = Setting.intBuilder("bladder", Player.class)
            .range(p -> 0, p -> Metabolism.of(p).getBladderCapacity())
            .label(Component.translatable("setting.crinklemod.metabolism.bladder.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bladder.tooltip"))
            .defaultValue(0)
            .getter(p -> Metabolism.of(p).getBladder())
            .setter((p, v) -> Metabolism.of(p).setBladder(v))
            .build();

    public static final Setting<Integer, Player> BOWELS = Setting.intBuilder("bowels", Player.class)
            .range(p -> 0, p -> Metabolism.of(p).getBowelCapacity())
            .label(Component.translatable("setting.crinklemod.metabolism.bowels.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bowels.tooltip"))
            .defaultValue(0)
            .getter(p -> Metabolism.of(p).getBowels())
            .setter((p, v) -> Metabolism.of(p).setBowels(v))
            .build();

    public static final Setting<Integer, Player> MAX_LIQUIDS = Setting.intBuilder("maxLiquids", Player.class)
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.max_liquids.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.max_liquids.tooltip"))
            .defaultValue(() -> MetabolismConfig.maxLiquids)
            .getter(p -> Metabolism.of(p).getMaxLiquids())
            .setter((p, v) -> Metabolism.of(p).setMaxLiquids(v))
            .build();

    public static final Setting<Integer, Player> MAX_SOLIDS = Setting.intBuilder("maxSolids", Player.class)
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.max_solids.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.max_solids.tooltip"))
            .defaultValue(() -> MetabolismConfig.maxSolids)
            .getter(p -> Metabolism.of(p).getMaxSolids())
            .setter((p, v) -> Metabolism.of(p).setMaxSolids(v))
            .build();

    public static final Setting<Integer, Player> BLADDER_CAPACITY = Setting.intBuilder("bladderCapacity", Player.class)
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.bladder_capacity.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bladder_capacity.tooltip"))
            .defaultValue(() -> MetabolismConfig.bladderCapacity)
            .getter(p -> Metabolism.of(p).getBladderCapacity())
            .setter((p, v) -> Metabolism.of(p).setBladderCapacity(v))
            .build();

    public static final Setting<Integer, Player> BOWEL_CAPACITY = Setting.intBuilder("bowelCapacity", Player.class)
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.bowel_capacity.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bowel_capacity.tooltip"))
            .defaultValue(() -> MetabolismConfig.bowelCapacity)
            .getter(p -> Metabolism.of(p).getBowelCapacity())
            .setter((p, v) -> Metabolism.of(p).setBowelCapacity(v))
            .build();

    public static final Setting<Integer, Player> LIQUIDS_RATE = Setting.intBuilder("liquidsRate", Player.class)
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.liquids_rate.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.liquids_rate.tooltip"))
            .defaultValue(() -> MetabolismConfig.liquidsRate)
            .getter(p -> Metabolism.of(p).getLiquidsRate())
            .setter((p, v) -> Metabolism.of(p).setLiquidsRate(v))
            .build();

    public static final Setting<Integer, Player> SOLIDS_RATE = Setting.intBuilder("solidsRate", Player.class)
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.solids_rate.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.solids_rate.tooltip"))
            .defaultValue(() -> MetabolismConfig.solidsRate)
            .getter(p -> Metabolism.of(p).getSolidsRate())
            .setter((p, v) -> Metabolism.of(p).setSolidsRate(v))
            .build();

    public static final Setting<Double, Player> BLADDER_CONTINENCE = Setting.doubleBuilder("bladderContinence", Player.class)
            .range(p -> 0.0, p -> 1.0)
            .label(Component.translatable("setting.crinklemod.metabolism.bladder_continence.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bladder_continence.tooltip"))
            .defaultValue(() -> MetabolismConfig.bladderContinence)
            .getter(p -> Metabolism.of(p).getBladderContinence())
            .setter((p, v) -> Metabolism.of(p).setBladderContinence(v))
            .build();

    public static final Setting<Double, Player> BOWEL_CONTINENCE = Setting.doubleBuilder("bowelContinence", Player.class)
            .range(p -> 0.0, p -> 1.0)
            .label(Component.translatable("setting.crinklemod.metabolism.bowel_continence.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bowel_continence.tooltip"))
            .defaultValue(() -> MetabolismConfig.bowelContinence)
            .getter(p -> Metabolism.of(p).getBowelContinence())
            .setter((p, v) -> Metabolism.of(p).setBowelContinence(v))
            .build();
}
