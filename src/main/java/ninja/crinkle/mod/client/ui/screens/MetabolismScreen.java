package ninja.crinkle.mod.client.ui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.client.ClientHooks;
import ninja.crinkle.mod.client.ui.menus.AbstractMenu;
import ninja.crinkle.mod.client.ui.menus.ConfigMenu;
import ninja.crinkle.mod.client.ui.menus.StatusMenu;
import ninja.crinkle.mod.metabolism.MetabolismSettings;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MetabolismScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui.crinklemod.metabolism_screen.title");
    private static final Component LIQUIDS_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.liquids_menu.title");
    private static final Component BLADDER_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.bladder_menu.title");
    private static final Component BOWEL_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.bowel_menu.title");
    private static final Component SOLIDS_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.solids_menu.title");

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(CrinkleMod.MODID, "textures/gui/metabolism_configuration_background.png");
    private final int imageWidth;
    private final int imageHeight;
    private int leftPos;
    private int topPos;

    private AbstractMenu mainMenu;
    private AbstractMenu currentMenu;
    private ConfigMenu<Player> liquidsMenu;
    private ConfigMenu<Player> solidsMenu;
    private ConfigMenu<Player> bladderMenu;
    private ConfigMenu<Player> bowelMenu;

    public MetabolismScreen() {
        super(TITLE);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void setCurrentMenu(AbstractMenu menu) {
        if (currentMenu != null) {
            currentMenu.setVisible(false);
        }
        currentMenu = menu;
        currentMenu.setVisible(true);
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        Optional.ofNullable(DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHooks::getMinecraft))
                .ifPresent(minecraft -> {
                    mainMenu = StatusMenu.builder(minecraft.player)
                            .title(TITLE)
                            .font(font)
                            .leftPos(leftPos)
                            .topPos(topPos)
                            .lineHeight(15)
                            .lineSpacing(5)
                            .spacer(4)
                            .entry(StatusMenu.Entry.intBuilder()
                                    .lineNumber(1)
                                    .setting(MetabolismSettings.LIQUIDS)
                                    .onPress((menu) -> setCurrentMenu(liquidsMenu))
                                    .gradientStartColor(0xffe0ffff)
                                    .gradientEndColor(0xffb9f2ff)
                                    .gradientBackgroundColor(0xff008b8b)
                                    .build())
                            .entry(StatusMenu.Entry.intBuilder()
                                    .lineNumber(2)
                                    .setting(MetabolismSettings.SOLIDS)
                                    .onPress((menu) -> setCurrentMenu(solidsMenu))
                                    .gradientStartColor(0xff90ee90)
                                    .gradientEndColor(0xff008000)
                                    .gradientBackgroundColor(0xff006400)
                                    .build())
                            .entry(StatusMenu.Entry.intBuilder()
                                    .lineNumber(3)
                                    .setting(MetabolismSettings.BLADDER)
                                    .onPress((menu) -> setCurrentMenu(bladderMenu))
                                    .gradientStartColor(0xffffef00)
                                    .gradientEndColor(0xffffdf00)
                                    .gradientBackgroundColor(0xfff5c71a)
                                    .build())
                            .entry(StatusMenu.Entry.intBuilder()
                                    .lineNumber(4)
                                    .setting(MetabolismSettings.BOWELS)
                                    .onPress((menu) -> setCurrentMenu(bowelMenu))
                                    .gradientStartColor(0xFF836953)
                                    .gradientEndColor(0xFF644117)
                                    .gradientBackgroundColor(0xFF321414)
                                    .build())
                            .build();
                    mainMenu.visitChildren(this::addRenderableWidget);
                    setCurrentMenu(mainMenu);
                    liquidsMenu = ConfigMenu.builder(font, LIQUIDS_MENU_TITLE, () -> (Player) minecraft.player)
                            .origin(leftPos, topPos)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, MetabolismSettings.MAX_LIQUIDS))
                            .entry(new ConfigMenu.Entry<>(2, 10, MetabolismSettings.LIQUIDS_RATE))
                            .visible(false)
                            .build();
                    liquidsMenu.visitChildren(this::addRenderableWidget);
                    solidsMenu = ConfigMenu.builder(font, SOLIDS_MENU_TITLE, () -> (Player) minecraft.player)
                            .origin(leftPos, topPos)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, MetabolismSettings.MAX_SOLIDS))
                            .entry(new ConfigMenu.Entry<>(2, 10, MetabolismSettings.SOLIDS_RATE))
                            .visible(false)
                            .build();
                    solidsMenu.visitChildren(this::addRenderableWidget);
                    bladderMenu = ConfigMenu.builder(font, BLADDER_MENU_TITLE, () -> (Player) minecraft.player)
                            .origin(leftPos, topPos)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, MetabolismSettings.BLADDER_CAPACITY))
                            .entry(new ConfigMenu.Entry<>(2, 10, MetabolismSettings.BLADDER_CONTINENCE))
                            .visible(false)
                            .build();
                    bladderMenu.visitChildren(this::addRenderableWidget);
                    bowelMenu = ConfigMenu.builder(font, BOWEL_MENU_TITLE, () -> (Player) minecraft.player)
                            .origin(leftPos, topPos)
                            .onClose(m -> setCurrentMenu(mainMenu))
                            .entry(new ConfigMenu.Entry<>(1, 10, MetabolismSettings.BOWEL_CAPACITY))
                            .entry(new ConfigMenu.Entry<>(2, 10, MetabolismSettings.BOWEL_CONTINENCE))
                            .visible(false)
                            .build();
                    bowelMenu.visitChildren(this::addRenderableWidget);
                });
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Background is typically rendered first
        this.renderBackground(guiGraphics);

        // Render the background texture
        guiGraphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // Then the widgets if this is a direct child of the Screen
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // Render things after widgets (tooltips)
    }

    @Override
    public void tick() {
        super.tick();
        if (currentMenu != null)
            currentMenu.tick();
    }
}

