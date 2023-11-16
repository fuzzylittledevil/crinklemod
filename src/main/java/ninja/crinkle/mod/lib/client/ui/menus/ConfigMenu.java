package ninja.crinkle.mod.lib.client.ui.menus;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import ninja.crinkle.mod.lib.client.ClientHooks;
import ninja.crinkle.mod.lib.client.ui.widgets.Label;
import ninja.crinkle.mod.lib.common.settings.Setting;
import ninja.crinkle.mod.metabolism.common.Metabolism;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigMenu<E extends Entity> extends AbstractMenu {

    private static final Component UNSAVED_CHANGES_LABEL = Component.translatable("gui.crinklemod.shared.unsaved_changes.label");
    private static final Component SAVE_BUTTON = Component.translatable("gui.crinklemod.shared.save_button.title");
    private static final Component RESET_BUTTON = Component.translatable("gui.crinklemod.shared.reset_button.title");
    private static final Component BACK_BUTTON = Component.translatable("gui.crinklemod.shared.back_button.title");
    private static final int CHANGED_TEXT_COLOR = 0xff008080;
    private static final int INVALID_TEXT_COLOR = 0xff800000;
    private static final int DEFAULT_EDIT_BOX_TEXT_COLOR = 0xffffffff;
    private static final int BUTTON_WIDTH = 40;
    private static final int EDIT_BOX_WIDTH = 80;
    private final Component title;
    private final Consumer<AbstractMenu> onClose;
    private final Supplier<E> entitySupplier;
    private final List<Entry<?, E>> configMenuEntries = new ArrayList<>();
    private final Map<Entry<?, E>, EditBox> editBoxes = new HashMap<>();
    private final Map<Entry<?, E>, Label> labels = new HashMap<>();
    private Label notificationLabel;
    private Button saveButton;

    protected ConfigMenu(Builder<E> builder) {
        super(builder.leftPos, builder.topPos, builder.spacer, builder.lineSpacing, builder.margin, builder.lineHeight,
                builder.visible, builder.font);
        this.entitySupplier = builder.entitySupplier;
        this.title = builder.title;
        this.onClose = builder.onClose;
        this.configMenuEntries.addAll(builder.configMenuEntries);
        create();
        setVisible(isVisible());
    }

    public record Entry<T extends Comparable<? super T>, E extends Entity>(int lineNumber, int maxLength, Setting<T, E> setting) {
    }
    public static <E extends Entity> Builder<E> builder(Font font, Component title, Supplier<E> entitySupplier) {
        return new Builder<>(font, title, entitySupplier);
    }

    private void clearNotifications() {
        notificationLabel.visible = false;
    }

    private void setNotificationLabel(Component component, int color) {
        if (component.equals(Component.empty())) return;
        notificationLabel.setValue(component);
        notificationLabel.visible = true;
        notificationLabel.setColor(color);
    }

    @Override
    public void tick() {
        if (!isVisible()) return;
        // Clear state
        clearNotifications();
        saveButton.active = false;
        labels.forEach((c, l) -> l.setColor(Label.DEFAULT_COLOR));
        editBoxes.forEach((c, e) -> e.setTextColor(DEFAULT_EDIT_BOX_TEXT_COLOR));

        // Check for validation
        final List<Component> validationErrors = configMenuEntries.stream()
                .map(c -> c.setting().errors(entitySupplier.get(), editBoxes.get(c).getValue()))
                .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
        if (!validationErrors.isEmpty()) {
            setNotificationLabel(validationErrors.get(0), INVALID_TEXT_COLOR);
            return;
        }

        // Check for changes
        configMenuEntries.forEach(c -> {
            Comparable<?> value = c.setting().valueOf(editBoxes.get(c).getValue());
            if (!c.setting().get(entitySupplier.get()).equals(value)) {
                setNotificationLabel(UNSAVED_CHANGES_LABEL, CHANGED_TEXT_COLOR);
                saveButton.active = true;
                labels.get(c).setColor(CHANGED_TEXT_COLOR);
                editBoxes.get(c).setTextColor(CHANGED_TEXT_COLOR);
            }
        });
    }

    private void create() {
        final int lastLineNumber = configMenuEntries.stream().mapToInt(Entry::lineNumber).max().orElse(0);
        add(Label.builder(getFont(), title)
                .pos(getLeftPos() + getMargin(), getTopPos() + getMargin())
                .dropShadow(false)
                .color(0xffffffff)
                .build());
        notificationLabel = Label.builder(getFont(), Component.literal(""))
                .pos(getLeftPos() + getMargin(), getTopPos() + getLineYOffset(lastLineNumber + 2))
                .wrapWidth(150)
                .dropShadow(false)
                .build();
        add(notificationLabel);
        final int labelWidth = configMenuEntries.stream().mapToInt(c -> getFont().width(c.setting().label())).max().orElse(0);
        final Player player = Optional.ofNullable(DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHooks::getMinecraft))
                .map(m -> m.player)
                .orElse(null);
        final Metabolism metabolism = Metabolism.of(player);
        configMenuEntries.stream().sorted(Comparator.comparingInt(Entry::lineNumber)).forEach(c -> {
            final Label label = Label.builder(getFont(), c.setting().label())
                    .pos(getLeftPos() + getMargin(), getTopPos() + getLineYOffset(c.lineNumber()) + getFontOffset())
                    .dropShadow(false)
                    .build();
            add(label);
            labels.put(c, label);
            final EditBox editBox = new EditBox(getFont(),
                    getLeftPos() + getMargin() + labelWidth + getSpacer(),
                    getTopPos() + getLineYOffset(c.lineNumber()),
                    EDIT_BOX_WIDTH,
                    getLineHeight(),
                    c.setting().label());
            editBox.setMaxLength(c.maxLength());
            editBox.setValue(String.valueOf(c.setting.get(entitySupplier.get())));
            editBox.setTooltip(Tooltip.create(c.setting().tooltip()));
            add(editBox);
            editBoxes.put(c, editBox);
        });
        saveButton = Button.builder(SAVE_BUTTON, b -> {
                    clearNotifications();
                    configMenuEntries.forEach(c -> {
                        if (!editBoxes.get(c).getValue().isEmpty()) {
                            c.setting().set(entitySupplier.get(), c.setting().valueOf(editBoxes.get(c).getValue()));
                        }
                    });
                    metabolism.syncServer(false);
                })
                .bounds(getLeftPos() + getMargin(), getTopPos() + getLineYOffset(lastLineNumber + 1), BUTTON_WIDTH, getLineHeight())
                .build();
        add(saveButton);
        add(Button.builder(RESET_BUTTON, b -> {
                    clearNotifications();
                    configMenuEntries.forEach(c -> {
                        c.setting().set(entitySupplier.get(), c.setting().getDefault());
                        editBoxes.get(c).setValue(String.valueOf(c.setting().get(entitySupplier.get())));
                    });
                    metabolism.syncServer(true);
                })
                .bounds(getLeftPos() + getMargin() + BUTTON_WIDTH + getSpacer(), getTopPos() + getLineYOffset(lastLineNumber + 1), BUTTON_WIDTH, getLineHeight())
                .build());
        add(Button.builder(BACK_BUTTON, b -> {
                    clearNotifications();
                    onClose.accept(this);
                })
                .bounds(getLeftPos() + getMargin() + (BUTTON_WIDTH * 2) + (getSpacer() * 2), getTopPos() + getLineYOffset(lastLineNumber + 1), BUTTON_WIDTH, getLineHeight())
                .build());
    }

    public static class Builder<E extends Entity> {
        private final Component title;
        private int leftPos;
        private int topPos;
        private int spacer;
        private int margin;
        private int lineHeight;
        public int lineSpacing;
        private boolean visible;
        private final Supplier<E> entitySupplier;
        private Consumer<AbstractMenu> onClose = w -> {
        };
        private final Font font;
        private final List<Entry<?, E>> configMenuEntries = new ArrayList<>();

        public Builder(Font font, Component title, Supplier<E> entitySupplier) {
            this.font = font;
            this.title = title;
            this.entitySupplier = entitySupplier;
        }

        public Builder<E> leftPos(int leftPos) {
            this.leftPos = leftPos;
            return this;
        }

        public Builder<E> topPos(int topPos) {
            this.topPos = topPos;
            return this;
        }

        public Builder<E> origin(int leftPos, int topPos) {
            return leftPos(leftPos).topPos(topPos);
        }

        public Builder<E> visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder<E> onClose(Consumer<AbstractMenu> onClose) {
            this.onClose = onClose;
            return this;
        }

        public Builder<E> entry(Entry<?, E> entry) {
            this.configMenuEntries.add(entry);
            return this;
        }

        public ConfigMenu<E> build() {
            return new ConfigMenu<>(this);
        }
    }
}
