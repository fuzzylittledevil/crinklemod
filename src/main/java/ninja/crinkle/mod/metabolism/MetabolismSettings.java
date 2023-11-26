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
            .defaultValue(p -> MetabolismConfig.maxLiquids)
            .getter(p -> Metabolism.of(p).getMaxLiquids())
            .setter((p, v) -> Metabolism.of(p).setMaxLiquids(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> MAX_SOLIDS = Setting.intBuilder("maxSolids")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.max_solids.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.max_solids.tooltip"))
            .defaultValue(p -> MetabolismConfig.maxSolids)
            .getter(p -> Metabolism.of(p).getMaxSolids())
            .setter((p, v) -> Metabolism.of(p).setMaxSolids(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> BLADDER_CAPACITY = Setting.intBuilder("bladderCapacity")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.bladder_capacity.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bladder_capacity.tooltip"))
            .defaultValue(p -> MetabolismConfig.bladderCapacity)
            .getter(p -> Metabolism.of(p).getBladderCapacity())
            .setter((p, v) -> Metabolism.of(p).setBladderCapacity(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> BOWEL_CAPACITY = Setting.intBuilder("bowelCapacity")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.bowel_capacity.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bowel_capacity.tooltip"))
            .defaultValue(p -> MetabolismConfig.bowelCapacity)
            .getter(p -> Metabolism.of(p).getBowelCapacity())
            .setter((p, v) -> Metabolism.of(p).setBowelCapacity(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> LIQUIDS_RATE = Setting.intBuilder("liquidsRate")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.liquids_rate.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.liquids_rate.tooltip"))
            .defaultValue(p -> MetabolismConfig.liquidsRate)
            .getter(p -> Metabolism.of(p).getLiquidsRate())
            .setter((p, v) -> Metabolism.of(p).setLiquidsRate(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> SOLIDS_RATE = Setting.intBuilder("solidsRate")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.solids_rate.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.solids_rate.tooltip"))
            .defaultValue(p -> MetabolismConfig.solidsRate)
            .getter(p -> Metabolism.of(p).getSolidsRate())
            .setter((p, v) -> Metabolism.of(p).setSolidsRate(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Double> BLADDER_ACCIDENT_WARNING = Setting.doubleBuilder("bladderAccidentWarning")
            .range(p -> 0.0, p -> 1.0)
            .label(Component.translatable("setting.crinklemod.metabolism.bladder_accident_warning.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bladder_accident_warning.tooltip"))
            .defaultValue(p -> MetabolismConfig.bladderAccidentWarning)
            .getter(p -> Metabolism.of(p).getBladderAccidentWarning())
            .setter((p, v) -> Metabolism.of(p).setBladderAccidentWarning(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Double> BOWEL_ACCIDENT_WARNING = Setting.doubleBuilder("bowelAccidentWarning")
            .range(p -> 0.0, p -> 1.0)
            .label(Component.translatable("setting.crinklemod.metabolism.bowel_accident_warning.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bowel_accident_warning.tooltip"))
            .defaultValue(p -> MetabolismConfig.bowelAccidentWarning)
            .getter(p -> Metabolism.of(p).getBowelAccidentWarning())
            .setter((p, v) -> Metabolism.of(p).setBowelAccidentWarning(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> BLADDER_ACCIDENT_FREQUENCY = Setting.intBuilder("bladderAccidentFrequency")
            .range(p -> 1, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.bladder_accident_frequency.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bladder_accident_frequency.tooltip"))
            .defaultValue(p -> MetabolismConfig.bladderAccidentFrequency)
            .getter(p -> Metabolism.of(p).getBladderAccidentFrequency())
            .setter((p, v) -> Metabolism.of(p).setBladderAccidentFrequency(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> BOWEL_ACCIDENT_FREQUENCY = Setting.intBuilder("bowelAccidentFrequency")
            .range(p -> 1, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.bowel_accident_frequency.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bowel_accident_frequency.tooltip"))
            .defaultValue(p -> MetabolismConfig.bowelAccidentFrequency)
            .getter(p -> Metabolism.of(p).getBowelAccidentFrequency())
            .setter((p, v) -> Metabolism.of(p).setBowelAccidentFrequency(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Double> BLADDER_ACCIDENT_AMOUNT_PERCENT = Setting.doubleBuilder("bladderAccidentAmountPercent")
            .range(p -> 0.0, p -> 1.0)
            .label(Component.translatable("setting.crinklemod.metabolism.bladder_accident_amount_percent.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bladder_accident_amount_percent.tooltip"))
            .defaultValue(p -> MetabolismConfig.bladderAccidentAmountPercent)
            .getter(p -> Metabolism.of(p).getBladderAccidentAmountPercent())
            .setter((p, v) -> Metabolism.of(p).setBladderAccidentAmountPercent(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Double> BOWEL_ACCIDENT_AMOUNT_PERCENT = Setting.doubleBuilder("bowelAccidentAmountPercent")
            .range(p -> 0.0, p -> 1.0)
            .label(Component.translatable("setting.crinklemod.metabolism.bowel_accident_amount_percent.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.bowel_accident_amount_percent.tooltip"))
            .defaultValue(p -> MetabolismConfig.bowelAccidentAmountPercent)
            .getter(p -> Metabolism.of(p).getBowelAccidentAmountPercent())
            .setter((p, v) -> Metabolism.of(p).setBowelAccidentAmountPercent(v))
            .synchronizer(Metabolism::of)
            .build();

}
