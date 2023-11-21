package ninja.crinkle.mod.client.ui.menus;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import ninja.crinkle.mod.client.ui.elements.Text;
import ninja.crinkle.mod.client.ui.widgets.GradientBar;
import ninja.crinkle.mod.client.ui.widgets.Label;
import ninja.crinkle.mod.settings.Setting;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StatusMenu extends AbstractMenu {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final int barWidth;
    private final Component title;
    private final List<Entry<?>> entries = new ArrayList<>();
    private final ICapabilityProvider provider;



    protected StatusMenu(Builder builder, ICapabilityProvider provider) {
        super(builder.leftPos, builder.topPos, builder.spacer, builder.lineSpacing, builder.margin, builder.lineHeight,
                builder.visible, builder.font);
        this.barWidth = builder.barWidth;
        this.title = builder.title;
        this.provider = provider;
        this.entries.addAll(builder.entries);
        create();
    }

    public static Builder builder(ICapabilityProvider provider) {
        return new Builder(provider);
    }

    public static class Entry<T extends Comparable<? super T>> {
        private final int lineNumber;
        private final Setting<T> setting;
        private final Consumer<AbstractMenu> onPress;
        private final int gradientStartColor;
        private final int gradientEndColor;
        private final int gradientBackgroundColor;

        public Entry(int lineNumber, Setting<T> setting, Consumer<AbstractMenu> onPress,
                     int gradientStartColor, int gradientEndColor, int gradientBackgroundColor) {
            this.lineNumber = lineNumber;
            this.setting = setting;
            this.onPress = onPress;
            this.gradientStartColor = gradientStartColor;
            this.gradientEndColor = gradientEndColor;
            this.gradientBackgroundColor = gradientBackgroundColor;
        }

        public static <E extends ICapabilityProvider> Builder<Integer, E> intBuilder() {
            return new Builder<>();
        }

        private Supplier<Component> hoverText(Setting<T> setting, ICapabilityProvider provider) {
            return () -> {
                if (setting.isInt() || setting.isDouble())
                {
                    double value = setting.getDouble(provider);
                    double max = setting.range().apply(provider).asDouble().max();
                    if(setting.isInt())
                        return Component.literal(String.format("%d / %d", (int) value, (int) max));
                    else
                        return Component.literal(String.format("%.2f / %.2f", value, max));
                }
                return Component.literal(setting.get(provider).toString());
            };
        }

        private Supplier<Component> text(Setting<T> setting, ICapabilityProvider provider) {
            return () -> {
                if (setting.isInt() || setting.isDouble()) {
                    double value = setting.getDouble(provider);
                    double max = setting.range().apply(provider).asDouble().max();
                    double p = max == 0 ? 0 : value / max * 100;
                    if (setting.isInt())
                        return Component.literal(String.format("%d%%", (int) p));
                    else
                        return Component.literal(String.format("%.2f%%", p));
                }
                return Component.literal(setting.get(provider).toString());
            };
        }

        public List<AbstractWidget> create(StatusMenu menu) {
            List<AbstractWidget> widgets = new ArrayList<>();
            ICapabilityProvider provider = menu.provider;
            widgets.add(Label.builder(menu.getFont(), setting.label())
                    .pos(menu.getLeftPos() + menu.getMargin(), menu.getTopPos() + menu.getLineYOffset(lineNumber) + menu.getFontOffset())
                    .dropShadow(false)
                    .build());
            widgets.add(GradientBar.builder(setting.label())
                    .bounds(menu.getLeftPos() + menu.getLineXOffset(), menu.getTopPos() + menu.getLineYOffset(lineNumber), menu.barWidth,
                            menu.getLineHeight())
                    .maxValueSupplier(() -> setting.range().apply(provider).asDouble().max() == 0 ? setting.getDefaultDouble() :
                            setting.range().apply(provider).asDouble().max())
                    .valueSupplier(() -> (Number) setting.get(provider))
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
            widgets.add(Button.builder(Component.literal("C"), b -> Optional.ofNullable(onPress).ifPresent(c -> c.accept(menu)))
                    .bounds(menu.getLeftPos() + menu.getLineXOffset() + menu.barWidth + menu.getSpacer(), menu.getTopPos() + menu.getLineYOffset(lineNumber), menu.getLineHeight(), menu.getLineHeight())
                    .build());
            return widgets;
        }

        static public class Builder<T extends Comparable<? super T>, E extends ICapabilityProvider> {
            private int lineNumber;
            private Setting<T> setting;
            private Consumer<AbstractMenu> onPress;
            private int gradientStartColor;
            private int gradientEndColor;
            private int gradientBackgroundColor;

            public Builder() {
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

            public Entry<T> build() {
                return new Entry<>(lineNumber, setting, onPress, gradientStartColor, gradientEndColor,
                        gradientBackgroundColor);
            }
        }
    }

    public void create() {
        LOGGER.debug("Creating status menu");
        add(Label.builder(getFont(), title)
                .pos(getLeftPos() + getMargin(), getTopPos() + getMargin())
                .dropShadow(false)
                .color(0xffffffff)
                .build());
        entries.forEach(e -> addAll(e.create(this)));
    }

    @Override
    public int getLineXOffset() {
        int offset = 0;
        for(Entry<?> entry : entries) {
            int width = getFont().width(entry.setting.label());
            if (width > offset) offset = width;
        }
        return offset + getSpacer() + getMargin();
    }

    public static class Builder {
        public int leftPos;
        public int topPos;
        public int spacer = 4;
        public int margin = 8;
        public int lineHeight = 15;
        public boolean visible = true;
        public int lineSpacing;
        public int barWidth = 80;
        public final List<Entry<?>> entries = new ArrayList<>();
        public Component title = Component.empty();
        private Font font;
        private final ICapabilityProvider provider;

        Builder(ICapabilityProvider provider) {
            this.provider = provider;
        }

        public Builder leftPos(int leftPos) {
            this.leftPos = leftPos;
            return this;
        }

        public Builder topPos(int topPos) {
            this.topPos = topPos;
            return this;
        }

        public Builder lineHeight(int lineHeight) {
            this.lineHeight = lineHeight;
            return this;
        }

        public Builder spacer(int spacer) {
            this.spacer = spacer;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder margin(int margin) {
            this.margin = margin;
            return this;
        }

        public Builder lineSpacing(int lineSpacing) {
            this.lineSpacing = lineSpacing;
            return this;
        }

        public Builder font(Font font) {
            this.font = font;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder barWidth(int barWidth) {
            this.barWidth = barWidth;
            return this;
        }

        public Builder entry(Entry<?> entry) {
            entries.add(entry);
            return this;
        }

        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        public AbstractMenu build() {
            return new StatusMenu(this, provider);
        }
    }
}
