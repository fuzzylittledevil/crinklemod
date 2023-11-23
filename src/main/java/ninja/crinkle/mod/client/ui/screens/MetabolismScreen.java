package ninja.crinkle.mod.client.ui.screens;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import ninja.crinkle.mod.client.ClientHooks;
import ninja.crinkle.mod.client.ui.menus.AbstractMenu;
import ninja.crinkle.mod.client.ui.menus.ConfigMenu;
import ninja.crinkle.mod.client.ui.menus.status.EquipmentSlotEntry;
import ninja.crinkle.mod.client.ui.menus.status.StatusBarEntry;
import ninja.crinkle.mod.client.ui.menus.status.StatusMenu;
import ninja.crinkle.mod.metabolism.MetabolismSettings;
import ninja.crinkle.mod.undergarment.Undergarment;
import ninja.crinkle.mod.undergarment.UndergarmentSettings;
import ninja.crinkle.mod.util.ColorUtil;

import java.util.Objects;
import java.util.Optional;

public class MetabolismScreen extends FlexContainerScreen {
    private static final Component TITLE = Component.translatable("gui.crinklemod.metabolism_screen.title");
    private static final Component LIQUIDS_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.liquids_menu.title");
    private static final Component BLADDER_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.bladder_menu.title");
    private static final Component BOWEL_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.bowel_menu.title");
    private static final Component SOLIDS_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.solids_menu.title");
    private static final int LIQUIDS_COLOR = 0xffe0ffff;
    private static final int SOLIDS_COLOR = 0xff90ee90;
    private static final int BLADDER_COLOR = 0xffffef00;
    private static final int BOWEL_COLOR = 0xFF836953;

    private AbstractMenu mainMenu;
    private AbstractMenu currentMenu;
    private ConfigMenu<Player> liquidsMenu;
    private ConfigMenu<Player> solidsMenu;
    private ConfigMenu<Player> bladderMenu;
    private ConfigMenu<Player> bowelMenu;
    private ConfigMenu<ItemStack> undergarmentLiquidsMenu;
    private ConfigMenu<ItemStack> undergarmentSolidsMenu;

    public MetabolismScreen() {
        super(TITLE);
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
        flex();
    }

    @Override
    protected void init() {
        super.init();
        final int lineSpacing = 5;
        setPadding(lineSpacing);
        Optional.ofNullable(DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHooks::getMinecraft))
                .ifPresent(minecraft -> {
                    mainMenu = StatusMenu.builder(this)
                            .title(TITLE)
                            .font(font)
                            .leftPos(0)
                            .topPos(0)
                            .lineHeight(15)
                            .lineSpacing(lineSpacing)
                            .spacer(4)
                            .entry(StatusBarEntry.intBuilder(() -> minecraft.player)
                                    .lineNumber(1)
                                    .setting(MetabolismSettings.LIQUIDS)
                                    .onPress((menu) -> setCurrentMenu(liquidsMenu))
                                    .gradientStartColor(LIQUIDS_COLOR)
                                    .gradientEndColor(ColorUtil.darken(LIQUIDS_COLOR, 0.5f))
                                    .gradientBackgroundColor(ColorUtil.darken(LIQUIDS_COLOR, 0.25f))
                                    .build())
                            .entry(StatusBarEntry.intBuilder(() -> minecraft.player)
                                    .lineNumber(2)
                                    .setting(MetabolismSettings.SOLIDS)
                                    .onPress((menu) -> setCurrentMenu(solidsMenu))
                                    .gradientStartColor(SOLIDS_COLOR)
                                    .gradientEndColor(ColorUtil.darken(SOLIDS_COLOR, 0.5f))
                                    .gradientBackgroundColor(ColorUtil.darken(SOLIDS_COLOR, 0.25f))
                                    .build())
                            .entry(StatusBarEntry.intBuilder(() -> minecraft.player)
                                    .lineNumber(3)
                                    .setting(MetabolismSettings.BLADDER)
                                    .onPress((menu) -> setCurrentMenu(bladderMenu))
                                    .gradientStartColor(BLADDER_COLOR)
                                    .gradientEndColor(ColorUtil.darken(BLADDER_COLOR, 0.5f))
                                    .gradientBackgroundColor(ColorUtil.darken(BLADDER_COLOR, 0.25f))
                                    .build())
                            .entry(StatusBarEntry.intBuilder(() -> minecraft.player)
                                    .lineNumber(4)
                                    .setting(MetabolismSettings.BOWELS)
                                    .onPress((menu) -> setCurrentMenu(bowelMenu))
                                    .gradientStartColor(BOWEL_COLOR)
                                    .gradientEndColor(ColorUtil.darken(BOWEL_COLOR, 0.5f))
                                    .gradientBackgroundColor(ColorUtil.darken(BOWEL_COLOR, 0.25f))
                                    .build())
                            .entry(new EquipmentSlotEntry(5, EquipmentSlot.LEGS, Component.literal("Pants"), Tooltip.create(Component.literal("These are your pants"))))
                            .entry(StatusBarEntry.intBuilder(() -> Undergarment.getWornUndergarment(Objects.requireNonNull(minecraft.player)))
                                    .lineNumber(6)
                                    .setting(UndergarmentSettings.LIQUIDS)
                                    .onPress((menu) -> setCurrentMenu(undergarmentLiquidsMenu))
                                    .gradientStartColor(Undergarment.LIQUIDS_COLOR)
                                    .gradientEndColor(ColorUtil.darken(Undergarment.LIQUIDS_COLOR, 0.5f))
                                    .gradientBackgroundColor(ColorUtil.darken(Undergarment.LIQUIDS_COLOR, 0.25f))
                                    .build())
                            .entry(StatusBarEntry.intBuilder(() -> Undergarment.getWornUndergarment(Objects.requireNonNull(minecraft.player)))
                                    .lineNumber(7)
                                    .setting(UndergarmentSettings.SOLIDS)
                                    .onPress((menu) -> setCurrentMenu(undergarmentSolidsMenu))
                                    .gradientStartColor(Undergarment.SOLIDS_COLOR)
                                    .gradientEndColor(ColorUtil.darken(Undergarment.SOLIDS_COLOR, 0.5f))
                                    .gradientBackgroundColor(ColorUtil.darken(Undergarment.SOLIDS_COLOR, 0.25f))
                                    .build())
                            .build();
                    mainMenu.visitChildren(this::addRenderableWidget);
                    setCurrentMenu(mainMenu);
                    liquidsMenu = ConfigMenu.builder(this, font, LIQUIDS_MENU_TITLE, () -> (Player) minecraft.player)
                            .origin(0, 0)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, MetabolismSettings.MAX_LIQUIDS))
                            .entry(new ConfigMenu.Entry<>(2, 10, MetabolismSettings.LIQUIDS_RATE))
                            .visible(false)
                            .build();
                    liquidsMenu.visitChildren(this::addRenderableWidget);
                    solidsMenu = ConfigMenu.builder(this, font, SOLIDS_MENU_TITLE, () -> (Player) minecraft.player)
                            .origin(0, 0)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, MetabolismSettings.MAX_SOLIDS))
                            .entry(new ConfigMenu.Entry<>(2, 10, MetabolismSettings.SOLIDS_RATE))
                            .visible(false)
                            .build();
                    solidsMenu.visitChildren(this::addRenderableWidget);
                    bladderMenu = ConfigMenu.builder(this, font, BLADDER_MENU_TITLE, () -> (Player) minecraft.player)
                            .origin(0, 0)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, MetabolismSettings.BLADDER_CAPACITY))
                            .entry(new ConfigMenu.Entry<>(2, 10, MetabolismSettings.BLADDER_CONTINENCE))
                            .visible(false)
                            .build();
                    bladderMenu.visitChildren(this::addRenderableWidget);
                    bowelMenu = ConfigMenu.builder(this, font, BOWEL_MENU_TITLE, () -> (Player) minecraft.player)
                            .origin(0, 0)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, MetabolismSettings.BOWEL_CAPACITY))
                            .entry(new ConfigMenu.Entry<>(2, 10, MetabolismSettings.BOWEL_CONTINENCE))
                            .visible(false)
                            .build();
                    bowelMenu.visitChildren(this::addRenderableWidget);
                    undergarmentLiquidsMenu = ConfigMenu.builder(this, font, LIQUIDS_MENU_TITLE, () -> Undergarment.getWornUndergarment(Objects.requireNonNull(minecraft.player)))
                            .origin(0, 0)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, UndergarmentSettings.LIQUIDS))
                            .entry(new ConfigMenu.Entry<>(2, 10, UndergarmentSettings.MAX_LIQUIDS))
                            .visible(false)
                            .build();
                    undergarmentLiquidsMenu.visitChildren(this::addRenderableWidget);
                    undergarmentSolidsMenu = ConfigMenu.builder(this, font, SOLIDS_MENU_TITLE, () -> Undergarment.getWornUndergarment(Objects.requireNonNull(minecraft.player)))
                            .origin(0, 0)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, UndergarmentSettings.SOLIDS))
                            .entry(new ConfigMenu.Entry<>(2, 10, UndergarmentSettings.MAX_SOLIDS))
                            .visible(false)
                            .build();
                    undergarmentSolidsMenu.visitChildren(this::addRenderableWidget);
                });
    }

    @Override
    public void tick() {
        super.tick();
        if (currentMenu != null)
            currentMenu.tick();
    }
}

