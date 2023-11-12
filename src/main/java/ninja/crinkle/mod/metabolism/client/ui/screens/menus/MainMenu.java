package ninja.crinkle.mod.metabolism.client.ui.screens.menus;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.metabolism.client.ui.elements.Text;
import ninja.crinkle.mod.metabolism.client.ui.widgets.GradientBar;
import ninja.crinkle.mod.metabolism.client.ui.widgets.Label;
import ninja.crinkle.mod.metabolism.common.Metabolism;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MainMenu extends AbstractMenu {
    private static final int BAR_WIDTH = 80;
    private static final Component TITLE = Component.translatable("gui.crinklemod.metabolism_screen.main_menu.title");
    private static final Component BOWELS_LABEL = Component.translatable("gui.crinklemod.metabolism_screen.main_menu.bowels_label");
    private static final Component BLADDER_LABEL = Component.translatable("gui.crinklemod.metabolism_screen.main_menu.bladder_label");
    private static final Component SOLIDS_LABEL = Component.translatable("gui.crinklemod.metabolism_screen.main_menu.solids_label");
    private static final Component LIQUIDS_LABEL = Component.translatable("gui.crinklemod.metabolism_screen.main_menu.liquids_label");

    private final Consumer<AbstractMenu> liquidsButtonPress;
    private final Consumer<AbstractMenu> solidsButtonPress;
    private final Consumer<AbstractMenu> bladderButtonPress;
    private final Consumer<AbstractMenu> bowelsButtonPress;

    protected MainMenu(Builder builder) {
        super(builder.leftPos, builder.topPos, builder.spacer, builder.lineSpacing, builder.margin, builder.lineHeight, builder.visible, builder.metabolismSupplier, builder.font);
        this.liquidsButtonPress = builder.liquidsButtonPress;
        this.solidsButtonPress = builder.solidsButtonPress;
        this.bladderButtonPress = builder.bladderButtonPress;
        this.bowelsButtonPress = builder.bowelsButtonPress;
        create();
    }

    public static Builder builder() {
        return new Builder();
    }

    public void create() {
        Metabolism metabolism = getMetabolismSupplier().get();
        add(Label.builder(getFont(), TITLE)
                .pos(getLeftPos() + getMargin(), getTopPos() + getMargin())
                .dropShadow(false)
                .color(0xffffffff)
                .build());
        add(Label.builder(getFont(), LIQUIDS_LABEL)
                .pos(getLeftPos() + getMargin(), getTopPos() + getLineYOffset(1) + getFontOffset())
                .dropShadow(false)
                .build());
        add(GradientBar.builder(LIQUIDS_LABEL)
                .bounds(getLeftPos() + getLineXOffset(), getTopPos() + getLineYOffset(1), BAR_WIDTH, getLineHeight())
                .maxValueSupplier(metabolism::getMaxLiquids)
                .valueSupplier(metabolism::getLiquids)
                .hoverText(Text.builder(getFont(), () -> {
                            double value = metabolism.getLiquids();
                            double max = metabolism.getMaxLiquids();
                            return Component.literal(String.format("%.2f / %.2f", value, max));
                        })
                        .color(0xffffffff)
                        .dropShadow(true)
                        .build())
                .text(Text.builder(getFont(), () -> {
                            double p = metabolism.getLiquids() / metabolism.getMaxLiquids();
                            return Component.literal(String.format("%.0f%%", p * 100));
                        })
                        .color(0xffffffff)
                        .dropShadow(true)
                        .build())
                .gradientColor(0xffe0ffff, 0xffb9f2ff, 0xff008b8b)
                .build());
        add(Button.builder(Component.literal("C"), b -> liquidsButtonPress.accept(this))
                .bounds(getLeftPos() + getLineXOffset() + BAR_WIDTH + getSpacer(), getTopPos() + getLineYOffset(1), getLineHeight(), getLineHeight())
                .build());
        add(Label.builder(getFont(), SOLIDS_LABEL)
                .pos(getLeftPos() + getMargin(), getTopPos() + getLineYOffset(2) + getFontOffset())
                .dropShadow(false)
                .build());
        add(GradientBar.builder(SOLIDS_LABEL)
                .bounds(getLeftPos() + getLineXOffset(), getTopPos() + getLineYOffset(2), BAR_WIDTH, getLineHeight())
                .maxValueSupplier(metabolism::getMaxSolids)
                .valueSupplier(metabolism::getSolids)
                .hoverText(Text.builder(getFont(), () -> {
                            double value = metabolism.getSolids();
                            double max = metabolism.getMaxSolids();
                            return Component.literal(String.format("%.2f / %.2f", value, max));
                        })
                        .color(0xffffffff)
                        .dropShadow(true)
                        .build())
                .text(Text.builder(getFont(), () -> {
                            double p = metabolism.getSolids() / metabolism.getMaxSolids();
                            return Component.literal(String.format("%.0f%%", p * 100));
                        })
                        .color(0xffffffff)
                        .dropShadow(true)
                        .build())
                .gradientColor(0xff90ee90, 0xff008000, 0xff006400)
                .build());
        add(Button.builder(Component.literal("C"), b -> solidsButtonPress.accept(this))
                .bounds(getLeftPos() + getLineXOffset() + BAR_WIDTH + getSpacer(), getTopPos() + getLineYOffset(2), getLineHeight(), getLineHeight())
                .build());
        add(Label.builder(getFont(), BLADDER_LABEL)
                .pos(getLeftPos() + getMargin(), getTopPos() + getLineYOffset(3) + getFontOffset())
                .dropShadow(false)
                .build());
        add(GradientBar.builder(BLADDER_LABEL)
                .bounds(getLeftPos() + getLineXOffset(), getTopPos() + getLineYOffset(3), BAR_WIDTH, getLineHeight())
                .maxValueSupplier(metabolism::getBladderCapacity)
                .valueSupplier(metabolism::getBladder)
                .hoverText(Text.builder(getFont(), () -> {
                            double value = metabolism.getBladder();
                            double max = metabolism.getBladderCapacity();
                            return Component.literal(String.format("%.2f / %.2f", value, max));
                        })
                        .color(0xffffffff)
                        .dropShadow(true)
                        .build())
                .text(Text.builder(getFont(), () -> {
                            double p = metabolism.getBladder() / metabolism.getBladderCapacity();
                            return Component.literal(String.format("%.0f%%", p * 100));
                        })
                        .color(0xffffffff)
                        .dropShadow(true)
                        .build())
                .gradientColor(0xffffef00, 0xffffdf00, 0xfff5c71a)
                .build());
        add(Button.builder(Component.literal("C"), b -> bladderButtonPress.accept(this))
                .bounds(getLeftPos() + getLineXOffset() + BAR_WIDTH + getSpacer(), getTopPos() + getLineYOffset(3), getLineHeight(), getLineHeight())
                .build());
        add(Label.builder(getFont(), BOWELS_LABEL)
                .pos(getLeftPos() + getMargin(), getTopPos() + getLineYOffset(4) + getFontOffset())
                .dropShadow(false)
                .build());
        add(GradientBar.builder(BOWELS_LABEL)
                .bounds(getLeftPos() + getLineXOffset(), getTopPos() + getLineYOffset(4), 80, getLineHeight())
                .maxValueSupplier(metabolism::getBowelCapacity)
                .valueSupplier(metabolism::getBowels)
                .hoverText(Text.builder(getFont(), () -> {
                            double value = metabolism.getBowels();
                            double max = metabolism.getBowelCapacity();
                            return Component.literal(String.format("%.2f / %.2f", value, max));
                        })
                        .color(0xffffffff)
                        .dropShadow(true)
                        .build())
                .text(Text.builder(getFont(), () -> {
                            double p = metabolism.getBowels() / metabolism.getBowelCapacity();
                            return Component.literal(String.format("%.0f%%", p * 100));
                        })
                        .color(0xffffffff)
                        .dropShadow(true)
                        .build())
                .gradientColor(0xFF836953, 0xFF644117, 0xFF321414)
                .build());
        add(Button.builder(Component.literal("C"), b -> bowelsButtonPress.accept(this))
                .bounds(getLeftPos() + getLineXOffset() + BAR_WIDTH + getSpacer(), getTopPos() + getLineYOffset(4), getLineHeight(), getLineHeight())
                .build());
    }

    @Override
    public int getLineXOffset() {
        int offset = getFont().width(BOWELS_LABEL);
        if (getFont().width(BLADDER_LABEL) + getSpacer() > offset) offset = getFont().width(BLADDER_LABEL);
        if (getFont().width(SOLIDS_LABEL) + getSpacer() > offset) offset = getFont().width(SOLIDS_LABEL);
        if (getFont().width(LIQUIDS_LABEL) + getSpacer() > offset) offset = getFont().width(LIQUIDS_LABEL);
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
        private Supplier<Metabolism> metabolismSupplier;
        private Consumer<AbstractMenu> liquidsButtonPress;
        private Consumer<AbstractMenu> solidsButtonPress;
        private Consumer<AbstractMenu> bladderButtonPress;
        private Consumer<AbstractMenu> bowelsButtonPress;
        private Font font;

        Builder() {
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

        public Builder margin(int margin) {
            this.margin = margin;
            return this;
        }

        public Builder lineSpacing(int lineSpacing) {
            this.lineSpacing = lineSpacing;
            return this;
        }

        public Builder metabolismSupplier(Supplier<Metabolism> metabolismSupplier) {
            this.metabolismSupplier = metabolismSupplier;
            return this;
        }

        public Builder liquidsButtonPress(Consumer<AbstractMenu> liquidsButtonPress) {
            this.liquidsButtonPress = liquidsButtonPress;
            return this;
        }

        public Builder solidsButtonPress(Consumer<AbstractMenu> solidsButtonPress) {
            this.solidsButtonPress = solidsButtonPress;
            return this;
        }

        public Builder bladderButtonPress(Consumer<AbstractMenu> bladderButtonPress) {
            this.bladderButtonPress = bladderButtonPress;
            return this;
        }

        public Builder bowelsButtonPress(Consumer<AbstractMenu> bowelsButtonPress) {
            this.bowelsButtonPress = bowelsButtonPress;
            return this;
        }

        public Builder font(Font font) {
            this.font = font;
            return this;
        }

        public AbstractMenu build() {
            return new MainMenu(this);
        }
    }
}
