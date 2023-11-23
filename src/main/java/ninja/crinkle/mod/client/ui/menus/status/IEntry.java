package ninja.crinkle.mod.client.ui.menus.status;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;

import java.util.List;

public interface IEntry {
    List<AbstractWidget> create(StatusMenu menu);

    int getLineWidth(Font font);
}
