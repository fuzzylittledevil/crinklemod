package ninja.crinkle.mod.client.ui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.api.ServerUpdater;
import ninja.crinkle.mod.client.ClientHooks;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.icons.Icons;
import ninja.crinkle.mod.client.ui.menus.AbstractMenu;
import ninja.crinkle.mod.client.ui.menus.ConfigMenu;
import ninja.crinkle.mod.client.ui.menus.status.*;
import ninja.crinkle.mod.client.ui.themes.Theme;
import ninja.crinkle.mod.events.AccidentEvent;
import ninja.crinkle.mod.metabolism.MetabolismSettings;
import ninja.crinkle.mod.undergarment.Undergarment;
import ninja.crinkle.mod.undergarment.UndergarmentSettings;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class CrinkleScreen extends FlexContainerScreen {
    private static final Component METABOLISM_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.title");
    private static final Component LIQUIDS_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.liquids_menu.title");
    private static final Component BLADDER_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.bladder_menu.title");
    private static final Component BOWEL_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.bowel_menu.title");
    private static final Component SOLIDS_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.solids_menu.title");
    private static final int LIQUIDS_COLOR = 0xff00ffff;
    private static final int SOLIDS_COLOR = 0xff90ee90;
    private static final int BLADDER_COLOR = 0xffffef00;
    private static final int BOWEL_COLOR = 0xFF836953;
    private final Screen previousScreen;
    private AbstractMenu mainMenu;
    private AbstractMenu metabolismMenu;
    private AbstractMenu undergarmentMenu;
    private AbstractMenu currentMenu;
    private ConfigMenu<Player> liquidsMenu;
    private ConfigMenu<Player> solidsMenu;
    private ConfigMenu<Player> bladderMenu;
    private ConfigMenu<Player> bowelMenu;
    private ConfigMenu<ItemStack> undergarmentLiquidsMenu;
    private ConfigMenu<ItemStack> undergarmentSolidsMenu;
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
                            .entry(StatusBarEntry.intBuilder(() -> minecraft.player)
                                    .lineNumber(nextLine())
                                    .setting(MetabolismSettings.LIQUIDS)
                                    .onPress((menu) -> setCurrentMenu(liquidsMenu))
                                    .gradientStartColor(LIQUIDS_COLOR)
                                    .gradientEndColor(LIQUIDS_COLOR)
                                    .gradientBackgroundColor(Color.BLACK.color())
                                    .build())
                            .entry(StatusBarEntry.intBuilder(() -> minecraft.player)
                                    .lineNumber(nextLine())
                                    .setting(MetabolismSettings.SOLIDS)
                                    .onPress((menu) -> setCurrentMenu(solidsMenu))
                                    .gradientStartColor(SOLIDS_COLOR)
                                    .gradientEndColor(SOLIDS_COLOR)
                                    .gradientBackgroundColor(Color.BLACK.color())
                                    .build())
                            .entry(StatusBarEntry.intBuilder(() -> minecraft.player)
                                    .lineNumber(nextLine())
                                    .setting(MetabolismSettings.BLADDER)
                                    .onPress((menu) -> setCurrentMenu(bladderMenu))
                                    .gradientStartColor(BLADDER_COLOR)
                                    .gradientEndColor(BLADDER_COLOR)
                                    .gradientBackgroundColor(Color.BLACK.color())
                                    .action(new EntryAction(
                                            Icons.DOWN,
                                            (s, p) -> {
                                                int amount = s.getInt(p);
                                                if (amount == 0) return;
                                                s.set(p, 0);
                                                s.syncer(p).ifPresent(ServerUpdater::syncServer);
                                                CrinkleMod.EVENT_BUS.post(new AccidentEvent.Bladder(minecraft.player,
                                                        amount, AccidentEvent.Side.CLIENT));
                                            },
                                            b -> {
                                                int amount = MetabolismSettings.BLADDER.get(minecraft.player);
                                                return amount > 0;
                                            },
                                            Tooltip.create(Component.translatable("gui.crinklemod.crinkle_screen.bladder.accident_button.tooltip"))
                                    ))
                                    .build())
                            .entry(StatusBarEntry.intBuilder(() -> minecraft.player)
                                    .lineNumber(nextLine())
                                    .setting(MetabolismSettings.BOWELS)
                                    .onPress((menu) -> setCurrentMenu(bowelMenu))
                                    .gradientStartColor(BOWEL_COLOR)
                                    .gradientEndColor(BOWEL_COLOR)
                                    .gradientBackgroundColor(Color.BLACK.color())
                                    .action(new EntryAction(
                                            Icons.DOWN,
                                            (s, p) -> {
                                                int amount = s.getInt(p);
                                                if (amount == 0) return;
                                                s.set(p, 0);
                                                s.syncer(p).ifPresent(ServerUpdater::syncServer);
                                                CrinkleMod.EVENT_BUS.post(new AccidentEvent.Bowels(minecraft.player,
                                                        amount, AccidentEvent.Side.CLIENT));
                                            },
                                            b -> {
                                                int amount = MetabolismSettings.BOWELS.get(minecraft.player);
                                                return amount > 0;
                                            },
                                            Tooltip.create(Component.translatable("gui.crinklemod.crinkle_screen.bowel.accident_button.tooltip"))
                                    ))
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
                    liquidsMenu = ConfigMenu.builder(this, font, LIQUIDS_MENU_TITLE, () -> (Player) minecraft.player)
                            .origin(0, 0)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, MetabolismSettings.LIQUIDS))
                            .entry(new ConfigMenu.Entry<>(2, 10, MetabolismSettings.MAX_LIQUIDS))
                            .entry(new ConfigMenu.Entry<>(3, 10, MetabolismSettings.LIQUIDS_RATE))
                            .visible(false)
                            .build();
                    liquidsMenu.visitAll(this::addRenderableWidget);
                    solidsMenu = ConfigMenu.builder(this, font, SOLIDS_MENU_TITLE, () -> (Player) minecraft.player)
                            .origin(0, 0)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, MetabolismSettings.SOLIDS))
                            .entry(new ConfigMenu.Entry<>(2, 10, MetabolismSettings.MAX_SOLIDS))
                            .entry(new ConfigMenu.Entry<>(3, 10, MetabolismSettings.SOLIDS_RATE))
                            .visible(false)
                            .build();
                    solidsMenu.visitAll(this::addRenderableWidget);
                    bladderMenu = ConfigMenu.builder(this, font, BLADDER_MENU_TITLE, () -> (Player) minecraft.player)
                            .origin(0, 0)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, MetabolismSettings.BLADDER))
                            .entry(new ConfigMenu.Entry<>(2, 10, MetabolismSettings.BLADDER_CAPACITY))
                            .entry(new ConfigMenu.Entry<>(3, 10, MetabolismSettings.BLADDER_ACCIDENT_WARNING))
                            .entry(new ConfigMenu.Entry<>(4, 10, MetabolismSettings.BLADDER_ACCIDENT_FREQUENCY))
                            .entry(new ConfigMenu.Entry<>(5, 10, MetabolismSettings.BLADDER_ACCIDENT_AMOUNT_PERCENT))
                            .visible(false)
                            .build();
                    bladderMenu.visitAll(this::addRenderableWidget);
                    bowelMenu = ConfigMenu.builder(this, font, BOWEL_MENU_TITLE, () -> (Player) minecraft.player)
                            .origin(0, 0)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, MetabolismSettings.BOWELS))
                            .entry(new ConfigMenu.Entry<>(2, 10, MetabolismSettings.BOWEL_CAPACITY))
                            .entry(new ConfigMenu.Entry<>(3, 10, MetabolismSettings.BOWEL_ACCIDENT_WARNING))
                            .entry(new ConfigMenu.Entry<>(4, 10, MetabolismSettings.BOWEL_ACCIDENT_FREQUENCY))
                            .entry(new ConfigMenu.Entry<>(5, 10, MetabolismSettings.BOWEL_ACCIDENT_AMOUNT_PERCENT))
                            .visible(false)
                            .build();
                    bowelMenu.visitAll(this::addRenderableWidget);
                    undergarmentLiquidsMenu = ConfigMenu.builder(this, font, LIQUIDS_MENU_TITLE, () -> Undergarment.getWornUndergarment(Objects.requireNonNull(minecraft.player)))
                            .origin(0, 0)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, UndergarmentSettings.LIQUIDS))
                            .entry(new ConfigMenu.Entry<>(2, 10, UndergarmentSettings.MAX_LIQUIDS))
                            .visible(false)
                            .build();
                    undergarmentLiquidsMenu.visitAll(this::addRenderableWidget);
                    undergarmentSolidsMenu = ConfigMenu.builder(this, font, SOLIDS_MENU_TITLE, () -> Undergarment.getWornUndergarment(Objects.requireNonNull(minecraft.player)))
                            .origin(0, 0)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, UndergarmentSettings.SOLIDS))
                            .entry(new ConfigMenu.Entry<>(2, 10, UndergarmentSettings.MAX_SOLIDS))
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
    }

    @Override
    public void tick() {
        super.tick();
        if (currentMenu != null) {
            currentMenu.tick();
        }
    }
}

