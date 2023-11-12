package ninja.crinkle.mod.metabolism.client.ui.screens.menus;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.metabolism.client.ui.widgets.Label;
import ninja.crinkle.mod.metabolism.common.Metabolism;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigMenu extends AbstractMenu {

    private static final Component UNSAVED_CHANGES_LABEL_1 = Component.translatable("gui.crinklemod.shared.unsaved_changes.label_1");
    private static final Component UNSAVED_CHANGES_LABEL_2 = Component.translatable("gui.crinklemod.shared.unsaved_changes.label_2");
    private static final Component SAVE_BUTTON = Component.translatable("gui.crinklemod.shared.save_button.title");
    private static final Component RESET_BUTTON = Component.translatable("gui.crinklemod.shared.reset_button.title");
    private static final Component BACK_BUTTON = Component.translatable("gui.crinklemod.shared.back_button.title");
    private static final int CHANGED_TEXT_COLOR = 0xff008080;
    private static final int BUTTON_WIDTH = 40;
    private static final int EDIT_BOX_WIDTH = 80;
    private final Component title;
    private final Consumer<AbstractMenu> onClose;
    private final List<ConfigMenuEntry> configMenuEntries = new ArrayList<>();
    private final Map<ConfigMenuEntry, EditBox> editBoxes = new HashMap<>();
    private final Map<ConfigMenuEntry, Label> labels = new HashMap<>();
    private Label unsavedDataLabel_1;
    private Label unsavedDataLabel_2;

    protected ConfigMenu(Builder builder) {
        super(builder.leftPos, builder.topPos, builder.spacer, builder.lineSpacing, builder.margin, builder.lineHeight,
                builder.visible, builder.metabolismSupplier, builder.font);
        this.title = builder.title;
        this.onClose = builder.onClose;
        this.configMenuEntries.addAll(builder.configMenuEntries);
        create();
        setVisible(isVisible());
    }

    public static Builder builder(Font font, Component title) {
        return new Builder(font, title);
    }

    private void updateDirty() {
        configMenuEntries.forEach(c -> {
            try {
                double value = Double.parseDouble(editBoxes.get(c).getValue());
                final boolean visible = value != c.getValue();
                unsavedDataLabel_1.visible = visible;
                unsavedDataLabel_2.visible = visible;
                labels.get(c).setColor(visible ? CHANGED_TEXT_COLOR : Label.DEFAULT_COLOR);
                editBoxes.get(c).setTextColor(visible ? CHANGED_TEXT_COLOR : 0xffffffff);
            } catch (NumberFormatException e) {
                unsavedDataLabel_1.visible = false;
                unsavedDataLabel_2.visible = false;
                labels.get(c).setColor(Label.DEFAULT_COLOR);
                editBoxes.get(c).setTextColor(0xffffffff);
            }
        });
    }

    @Override
    public void visitChildren(Consumer<AbstractWidget> visitor) {
        super.visitChildren(visitor);
        updateDirty();
    }

    @Override
    public void tick() {
        editBoxes.forEach((c, e) -> {
            if (!e.getValue().matches("[-+]?[0-9]*\\.?[0-9]+")) {
                String s = e.getValue().replaceAll("[^\\d.+-]", "");
                e.setValue(s.isEmpty() && !e.isFocused() ? String.valueOf(c.getValue()) : s);
            }
        });
    }

    private void create() {
        Metabolism metabolism = getMetabolismSupplier().get();
        final int lastLineNumber = configMenuEntries.stream().mapToInt(ConfigMenuEntry::getLineNumber).max().orElse(0);
        add(Label.builder(getFont(), title)
                .pos(getLeftPos() + getMargin(), getTopPos() + getMargin())
                .dropShadow(false)
                .color(0xffffffff)
                .build());
        unsavedDataLabel_1 = Label.builder(getFont(), UNSAVED_CHANGES_LABEL_1)
                .pos(getLeftPos() + getMargin(), getTopPos() + getLineYOffset(lastLineNumber + 2))
                .dropShadow(false)
                .color(CHANGED_TEXT_COLOR)
                .build();
        add(unsavedDataLabel_1);
        unsavedDataLabel_2 = Label.builder(getFont(), UNSAVED_CHANGES_LABEL_2)
                .pos(getLeftPos() + getMargin(), getTopPos() + getLineYOffset(lastLineNumber + 3))
                .dropShadow(false)
                .color(CHANGED_TEXT_COLOR)
                .build();
        add(unsavedDataLabel_2);
        final int labelWidth = configMenuEntries.stream().mapToInt(c -> getFont().width(c.getLabel())).max().orElse(0);
        configMenuEntries.stream().sorted(Comparator.comparingInt(ConfigMenuEntry::getLineNumber)).forEach(c -> {
            final Label label = Label.builder(getFont(), c.getLabel())
                    .pos(getLeftPos() + getMargin(), getTopPos() + getLineYOffset(c.getLineNumber()) + getFontOffset())
                    .dropShadow(false)
                    .build();
            add(label);
            labels.put(c, label);
            final EditBox editBox = new EditBox(getFont(),
                    getLeftPos() + getMargin() + labelWidth + getSpacer(),
                    getTopPos() + getLineYOffset(c.getLineNumber()),
                    EDIT_BOX_WIDTH,
                    getLineHeight(),
                    c.getLabel());
            editBox.setResponder(s -> {
                if (!s.isEmpty()) {
                    try {
                        double value = Double.parseDouble(s);
                        if (value != c.getValue()) {
                            unsavedDataLabel_1.visible = true;
                            unsavedDataLabel_2.visible = true;
                            label.setColor(CHANGED_TEXT_COLOR);
                            editBox.setTextColor(CHANGED_TEXT_COLOR);
                        }
                        else {
                            unsavedDataLabel_1.visible = false;
                            unsavedDataLabel_2.visible = false;
                            label.setColor(Label.DEFAULT_COLOR);
                            editBox.setTextColor(0xffffffff);
                        }
                    }
                    catch (NumberFormatException e) {
                        unsavedDataLabel_1.visible = false;
                        unsavedDataLabel_2.visible = false;
                        label.setColor(Label.DEFAULT_COLOR);
                        editBox.setTextColor(0xffffffff);
                    }
                }
            });
            editBox.setMaxLength(c.getMaxLength());
            editBox.setValue(String.valueOf(c.getValue()));
            editBox.setTooltip(Tooltip.create(c.getTooltip()));
            add(editBox);
            editBoxes.put(c, editBox);
        });
        add(Button.builder(SAVE_BUTTON, b -> {
                    configMenuEntries.forEach(c -> {
                        if (!editBoxes.get(c).getValue().isEmpty()) {
                            c.setValue(Double.parseDouble(editBoxes.get(c).getValue()));
                        }
                    });
                    metabolism.sync(false);
                    updateDirty();
                })
                .bounds(getLeftPos() + getMargin(), getTopPos() + getLineYOffset(lastLineNumber + 1), BUTTON_WIDTH, getLineHeight())
                .build());
        add(Button.builder(RESET_BUTTON, b -> {
                    configMenuEntries.forEach(c -> {
                        c.setValue(c.getDefaultValue());
                        editBoxes.get(c).setValue(String.valueOf(c.getValue()));
                    });
                    metabolism.sync(true);
                    updateDirty();
                })
                .bounds(getLeftPos() + getMargin() + BUTTON_WIDTH + getSpacer(), getTopPos() + getLineYOffset(lastLineNumber + 1), BUTTON_WIDTH, getLineHeight())
                .build());
        add(Button.builder(BACK_BUTTON, b -> onClose.accept(this))
                .bounds(getLeftPos() + getMargin() + (BUTTON_WIDTH * 2) + (getSpacer() * 2), getTopPos() + getLineYOffset(lastLineNumber + 1), BUTTON_WIDTH, getLineHeight())
                .build());
    }

    public static class Builder {
        private final Component title;
        private int leftPos;
        private int topPos;
        private int spacer;
        private int margin;
        private int lineHeight;
        public int lineSpacing;
        private boolean visible;
        private Supplier<Metabolism> metabolismSupplier = () -> null;
        private Consumer<AbstractMenu> onClose = w -> {};
        private final Font font;
        private final List<ConfigMenuEntry> configMenuEntries = new ArrayList<>();

        public Builder(Font font, Component title) {
            this.font = font;
            this.title = title;
        }

        public Builder leftPos(int leftPos) {
            this.leftPos = leftPos;
            return this;
        }

        public Builder topPos(int topPos) {
            this.topPos = topPos;
            return this;
        }

        public Builder origin(int leftPos, int topPos) {
            return leftPos(leftPos).topPos(topPos);
        }

        public Builder visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder metabolismSupplier(Supplier<Metabolism> metabolismSupplier) {
            this.metabolismSupplier = metabolismSupplier;
            return this;
        }

        public Builder onClose(Consumer<AbstractMenu> onClose) {
            this.onClose = onClose;
            return this;
        }

        public Builder entry(ConfigMenuEntry configMenuEntry) {
            this.configMenuEntries.add(configMenuEntry);
            return this;
        }

        public ConfigMenu build() {
            return new ConfigMenu(this);
        }
    }
}
