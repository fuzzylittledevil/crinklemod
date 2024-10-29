package ninja.crinkle.mod.client.ui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import ninja.crinkle.mod.client.ClientHooks;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.icons.Icons;
import ninja.crinkle.mod.client.ui.animations.AnimationController;
import ninja.crinkle.mod.client.ui.menus.AbstractMenu;
import ninja.crinkle.mod.client.ui.menus.SettingsMenu;
import ninja.crinkle.mod.client.ui.menus.status.*;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.api.ServerUpdater;
import ninja.crinkle.mod.client.ui.widgets.Icon;
import ninja.crinkle.mod.client.ui.widgets.Label;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedCheckbox;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedIconButton;
import ninja.crinkle.mod.client.ui.widgets.themes.ThemedSlider;
import ninja.crinkle.mod.items.custom.DiaperArmorItem;
import ninja.crinkle.mod.metabolism.Metabolism;
import ninja.crinkle.mod.metabolism.MetabolismSettings;
import ninja.crinkle.mod.undergarment.Undergarment;
import ninja.crinkle.mod.undergarment.UndergarmentSettings;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class CrinkleScreen extends FlexContainerScreen {
    private static final Component METABOLISM_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.title");
    private static final Component LIQUIDS_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.liquids_menu.title");
    private static final Component SOLIDS_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.solids_menu.title");
    private final Screen previousScreen;
    private AbstractMenu mainMenu;
    private AbstractMenu metabolismMenu;
    private AbstractMenu undergarmentMenu;
    private AbstractMenu currentMenu;
    private SettingsMenu<LocalPlayer> metabolismSettingsMenu;
    private SettingsMenu<ItemStack> undergarmentLiquidsMenu;
    private SettingsMenu<ItemStack> undergarmentSolidsMenu;
    private boolean showDiaperTextureDebug = false;
    private int currentLine = 0;

    public CrinkleScreen(Screen previousScreen) {
        super(METABOLISM_TITLE, Theme.DEFAULT);
        this.previousScreen = previousScreen;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void setCurrentMenu(AbstractMenu menu) {
        if (currentMenu != null) {
            refreshFlex();
            currentMenu.setVisible(false);
        }
        currentMenu = menu;
        currentMenu.setVisible(true);
        updateUndergarmentVisibility();
        flex();
    }

    private int nextLine() {
        return currentLine++;
    }

    @Override
    protected void init() {
        super.init();
        final int lineSpacing = 5;
        final int lineHeight = 20;
        final int componentWidth = 20;

        setPadding(lineSpacing);
        Optional.ofNullable(DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHooks::getMinecraft))
                .ifPresent(minecraft -> {
                    metabolismMenu = StatusMenu.builder(this)
                            .font(font)
                            .lineHeight(lineHeight)
                            .lineSpacing(lineSpacing)
                            .spacer(4)
                            .entry(LabelEntry.builder(font, METABOLISM_TITLE)
                                    .lineNumber(nextLine())
                                    .color(getTheme().getForegroundColor().color())
                                    .build())
                            .entry(CheckboxEntry.builder(nextLine())
                                    .checkbox(ThemedCheckbox.builder(getTheme(), Component
                                                            .translatable("gui.crinklemod.status.checkbox.numberOneEnabled.title"),
                                                    c -> {
                                                        Metabolism.of(Objects.requireNonNull(minecraft.player)).setNumberOneEnabled(c.isSelected());
                                                        MetabolismSettings.NUMBER_ONE_ENABLED.syncer(Objects.requireNonNull(minecraft.player)).ifPresent(ServerUpdater::syncServer);
                                                    })
                                            .bounds(0, 0, componentWidth, lineHeight)
                                            .selected(Metabolism.of(Objects.requireNonNull(minecraft.player)).isNumberOneEnabled())
                                            .label(Label.builder(getMinecraft().font, Component
                                                            .translatable("gui.crinklemod.status.checkbox.numberOneEnabled.title"))
                                                    .color(getTheme().getForegroundColor().color())
                                                    .build(), false)
                                            .tooltip(Tooltip.create(Component
                                                    .translatable("gui.crinklemod.status.checkbox.numberOneEnabled.tooltip")))
                                            .build())
                                    .build())
                            .entry(LabelEntry.builder(font, () ->
                                            Component.translatable("gui.crinklemod.status.label.number_one_rolls.title",
                                                    Metabolism.of(Objects.requireNonNull(minecraft.player)).getNumberOneRolls(),
                                                    Metabolism.of(Objects.requireNonNull(minecraft.player)).getNumberOneSafeRolls(),
                                                    Metabolism.of(Objects.requireNonNull(minecraft.player)).getNumberOneDesperationLevel().getLabel()))
                                    .lineNumber(nextLine())
                                    .build())
                            .entry(CheckboxEntry.builder(nextLine())
                                    .checkbox(ThemedCheckbox.builder(getTheme(), Component
                                                            .translatable("gui.crinklemod.status.checkbox.numberTwoEnabled.title"),
                                                    c -> {
                                                        Metabolism.of(Objects.requireNonNull(minecraft.player)).setNumberTwoEnabled(c.isSelected());
                                                        MetabolismSettings.NUMBER_ONE_ENABLED.syncer(Objects.requireNonNull(minecraft.player)).ifPresent(ServerUpdater::syncServer);
                                                    })
                                            .bounds(0, 0, componentWidth, lineHeight)
                                            .selected(Metabolism.of(Objects.requireNonNull(minecraft.player)).isNumberTwoEnabled())
                                            .label(Label.builder(getMinecraft().font, Component
                                                            .translatable("gui.crinklemod.status.checkbox.numberTwoEnabled.title"))
                                                    .color(getTheme().getForegroundColor().color())
                                                    .build(), false)
                                            .tooltip(Tooltip.create(Component
                                                    .translatable("gui.crinklemod.status.checkbox.numberTwoEnabled.tooltip")))
                                            .build())
                                    .build())
                            .entry(LabelEntry.builder(font, () ->
                                            Component.translatable("gui.crinklemod.status.label.number_two_rolls.title",
                                                    Metabolism.of(Objects.requireNonNull(minecraft.player)).getNumberTwoRolls(),
                                                    Metabolism.of(Objects.requireNonNull(minecraft.player)).getNumberTwoSafeRolls(),
                                                    Metabolism.of(Objects.requireNonNull(minecraft.player)).getNumberTwoDesperationLevel().getLabel()))
                                    .lineNumber(nextLine())
                                    .build())
                            .entry(ButtonBarEntry.builder(nextLine())
                                    .button(ThemedIconButton.builder(getTheme(), Icons.WRENCH)
                                            .bounds(0, 0, componentWidth, lineHeight)
                                            .tooltip(Component.translatable("gui.crinklemod.status.button.settings.tooltip"))
                                            .onPress((menu) -> setCurrentMenu(metabolismSettingsMenu))
                                            .build())
                                    .button(ThemedIconButton.builder(getTheme(), Icons.WETNESS_OUTLINED)
                                            .bounds(0, 0, componentWidth, lineHeight)
                                            .tooltip(Component.translatable("gui.crinklemod.status.button.number_one_void.tooltip"))
                                            .onPress((menu) -> Metabolism.of(Objects.requireNonNull(minecraft.player)).voidNumberOne())
                                            .build())
                                    .button(ThemedIconButton.builder(getTheme(), Icons.MESSINESS_OUTLINED)
                                            .iconColor(Color.RAINBOW)
                                            .bounds(0, 0, componentWidth, lineHeight)
                                            .tooltip(Component.translatable("gui.crinklemod.status.button.number_two_void.tooltip"))
                                            .onPress((menu) -> Metabolism.of(Objects.requireNonNull(minecraft.player)).voidNumberTwo())
                                            .build())
                                    .build())
                            .entry(SliderEntry.builder(nextLine())
                                    .slider(ThemedSlider.builder(getTheme())
                                            .bounds(0, 0, 100, lineHeight)
                                            .label(Component.literal("Slider Test"))
                                            .value(3)
                                            .interval(1)
                                            .min(0)
                                            .max(4)
                                            .trackHeight(lineHeight * 2 / 3)
                                            .build())
                                    .showLabel(true)
                                    .valueIcon(2D, Icon.builder(Icons.WRENCH).bounds(0, 0, 12, 12).build(),
                                                slider -> switch ((int) slider.getValue()) {
                                                    case 0 -> Icons.WETNESS_OUTLINED;
                                                    case 1 -> Icons.WARNING1;
                                                    case 2 -> Icons.WARNING2;
                                                    case 3 -> Icons.WARNING3;
                                                    case 4 -> Icons.MESSINESS_OUTLINED;
                                                    default -> Icons.WRENCH;
                                                })
                                    .valueButton(0D, ThemedIconButton.builder(getTheme(), Icons.WRENCH)
                                            .bounds(0, 0, 12, 12)
                                            .iconColor(Color.RAINBOW)
                                            .build(), (button, slider) -> slider.setValue(slider.getMinValue()))
                                    .valueButton(4D, ThemedIconButton.builder(getTheme(), Icons.MESSINESS_OUTLINED)
                                            .bounds(0, 0, 12, 12)
                                            .build(), (button, slider) -> slider.setValue(slider.getMaxValue()))
                                    .showValues(true)
                                    .build())
                            .build();
                    // Creating this here so nextLine() still works
                    EquipmentSlotEntry equipmentSlotEntry = new EquipmentSlotEntry(nextLine(), EquipmentSlot.LEGS,
                            Component.translatable("gui.crinklemod.status.equipment_slot.title"),
                            Tooltip.create(Component.translatable("gui.crinklemod.status.equipment_slot.tooltip")));
                    undergarmentMenu = StatusMenu.builder(this)
                            .font(font)
                            .lineHeight(lineHeight)
                            .lineSpacing(lineSpacing)
                            .spacer(4)
                            .entry(StatusBarEntry.intBuilder(() ->
                                            Undergarment.getWornUndergarment(Objects.requireNonNull(minecraft.player)))
                                    .lineNumber(nextLine())
                                    .setting(UndergarmentSettings.LIQUIDS)
                                    .onPress((menu) -> setCurrentMenu(undergarmentLiquidsMenu))
                                    .gradientStartColor(Undergarment.LIQUIDS_COLOR)
                                    .gradientEndColor(Undergarment.LIQUIDS_COLOR)
                                    .gradientBackgroundColor(Color.BLACK.color())
                                    .build())
                            .entry(StatusBarEntry.intBuilder(() ->
                                            Undergarment.getWornUndergarment(Objects.requireNonNull(minecraft.player)))
                                    .lineNumber(nextLine())
                                    .setting(UndergarmentSettings.SOLIDS)
                                    .onPress((menu) -> setCurrentMenu(undergarmentSolidsMenu))
                                    .gradientStartColor(Undergarment.SOLIDS_COLOR)
                                    .gradientEndColor(Undergarment.SOLIDS_COLOR)
                                    .gradientBackgroundColor(Color.BLACK.color())
                                    .build())
                            .entry(CheckboxEntry.builder(nextLine())
                                    .checkbox(ThemedCheckbox.builder(getTheme(), Component
                                                    .translatable("gui.crinklemod.status.checkbox.debug.title"),
                                                    c -> showDiaperTextureDebug = c.isSelected())
                                            .bounds(0, 0, componentWidth, lineHeight)
                                            .selected(showDiaperTextureDebug)
                                            .label(Label.builder(getMinecraft().font, Component
                                                            .translatable("gui.crinklemod.status.checkbox.debug.title"))
                                                    .color(getTheme().getForegroundColor().color())
                                                    .build(), true)
                                            .tooltip(Tooltip.create(Component
                                                    .translatable("gui.crinklemod.status.checkbox.debug.tooltip")))
                                            .build())
                                    .build())
                            .build();
                    mainMenu = StatusMenu.builder(this)
                            .font(font)
                            .lineHeight(lineHeight)
                            .lineSpacing(lineSpacing)
                            .spacer(4)
                            .entry(equipmentSlotEntry)
                            .subMenu(metabolismMenu)
                            .subMenu(undergarmentMenu)
                            .build();
                    mainMenu.visitAll(this::addRenderableWidget);
                    setCurrentMenu(mainMenu);
                    metabolismSettingsMenu = SettingsMenu.builder(this, font, METABOLISM_TITLE, () -> Objects.requireNonNull(minecraft.player))
                            .origin(0, 0)
                            .onClose(m -> {
                                AnimationController.INSTANCE.clearQueue();
                                setCurrentMenu(mainMenu);
                            })
                            .entry(new SettingsMenu.Entry<>(1, 10, MetabolismSettings.TIMER))
                            .entry(new SettingsMenu.Entry<>(2, 10, MetabolismSettings.NUMBER_ONE_ROLLS))
                            .entry(new SettingsMenu.Entry<>(3, 10, MetabolismSettings.NUMBER_ONE_SAFE_ROLLS))
                            .entry(new SettingsMenu.Entry<>(4, 10, MetabolismSettings.NUMBER_ONE_CHANCE))
                            .entry(new SettingsMenu.Entry<>(5, 10, MetabolismSettings.NUMBER_TWO_ROLLS))
                            .entry(new SettingsMenu.Entry<>(6, 10, MetabolismSettings.NUMBER_TWO_SAFE_ROLLS))
                            .entry(new SettingsMenu.Entry<>(7, 10, MetabolismSettings.NUMBER_TWO_CHANCE))
                            .entry(new SettingsMenu.Entry<>(8, 10, MetabolismSettings.INDICATOR_POSITION_X))
                            .entry(new SettingsMenu.Entry<>(9, 10, MetabolismSettings.INDICATOR_POSITION_Y))
                            .visible(false)
                            .build();
                    metabolismSettingsMenu.visitAll(this::addRenderableWidget);
                    undergarmentLiquidsMenu = SettingsMenu.builder(this, font, LIQUIDS_MENU_TITLE, () -> Undergarment.getWornUndergarment(Objects.requireNonNull(minecraft.player)))
                            .origin(0, 0)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new SettingsMenu.Entry<>(1, 10, UndergarmentSettings.LIQUIDS))
                            .entry(new SettingsMenu.Entry<>(2, 10, UndergarmentSettings.MAX_LIQUIDS))
                            .visible(false)
                            .build();
                    undergarmentLiquidsMenu.visitAll(this::addRenderableWidget);
                    undergarmentSolidsMenu = SettingsMenu.builder(this, font, SOLIDS_MENU_TITLE, () -> Undergarment.getWornUndergarment(Objects.requireNonNull(minecraft.player)))
                            .origin(0, 0)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new SettingsMenu.Entry<>(1, 10, UndergarmentSettings.SOLIDS))
                            .entry(new SettingsMenu.Entry<>(2, 10, UndergarmentSettings.MAX_SOLIDS))
                            .visible(false)
                            .build();
                    undergarmentSolidsMenu.visitAll(this::addRenderableWidget);
                });
    }

    private void updateUndergarmentVisibility() {
        if (currentMenu != mainMenu) return;
        boolean hasUndergarment = Undergarment.getWornUndergarment(Objects.requireNonNull(getMinecraft().player)) != ItemStack.EMPTY;
        if (undergarmentMenu.isVisible() != hasUndergarment) {
            undergarmentMenu.setVisible(hasUndergarment);
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        Optional.ofNullable(DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHooks::getMinecraft))
                .ifPresent(minecraft -> minecraft.setScreen(previousScreen));
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        if (!showDiaperTextureDebug) return;
        ItemStack item = Undergarment.getWornUndergarment(Objects.requireNonNull(getMinecraft().player));
        if (item != ItemStack.EMPTY && item.getItem() instanceof DiaperArmorItem diaper) {
            graphics.blit(diaper.getTexture(), 0, 0, 0, 0, 256, 256);
            graphics.drawString(getMinecraft().font, Component.literal(diaper.getTexture().getPath()),
                    0, 260, Color.WHITE.color(), true);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (currentMenu != null) {
            currentMenu.tick();
        }
    }
}

