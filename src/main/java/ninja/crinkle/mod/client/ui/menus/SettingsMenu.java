package ninja.crinkle.mod.client.ui.menus;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import ninja.crinkle.mod.api.ServerUpdater;
import ninja.crinkle.mod.client.icons.Icons;
import ninja.crinkle.mod.client.ui.screens.FlexContainerScreen;
import ninja.crinkle.mod.client.ui.widgets.Label;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedIconButton;
import ninja.crinkle.mod.settings.Setting;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SettingsMenu<E extends ICapabilityProvider> extends AbstractMenu {

    private static final Component UNSAVED_CHANGES_LABEL = Component.translatable("gui.crinklemod.shared.unsaved_changes.label");
    private static final int CHANGED_TEXT_COLOR = 0xff008080;
    private static final int INVALID_TEXT_COLOR = 0xff800000;
    private static final int DEFAULT_EDIT_BOX_TEXT_COLOR = 0xffffffff;
    private static final int BUTTON_SIZE = 20;
    private static final int EDIT_BOX_WIDTH = 80;
    private final Component title;
    private final Consumer<AbstractMenu> onClose;
    private final Supplier<E> entitySupplier;
    private final List<Entry<?>> configMenuEntries = new ArrayList<>();
    private final Map<Entry<?>, EditBox> editBoxes = new HashMap<>();
    private final Map<Entry<?>, Label> labels = new HashMap<>();
    private Label notificationLabel;
    private ThemedIconButton saveButton;

    protected SettingsMenu(Builder<E> builder, Screen screen) {
        super(screen, builder.leftPos, builder.topPos, builder.spacer, builder.lineSpacing, builder.margin, builder.lineHeight,
                builder.visible, builder.font);
        this.entitySupplier = builder.entitySupplier;
        this.title = builder.title;
        this.onClose = builder.onClose;
        this.configMenuEntries.addAll(builder.configMenuEntries);
        create();
        setVisible(isVisible());
        clearNotifications();
    }

    public record Entry<T extends Comparable<? super T>>(int lineNumber, int maxLength, Setting<T> setting) {
    }

    public static <E extends ICapabilityProvider> Builder<E> builder(Screen screen, Font font, Component title, Supplier<E> capabilityProvider) {
        return new Builder<>(screen, font, title, capabilityProvider);
    }

    private void clearNotifications() {
        notificationLabel.visible = false;
        notificationLabel.setHeight(0);
        if (getScreen() instanceof FlexContainerScreen flexContainerScreen) {
            flexContainerScreen.refreshFlex();
        }
    }

    private void setNotificationLabel(Component component, int color) {
        if (component.equals(Component.empty())) return;
        notificationLabel.setValue(component);
        notificationLabel.visible = true;
        notificationLabel.setColor(color);
        notificationLabel.setHeight(notificationLabel.getLineHeight());
        if (getScreen() instanceof FlexContainerScreen flexContainerScreen) {
            flexContainerScreen.refreshFlex();
        }
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
        addWidget(Label.builder(getFont(), title)
                .pos(getLeftPos() + getMargin(), getTopPos() + getMargin())
                .dropShadow(false)
                .color(0xffffffff)
                .build());
        notificationLabel = Label.builder(getFont(), Component.empty())
                .pos(getLeftPos() + getMargin(), getTopPos() + getLineYOffset(lastLineNumber + 2) + getLineSpacing())
                .wrapWidth(150)
                .dropShadow(false)
                .build();
        addWidget(notificationLabel);
        final int labelWidth = configMenuEntries.stream().mapToInt(c -> getFont().width(c.setting().label())).max().orElse(0);
        configMenuEntries.stream().sorted(Comparator.comparingInt(Entry::lineNumber)).forEach(c -> {
            final Label label = Label.builder(getFont(), c.setting().label())
                    .pos(getLeftPos() + getMargin(), getTopPos() + getLineYOffset(c.lineNumber()) + getFontOffset())
                    .dropShadow(false)
                    .build();
            addWidget(label);
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
            addWidget(editBox);
            editBoxes.put(c, editBox);
        });

        saveButton = ThemedIconButton.builder(getTheme(), Icons.SAVE)
                .tooltip(Component.translatable("gui.crinklemod.shared.save_tooltip"))
                .onPress(b -> {
                    clearNotifications();
                    configMenuEntries.forEach(c -> {
                        if (!editBoxes.get(c).getValue().isEmpty()) {
                            c.setting().set(entitySupplier.get(), c.setting().valueOf(editBoxes.get(c).getValue()));
                            c.setting().syncer(entitySupplier.get()).ifPresent(ServerUpdater::syncServer);
                        }
                    });
                })
                .bounds(getLeftPos() + getMargin(), getTopPos() + getLineYOffset(lastLineNumber + 1),
                        BUTTON_SIZE, BUTTON_SIZE)
                .build();
        addWidget(saveButton);
        addWidget(ThemedIconButton.builder(getTheme(), Icons.RESET)
                .tooltip(Component.translatable("gui.crinklemod.shared.reset_tooltip"))
                .onPress(b -> {
                    clearNotifications();
                    configMenuEntries.forEach(c -> {
                        c.setting().set(entitySupplier.get(), c.setting().getDefault(entitySupplier.get()));
                        editBoxes.get(c).setValue(String.valueOf(c.setting().get(entitySupplier.get())));
                        c.setting().syncer(entitySupplier.get()).ifPresent(ServerUpdater::syncServer);
                    });
                })
                .bounds(getLeftPos() + getMargin() + BUTTON_SIZE + getSpacer(),
                        getTopPos() + getLineYOffset(lastLineNumber + 1), BUTTON_SIZE, BUTTON_SIZE)
                .build());
        addWidget(ThemedIconButton.builder(getTheme(), Icons.BACK)
                .tooltip(Component.translatable("gui.crinklemod.shared.back_tooltip"))
                .onPress(b -> {
                    clearNotifications();
                    onClose.accept(this);
                })
                .bounds(getLeftPos() + getMargin() + (BUTTON_SIZE * 2) + (getSpacer() * 2),
                        getTopPos() + getLineYOffset(lastLineNumber + 1), BUTTON_SIZE, BUTTON_SIZE)
                .build());
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            configMenuEntries.forEach(c -> editBoxes.get(c).setValue(String.valueOf(c.setting().get(entitySupplier.get()))));
        }
    }

    public static class Builder<E extends ICapabilityProvider> {
        private final Screen screen;
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
        private final List<Entry<?>> configMenuEntries = new ArrayList<>();

        public Builder(Screen screen, Font font, Component title, Supplier<E> entitySupplier) {
            this.screen = screen;
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

        public Builder<E> entry(Entry<?> entry) {
            this.configMenuEntries.add(entry);
            return this;
        }

        public SettingsMenu<E> build() {
            return new SettingsMenu<>(this, screen);
        }
    }
}
