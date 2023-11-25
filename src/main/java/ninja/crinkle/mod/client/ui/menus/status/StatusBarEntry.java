package ninja.crinkle.mod.client.ui.menus.status;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import ninja.crinkle.mod.api.ServerUpdater;
import ninja.crinkle.mod.client.ui.themes.BoxTheme;
import ninja.crinkle.mod.icons.Icons;
import ninja.crinkle.mod.client.ui.elements.Text;
import ninja.crinkle.mod.client.ui.menus.AbstractMenu;
import ninja.crinkle.mod.client.ui.widgets.GradientBar;
import ninja.crinkle.mod.client.ui.widgets.Label;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedIconButton;
import ninja.crinkle.mod.settings.Setting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StatusBarEntry<T extends Comparable<? super T>> implements IEntry {
    private final int lineNumber;
    private final Setting<T> setting;
    private final Consumer<AbstractMenu> onPress;
    private final Supplier<ICapabilityProvider> provider;
    private final int barWidth = 80;
    private final int gradientStartColor;
    private final int gradientEndColor;
    private final int gradientBackgroundColor;
    private final List<EntryAction> actions = new ArrayList<>();

    public StatusBarEntry(int lineNumber, Setting<T> setting, Consumer<AbstractMenu> onPress,
                          int gradientStartColor, int gradientEndColor, int gradientBackgroundColor,
                          Supplier<ICapabilityProvider> provider,
                          List<EntryAction> actions) {
        this.lineNumber = lineNumber;
        this.setting = setting;
        this.onPress = onPress;
        this.provider = provider;
        this.gradientStartColor = gradientStartColor;
        this.gradientEndColor = gradientEndColor;
        this.gradientBackgroundColor = gradientBackgroundColor;
        this.actions.addAll(actions);
    }

    public static <E extends ICapabilityProvider> Builder<Integer, E> intBuilder(Supplier<ICapabilityProvider> provider) {
        return new Builder<>(provider);
    }

    public int getLineWidth(Font font) {
        return font.width(setting.label());
    }

    private Supplier<Component> hoverText(Setting<T> setting, Supplier<ICapabilityProvider> provider) {
        return () -> {
            if (setting.isInt() || setting.isDouble()) {
                double value = setting.getDouble(provider.get());
                double max = setting.range().apply(provider.get()).asDouble().max();
                if (setting.isInt())
                    return Component.literal(String.format("%d / %d", (int) value, (int) max));
                else
                    return Component.literal(String.format("%.2f / %.2f", value, max));
            }
            return Component.literal(setting.get(provider.get()).toString());
        };
    }

    private Supplier<Component> text(Setting<T> setting, Supplier<ICapabilityProvider> provider) {
        return () -> {
            if (setting.isInt() || setting.isDouble()) {
                double value = setting.getDouble(provider.get());
                double max = setting.range().apply(provider.get()).asDouble().max();
                double p = max == 0 ? 0 : value / max * 100;
                if (setting.isInt())
                    return Component.literal(String.format("%d%%", (int) p));
                else
                    return Component.literal(String.format("%.2f%%", p));
            }
            return Component.literal(setting.get(provider.get()).toString());
        };
    }

    public List<AbstractWidget> create(StatusMenu menu) {
        List<AbstractWidget> widgets = new ArrayList<>();

        widgets.add(Label.builder(menu.getFont(), setting.label())
                .pos(menu.getLeftPos() + menu.getMargin(), menu.getTopPos() + menu.getLineYOffset(lineNumber) + menu.getFontOffset())
                .dropShadow(false)
                .build());
        widgets.add(GradientBar.builder(setting.label())
                .bounds(menu.getLeftPos() + menu.getLineXOffset(), menu.getTopPos() + menu.getLineYOffset(lineNumber), barWidth,
                        menu.getLineHeight())
                .maxValueSupplier(() -> setting.range().apply(provider.get()).asDouble().max() == 0 ? setting.getDefaultDouble(provider.get()) :
                        setting.range().apply(provider.get()).asDouble().max())
                .valueSupplier(() -> (Number) setting.get(provider.get()))
                .tooltip(Tooltip.create(setting.tooltip()))
                .hoverText(Text.builder(menu.getFont(), hoverText(setting, provider))
                        .color(0xffffffff)
                        .dropShadow(true)
                        .build())
                .text(Text.builder(menu.getFont(), text(setting, provider))
                        .color(0xffffffff)
                        .dropShadow(true)
                        .build())
                .gradientColor(gradientStartColor, gradientEndColor, gradientBackgroundColor)
                .build());
        int buttonWidth = menu.getLineHeight() + menu.getTheme().getBorderTheme(BoxTheme.Size.MEDIUM).edgeWidth();
        int buttonHeight = menu.getLineHeight() + menu.getTheme().getBorderTheme(BoxTheme.Size.MEDIUM).edgeHeight();
        ThemedIconButton configButton = ThemedIconButton.builder(menu.getTheme(), Icons.WRENCH)
                .onPress(onPress == null ? b -> {} : b -> onPress.accept(menu))
                .x(menu.getLeftPos() + menu.getLineXOffset() + barWidth + menu.getSpacer())
                .y(menu.getTopPos() + menu.getLineYOffset(lineNumber))
                .width(buttonWidth)
                .height(buttonHeight)
                .build();
        ThemedIconButton resetButton = ThemedIconButton.builder(menu.getTheme(), Icons.RESET)
                .onPress(b -> {
                    setting.reset(provider.get());
                    setting.syncer(provider.get()).ifPresent(ServerUpdater::syncServer);
                })
                .x(configButton.getX() + configButton.getWidth() + menu.getSpacer())
                .y(menu.getTopPos() + menu.getLineYOffset(lineNumber))
                .width(buttonWidth)
                .height(buttonHeight)
                .build();
        resetButton.setTooltip(Tooltip.create(Component.translatable("gui.crinklemod.status.reset_tooltip")));
        configButton.setTooltip(Tooltip.create(Component.translatable("gui.crinklemod.status.config_tooltip")));
        widgets.add(configButton);
        widgets.add(resetButton);
        for(EntryAction action : actions) {
            ThemedIconButton actionButton = ThemedIconButton.builder(menu.getTheme(), action.getIcon())
                    .onPress(b -> action.getAction().accept(setting, provider.get()))
                    .x(resetButton.getX() + resetButton.getWidth() + menu.getSpacer())
                    .y(menu.getTopPos() + menu.getLineYOffset(lineNumber))
                    .width(buttonWidth)
                    .height(buttonHeight)
                    .build();
            actionButton.setTooltip(action.getTooltip());
            widgets.add(actionButton);
        }
        return widgets;
    }

    static public class Builder<T extends Comparable<? super T>, E extends ICapabilityProvider> {
        private final Supplier<ICapabilityProvider> provider;
        private int lineNumber;
        private Setting<T> setting;
        private Consumer<AbstractMenu> onPress;
        private int gradientStartColor;
        private int gradientEndColor;
        private int gradientBackgroundColor;
        private final List<EntryAction> actions = new ArrayList<>();

        public Builder(Supplier<ICapabilityProvider> provider) {
            this.provider = provider;
        }

        public Builder<T, E> lineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
            return this;
        }

        public Builder<T, E> setting(Setting<T> setting) {
            this.setting = setting;
            return this;
        }

        public Builder<T, E> onPress(Consumer<AbstractMenu> onPress) {
            this.onPress = onPress;
            return this;
        }

        public Builder<T, E> gradientStartColor(int gradientStartColor) {
            this.gradientStartColor = gradientStartColor;
            return this;
        }

        public Builder<T, E> gradientEndColor(int gradientEndColor) {
            this.gradientEndColor = gradientEndColor;
            return this;
        }

        public Builder<T, E> gradientBackgroundColor(int gradientBackgroundColor) {
            this.gradientBackgroundColor = gradientBackgroundColor;
            return this;
        }

        public Builder<T, E> action(EntryAction action) {
            actions.add(action);
            return this;
        }

        public StatusBarEntry<T> build() {
            return new StatusBarEntry<>(lineNumber, setting, onPress, gradientStartColor, gradientEndColor,
                    gradientBackgroundColor, provider, actions);
        }
    }
}
