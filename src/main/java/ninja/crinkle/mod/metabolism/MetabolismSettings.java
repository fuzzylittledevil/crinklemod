package ninja.crinkle.mod.metabolism;

import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.settings.Setting;

public class MetabolismSettings {
    public static final Setting<Boolean> NUMBER_ONE_ENABLED = Setting.booleanBuilder("numberOneEnabled")
            .label(Component.translatable("setting.crinklemod.metabolism.numberOneEnabled.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.numberOneEnabled.tooltip"))
            .defaultValue(false)
            .getter(p -> Metabolism.of(p).isNumberOneEnabled())
            .setter((p, v) -> Metabolism.of(p).setNumberOneEnabled(v))
            .synchronizer(Metabolism::of)
            .build();
    public static final Setting<Boolean> NUMBER_TWO_ENABLED = Setting.booleanBuilder("numberTwoEnabled")
            .label(Component.translatable("setting.crinklemod.metabolism.numberTwoEnabled.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.numberTwoEnabled.tooltip"))
            .defaultValue(false)
            .getter(p -> Metabolism.of(p).isNumberTwoEnabled())
            .setter((p, v) -> Metabolism.of(p).setNumberTwoEnabled(v))
            .synchronizer(Metabolism::of)
            .build();
    public static final Setting<Integer> TIMER = Setting.intBuilder("timer")
            .range(p -> 10, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.timer.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.timer.tooltip"))
            .defaultValue(60)
            .getter(p -> Metabolism.of(p).getTimer())
            .setter((p, v) -> Metabolism.of(p).setTimer(v))
            .synchronizer(Metabolism::of)
            .build();
    public static final Setting<Integer> NUMBER_ONE_ROLLS = Setting.intBuilder("numberOneRolls")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.numberOneRolls.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.numberOneRolls.tooltip"))
            .defaultValue(0)
            .getter(p -> Metabolism.of(p).getNumberOneRolls())
            .setter((p, v) -> Metabolism.of(p).setNumberOneRolls(v))
            .synchronizer(Metabolism::of)
            .build();
    public static final Setting<Integer> NUMBER_ONE_SAFE_ROLLS = Setting.intBuilder("numberOneSafeRolls")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.numberOneSafeRolls.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.numberOneSafeRolls.tooltip"))
            .defaultValue(3)
            .getter(p -> Metabolism.of(p).getNumberOneSafeRolls())
            .setter((p, v) -> Metabolism.of(p).setNumberOneSafeRolls(v))
            .synchronizer(Metabolism::of)
            .build();
    public static final Setting<Double> NUMBER_ONE_CHANCE = Setting.doubleBuilder("numberOneChance")
            .range(p -> 0.0, p -> 1.0)
            .label(Component.translatable("setting.crinklemod.metabolism.numberOneChance.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.numberOneChance.tooltip"))
            .defaultValue(0.25)
            .getter(p -> Metabolism.of(p).getNumberOneChance())
            .setter((p, v) -> Metabolism.of(p).setNumberOneChance(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Integer> NUMBER_TWO_ROLLS = Setting.intBuilder("numberTwoRolls")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.numberTwoRolls.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.numberTwoRolls.tooltip"))
            .defaultValue(0)
            .getter(p -> Metabolism.of(p).getNumberTwoRolls())
            .setter((p, v) -> Metabolism.of(p).setNumberTwoRolls(v))
            .synchronizer(Metabolism::of)
            .build();
    public static final Setting<Integer> NUMBER_TWO_SAFE_ROLLS = Setting.intBuilder("numberTwoSafeRolls")
            .range(p -> 0, p -> Integer.MAX_VALUE)
            .label(Component.translatable("setting.crinklemod.metabolism.numberTwoSafeRolls.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.numberTwoSafeRolls.tooltip"))
            .defaultValue(5)
            .getter(p -> Metabolism.of(p).getNumberTwoSafeRolls())
            .setter((p, v) -> Metabolism.of(p).setNumberTwoSafeRolls(v))
            .synchronizer(Metabolism::of)
            .build();

    public static final Setting<Double> NUMBER_TWO_CHANCE = Setting.doubleBuilder("numberTwoChance")
            .range(p -> 0.0, p -> 1.0)
            .label(Component.translatable("setting.crinklemod.metabolism.numberTwoChance.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.numberTwoChance.tooltip"))
            .defaultValue(0.25)
            .getter(p -> Metabolism.of(p).getNumberTwoChance())
            .setter((p, v) -> Metabolism.of(p).setNumberTwoChance(v))
            .synchronizer(Metabolism::of)
            .build();
    public static final Setting<Integer> INDICATOR_POSITION_X = Setting.intBuilder("indicatorPositionX")
            .label(Component.translatable("setting.crinklemod.metabolism.indicatorPositionX.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.indicatorPositionX.tooltip"))
            .defaultValue(0)
            .getter(p -> Metabolism.of(p).getIndicatorPositionX())
            .setter((p, v) -> Metabolism.of(p).setIndicatorPositionX(v))
            .synchronizer(Metabolism::of)
            .build();
    public static final Setting<Integer> INDICATOR_POSITION_Y = Setting.intBuilder("indicatorPositionY")
            .label(Component.translatable("setting.crinklemod.metabolism.indicatorPositionY.label"))
            .tooltip(Component.translatable("setting.crinklemod.metabolism.indicatorPositionY.tooltip"))
            .defaultValue(0)
            .getter(p -> Metabolism.of(p).getIndicatorPositionY())
            .setter((p, v) -> Metabolism.of(p).setIndicatorPositionY(v))
            .synchronizer(Metabolism::of)
            .build();
}
