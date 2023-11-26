package ninja.crinkle.mod.client.ui.menus.status;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import ninja.crinkle.mod.client.ui.widgets.Label;

import java.util.ArrayList;
import java.util.List;

public class EquipmentSlotEntry implements IEntry {
    private final int lineNumber;
    private final EquipmentSlot slot;
    private final Component label;
    private final Tooltip tooltip;

    public EquipmentSlotEntry(int lineNumber, EquipmentSlot slot, Component label, Tooltip tooltip) {
        this.lineNumber = lineNumber;
        this.slot = slot;
        this.label = label;
        this.tooltip = tooltip;
    }

    @Override
    public List<AbstractWidget> create(StatusMenu menu) {
        List<AbstractWidget> widgets = new ArrayList<>();
        Label labelComponent = Label.builder(menu.getFont(), label)
                .pos(menu.getLeftPos() + menu.getMargin(), menu.getTopPos() + menu.getLineYOffset(lineNumber) + menu.getFontOffset() + 2)
                .color(0xffffffff)
                .dropShadow(false)
                .build();
        if (tooltip != null)
            labelComponent.setTooltip(tooltip);
        widgets.add(labelComponent);
        ItemStack stack = menu.getPlayer().getItemBySlot(slot);
        int offset = menu.getFont().width(label) + menu.getSpacer() + menu.getMargin();
        Label name = Label.builder(menu.getFont(), Component.translatable("gui.crinklemod.status.equipment_slot.empty"))
                .pos(menu.getLeftPos() + offset, menu.getTopPos() + menu.getLineYOffset(lineNumber) + menu.getFontOffset() + 2)
                .dropShadow(true)
                .color(0xff404040)
                .build();
        if (!stack.isEmpty()) {
            name = Label.builder(menu.getFont(), stack.getHoverName())
                    .pos(menu.getLeftPos() + offset, menu.getTopPos() + menu.getLineYOffset(lineNumber) + menu.getFontOffset() + 2)
                    .dropShadow(false)
                    .color(0xffffffff)
                    .build();
        }
        if (tooltip != null)
            name.setTooltip(tooltip);
        widgets.add(name);
        return widgets;
    }

    @Override
    public int getLineWidth(Font font) {
        // Return 0 to prevent this entry from being considered for line width calculations
        return 0;
    }
}
