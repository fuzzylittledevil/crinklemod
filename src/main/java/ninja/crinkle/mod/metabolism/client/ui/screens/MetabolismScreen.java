package ninja.crinkle.mod.metabolism.client.ui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import ninja.crinkle.mod.CrinkleMod;
import ninja.crinkle.mod.lib.client.ui.menus.AbstractMenu;
import ninja.crinkle.mod.lib.client.ui.menus.ConfigMenu;
import ninja.crinkle.mod.lib.client.ui.menus.ConfigMenuEntry;
import ninja.crinkle.mod.lib.client.ui.menus.MainMenu;
import ninja.crinkle.mod.metabolism.client.ClientHooks;
import ninja.crinkle.mod.metabolism.common.Metabolism;
import ninja.crinkle.mod.metabolism.common.config.MetabolismConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MetabolismScreen extends Screen {
    private static final Component TITLE = Component.translatable("gui.crinklemod.metabolism_screen.title");
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(CrinkleMod.MODID, "textures/gui/metabolism_configuration_background.png");
    private static final Component LIQUIDS_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.liquids_menu.title");
    private static final Component MAX_LIQUIDS_LABEL = Component.translatable("gui.crinklemod.metabolism_screen.liquids_menu.max_liquids.label");
    private static final Component MAX_LIQUIDS_TOOLTIP = Component.translatable("gui.crinklemod.metabolism_screen.liquids_menu.max_liquids.tooltip");
    private static final Component LIQUIDS_RATE_LABEL = Component.translatable("gui.crinklemod.metabolism_screen.liquids_menu.liquids_rate.label");
    private static final Component LIQUIDS_RATE_TOOLTIP = Component.translatable("gui.crinklemod.metabolism_screen.liquids_menu.liquids_rate.tooltip");
    private static final Component BLADDER_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.bladder_menu.title");
    private static final Component BLADDER_CAPACITY_LABEL = Component.translatable("gui.crinklemod.metabolism_screen.bladder_menu.bladder_capacity.label");
    private static final Component BLADDER_CAPACITY_TOOLTIP = Component.translatable("gui.crinklemod.metabolism_screen.bladder_menu.bladder_capacity.tooltip");
    private static final Component BLADDER_CONTINENCE_LABEL = Component.translatable("gui.crinklemod.metabolism_screen.bladder_menu.bladder_continence.label");
    private static final Component BLADDER_CONTINENCE_TOOLTIP = Component.translatable("gui.crinklemod.metabolism_screen.bladder_menu.bladder_continence.tooltip");
    private static final Component BOWEL_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.bowel_menu.title");
    private static final Component BOWEL_CAPACITY_LABEL = Component.translatable("gui.crinklemod.metabolism_screen.bowel_menu.bowel_capacity.label");
    private static final Component BOWEL_CAPACITY_TOOLTIP = Component.translatable("gui.crinklemod.metabolism_screen.bowel_menu.bowel_capacity.tooltip");
    private static final Component BOWEL_CONTINENCE_LABEL = Component.translatable("gui.crinklemod.metabolism_screen.bowel_menu.bowel_continence.label");
    private static final Component BOWEL_CONTINENCE_TOOLTIP = Component.translatable("gui.crinklemod.metabolism_screen.bowel_menu.bowel_continence.tooltip");
    private static final Component SOLIDS_MENU_TITLE = Component.translatable("gui.crinklemod.metabolism_screen.solids_menu.title");
    private static final Component MAX_SOLIDS_LABEL = Component.translatable("gui.crinklemod.metabolism_screen.solids_menu.max_solids.label");
    private static final Component MAX_SOLIDS_TOOLTIP = Component.translatable("gui.crinklemod.metabolism_screen.solids_menu.max_solids.tooltip");
    private static final Component SOLIDS_RATE_LABEL = Component.translatable("gui.crinklemod.metabolism_screen.solids_menu.solids_rate.label");
    private static final Component SOLIDS_RATE_TOOLTIP = Component.translatable("gui.crinklemod.metabolism_screen.solids_menu.solids_rate.tooltip");
    private final int imageWidth;
    private final int imageHeight;
    private int leftPos;
    private int topPos;

    private AbstractMenu mainMenu;
    private ConfigMenu liquidsMenu;
    private ConfigMenu solidsMenu;
    private ConfigMenu bladderMenu;
    private ConfigMenu bowelMenu;

    public MetabolismScreen() {
        super(TITLE);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        Optional.ofNullable(DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientHooks::getMinecraft))
                .ifPresent(minecraft -> {
                    Metabolism metabolism = Metabolism.of(minecraft.player);
                    mainMenu = MainMenu.builder()
                            .font(font)
                            .leftPos(leftPos)
                            .topPos(topPos)
                            .metabolismSupplier(() -> metabolism)
                            .lineHeight(15)
                            .lineSpacing(5)
                            .spacer(4)
                            .liquidsButtonPress((menu) -> {
                                menu.setVisible(false);
                                liquidsMenu.setVisible(true);
                            })
                            .solidsButtonPress((menu) -> {
                                menu.setVisible(false);
                                solidsMenu.setVisible(true);
                            })
                            .bladderButtonPress((menu) -> {
                                menu.setVisible(false);
                                bladderMenu.setVisible(true);
                            })
                            .bowelsButtonPress((menu) -> {
                                menu.setVisible(false);
                                bowelMenu.setVisible(true);
                            })
                            .build();
                    mainMenu.visitChildren(this::addRenderableWidget);
                    liquidsMenu = ConfigMenu.builder(font, LIQUIDS_MENU_TITLE)
                            .origin(leftPos, topPos)
                            .metabolismSupplier(() -> metabolism)
                            .onClose(this::handleReturn)
                            .entry(ConfigMenuEntry.builder(1, MAX_LIQUIDS_LABEL)
                                    .tooltip(MAX_LIQUIDS_TOOLTIP)
                                    .valueSupplier(metabolism::getMaxLiquids)
                                    .valueSetter(metabolism::setMaxLiquids)
                                    .defaultSupplier(() -> MetabolismConfig.maxLiquids)
                                    .maxLength(5)
                                    .build())
                            .entry(ConfigMenuEntry.builder(2, LIQUIDS_RATE_LABEL)
                                    .tooltip(LIQUIDS_RATE_TOOLTIP)
                                    .valueSupplier(metabolism::getLiquidsRate)
                                    .valueSetter(metabolism::setLiquidsRate)
                                    .defaultSupplier(() -> MetabolismConfig.liquidsRate)
                                    .maxLength(5)
                                    .build())
                            .visible(false)
                            .build();
                    liquidsMenu.visitChildren(this::addRenderableWidget);
                    solidsMenu = ConfigMenu.builder(font, SOLIDS_MENU_TITLE)
                            .origin(leftPos, topPos)
                            .metabolismSupplier(() -> metabolism)
                            .onClose(this::handleReturn)
                            .entry(ConfigMenuEntry.builder(1, MAX_SOLIDS_LABEL)
                                    .tooltip(MAX_SOLIDS_TOOLTIP)
                                    .valueSupplier(metabolism::getMaxSolids)
                                    .valueSetter(metabolism::setMaxSolids)
                                    .defaultSupplier(() -> MetabolismConfig.maxSolids)
                                    .maxLength(5)
                                    .build())
                            .entry(ConfigMenuEntry.builder(2, SOLIDS_RATE_LABEL)
                                    .tooltip(SOLIDS_RATE_TOOLTIP)
                                    .valueSupplier(metabolism::getSolidsRate)
                                    .valueSetter(metabolism::setSolidsRate)
                                    .defaultSupplier(() -> MetabolismConfig.solidsRate)
                                    .maxLength(5)
                                    .build())
                            .visible(false)
                            .build();
                    solidsMenu.visitChildren(this::addRenderableWidget);
                    bladderMenu = ConfigMenu.builder(font, BLADDER_MENU_TITLE)
                            .origin(leftPos, topPos)
                            .metabolismSupplier(() -> metabolism)
                            .onClose(this::handleReturn)
                            .entry(ConfigMenuEntry.builder(1, BLADDER_CAPACITY_LABEL)
                                    .tooltip(BLADDER_CAPACITY_TOOLTIP)
                                    .valueSupplier(metabolism::getBladderCapacity)
                                    .valueSetter(metabolism::setBladderCapacity)
                                    .defaultSupplier(() -> MetabolismConfig.bladderCapacity)
                                    .maxLength(5)
                                    .build())
                            .entry(ConfigMenuEntry.builder(2, BLADDER_CONTINENCE_LABEL)
                                    .tooltip(BLADDER_CONTINENCE_TOOLTIP)
                                    .valueSupplier(metabolism::getBladderContinence)
                                    .valueSetter(metabolism::setBladderContinence)
                                    .defaultSupplier(() -> MetabolismConfig.bladderContinence)
                                    .maxLength(5)
                                    .build())
                            .visible(false)
                            .build();
                    bladderMenu.visitChildren(this::addRenderableWidget);
                    bowelMenu = ConfigMenu.builder(font, BOWEL_MENU_TITLE)
                            .origin(leftPos, topPos)
                            .metabolismSupplier(() -> metabolism)
                            .onClose(this::handleReturn)
                            .entry(ConfigMenuEntry.builder(1, BOWEL_CAPACITY_LABEL)
                                    .tooltip(BOWEL_CAPACITY_TOOLTIP)
                                    .valueSupplier(metabolism::getBowelCapacity)
                                    .valueSetter(metabolism::setBowelCapacity)
                                    .defaultSupplier(() -> MetabolismConfig.bowelCapacity)
                                    .maxLength(5)
                                    .build())
                            .entry(ConfigMenuEntry.builder(2, BOWEL_CONTINENCE_LABEL)
                                    .tooltip(BOWEL_CONTINENCE_TOOLTIP)
                                    .valueSupplier(metabolism::getBowelContinence)
                                    .valueSetter(metabolism::setBowelContinence)
                                    .defaultSupplier(() -> MetabolismConfig.bowelContinence)
                                    .maxLength(5)
                                    .build())
                            .visible(false)
                            .build();
                    bowelMenu.visitChildren(this::addRenderableWidget);
                });
    }

    private void handleReturn(@NotNull AbstractMenu menu) {
        menu.setVisible(false);
        mainMenu.setVisible(true);
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
        mainMenu.tick();
        liquidsMenu.tick();
        solidsMenu.tick();
        bladderMenu.tick();
        bowelMenu.tick();
    }
}

