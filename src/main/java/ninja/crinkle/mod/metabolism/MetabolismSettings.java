package ninja.crinkle.mod.metabolism;

import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.config.MetabolismConfig;
import ninja.crinkle.mod.settings.Setting;


public class MetabolismSettings {
    public static final Setting<Integer> LIQUIDS = Setting.intBuilder("liquids")
            .range(p -> 0, p -> Metabolism.of(p).getMaxLiquids())
            .label(Component.translatable("setting.crinklemod.metabolism.liquids.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.liquids.tooltip"))
            .defaultValue(0)
            .getter(p -> Metabolism.of(p).getLiquids())
            .setter((p, v) -> Metabolism.of(p).setLiquids(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> SOLIDS = Setting.intBuilder("solids")
            .range(p -> 0, p -> Metabolism.of(p).getMaxSolids())
            .label(Component.translatable("setting.crinklemod.metabolism.solids.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.solids.tooltip"))
            .defaultValue(0)
            .getter(p -> Metabolism.of(p).getSolids())
            .setter((p, v) -> Metabolism.of(p).setSolids(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> BLADDER = Setting.intBuilder("bladder")
            .range(p -> 0, p -> Metabolism.of(p).getBladderCapacity())
            .label(Component.translatable("setting.crinklemod.metabolism.bladder.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bladder.tooltip"))
            .defaultValue(0)
            .getter(p -> Metabolism.of(p).getBladder())
            .setter((p, v) -> Metabolism.of(p).setBladder(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> BOWELS = Setting.intBuilder("bowels")
            .range(p -> 0, p -> Metabolism.of(p).getBowelCapacity())
            .label(Component.translatable("setting.crinklemod.metabolism.bowels.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bowels.tooltip"))
            .defaultValue(0)
            .getter(p -> Metabolism.of(p).getBowels())
            .setter((p, v) -> Metabolism.of(p).setBowels(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> MAX_LIQUIDS = Setting.intBuilder("maxLiquids")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.max_liquids.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.max_liquids.tooltip"))
            .defaultValue(() -> MetabolismConfig.maxLiquids)
            .getter(p -> Metabolism.of(p).getMaxLiquids())
            .setter((p, v) -> Metabolism.of(p).setMaxLiquids(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> MAX_SOLIDS = Setting.intBuilder("maxSolids")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.max_solids.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.max_solids.tooltip"))
            .defaultValue(() -> MetabolismConfig.maxSolids)
            .getter(p -> Metabolism.of(p).getMaxSolids())
            .setter((p, v) -> Metabolism.of(p).setMaxSolids(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> BLADDER_CAPACITY = Setting.intBuilder("bladderCapacity")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.bladder_capacity.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bladder_capacity.tooltip"))
            .defaultValue(() -> MetabolismConfig.bladderCapacity)
            .getter(p -> Metabolism.of(p).getBladderCapacity())
            .setter((p, v) -> Metabolism.of(p).setBladderCapacity(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> BOWEL_CAPACITY = Setting.intBuilder("bowelCapacity")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.bowel_capacity.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bowel_capacity.tooltip"))
            .defaultValue(() -> MetabolismConfig.bowelCapacity)
            .getter(p -> Metabolism.of(p).getBowelCapacity())
            .setter((p, v) -> Metabolism.of(p).setBowelCapacity(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> LIQUIDS_RATE = Setting.intBuilder("liquidsRate")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.liquids_rate.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.liquids_rate.tooltip"))
            .defaultValue(() -> MetabolismConfig.liquidsRate)
            .getter(p -> Metabolism.of(p).getLiquidsRate())
            .setter((p, v) -> Metabolism.of(p).setLiquidsRate(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> SOLIDS_RATE = Setting.intBuilder("solidsRate")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.solids_rate.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.solids_rate.tooltip"))
            .defaultValue(() -> MetabolismConfig.solidsRate)
            .getter(p -> Metabolism.of(p).getSolidsRate())
            .setter((p, v) -> Metabolism.of(p).setSolidsRate(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Double> BLADDER_CONTINENCE = Setting.doubleBuilder("bladderContinence")
            .range(p -> 0.0, p -> 1.0)
            .label(Component.translatable("setting.crinklemod.metabolism.bladder_continence.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bladder_continence.tooltip"))
            .defaultValue(() -> MetabolismConfig.bladderContinence)
            .getter(p -> Metabolism.of(p).getBladderContinence())
            .setter((p, v) -> Metabolism.of(p).setBladderContinence(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Double> BOWEL_CONTINENCE = Setting.doubleBuilder("bowelContinence")
            .range(p -> 0.0, p -> 1.0)
            .label(Component.translatable("setting.crinklemod.metabolism.bowel_continence.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bowel_continence.tooltip"))
            .defaultValue(() -> MetabolismConfig.bowelContinence)
            .getter(p -> Metabolism.of(p).getBowelContinence())
            .setter((p, v) -> Metabolism.of(p).setBowelContinence(v))
            .synchronizer(Metabolism::of)
            .build();
}
