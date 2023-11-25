package ninja.crinkle.mod.client.ui.menus.status;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.ui.menus.AbstractMenu;
import ninja.crinkle.mod.client.ui.widgets.Label;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class StatusMenu extends AbstractMenu {
    private final List<IEntry> entries = new ArrayList<>();

    protected StatusMenu(Builder builder, Screen screen) {
        super(screen, builder.leftPos, builder.topPos, builder.spacer, builder.lineSpacing, builder.margin, builder.lineHeight,
                builder.visible, builder.font);
        this.entries.addAll(builder.entries);
        this.addAllSubMenus(builder.subMenus);
        create();
    }

    public static Builder builder(Screen screen) {
        return new Builder(screen);
    }

    public void create() {
        entries.forEach(e -> addAllWidgets(e.create(this)));
    }

    @Override
    public int getLineXOffset() {
        int offset = 0;
        for(IEntry entry : entries) {
            int width = entry.getLineWidth(getFont());
            if (width > offset) offset = width;
        }
        return offset + getSpacer() + getMargin();
    }

    public static class Builder {
        private final Screen screen;
        private int leftPos;
        private int topPos;
        private int spacer = 4;
        private int margin = 8;
        private int lineHeight = 15;
        private boolean visible = true;
        private int lineSpacing;
        private final List<IEntry> entries = new ArrayList<>();
        private final List<AbstractMenu> subMenus = new ArrayList<>();
        private Font font;

        Builder(Screen screen) {
            this.screen = screen;
        }

        public Builder visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder leftPos(int leftPos) {
            this.leftPos = leftPos;
            return this;
        }

        public Builder topPos(int topPos) {
            this.topPos = topPos;
            return this;
        }

        public Builder lineHeight(int lineHeight) {
            this.lineHeight = lineHeight;
            return this;
        }

        public Builder spacer(int spacer) {
            this.spacer = spacer;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder margin(int margin) {
            this.margin = margin;
            return this;
        }

        public Builder lineSpacing(int lineSpacing) {
            this.lineSpacing = lineSpacing;
            return this;
        }

        public Builder font(Font font) {
            this.font = font;
            return this;
        }

        public Builder entry(IEntry entry) {
            entries.add(entry);
            return this;
        }

        public Builder subMenu(AbstractMenu menu) {
            subMenus.add(menu);
            return this;
        }

        public Builder title(Component title) {
            return this;
        }

        public StatusMenu build() {
            return new StatusMenu(this, screen);
        }
    }
}
