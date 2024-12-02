package ninja.crinkle.mod.client.gui.widgets;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.gui.events.*;
import ninja.crinkle.mod.client.gui.events.listeners.FocusListener;
import ninja.crinkle.mod.client.gui.events.listeners.KeyListener;
import ninja.crinkle.mod.client.gui.events.listeners.MouseListener;
import ninja.crinkle.mod.client.gui.properties.Box;
import ninja.crinkle.mod.client.gui.properties.Point;
import ninja.crinkle.mod.client.gui.renderers.ThemeGraphics;
import ninja.crinkle.mod.util.ClientUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TextBox extends AbstractWidget implements KeyListener, MouseListener, FocusListener {
    private int cursorPos = -1;
    private boolean insertMode = true;
    private Component placeholder;
    private boolean readOnly;
    private String text;
    private boolean shadow;

    protected TextBox(Builder builder) {
        super(builder);
        this.text = builder.text();
        this.placeholder = builder.placeholder();
        this.shadow = builder.shadow();
        this.readOnly = builder.readOnly();
    }

    public static TextBox.Builder builder(AbstractContainer parent) {
        return new TextBox.Builder(parent);
    }

    public void cursorPos(int cursorPos) {
        this.cursorPos = cursorPos;
    }

    public void insertMode(boolean insertMode) {
        this.insertMode = insertMode;
    }

    private void handleInsert(int index, char c) {
        if (index == text().length()) {
            text(text() + c);
        } else {
            text(text().substring(0, index) + c + text().substring(index));
        }
        cursorPos(index + 1);
    }

    private void handleOverwrite(int index, char c) {
        if (index == text().length()) {
            text(text() + c);
        } else {
            text(text().substring(0, index) + c + text().substring(index + 1));
        }
        cursorPos(index + 1);
    }

    private void handleDelete(int index) {
        if (index < text().length()) {
            text(text().substring(0, index) + text().substring(index + 1));
        }
    }

    private void handleBackspace(int index) {
        if (index > 0) {
            text(text().substring(0, index - 1) + text().substring(index));
            cursorPos(index - 1);
        }
    }

    private void handleLeft() {
        cursorPos(Math.max(0, cursorPos() - 1));
    }

    private void handleRight() {
        cursorPos(Math.min(text().length(), cursorPos() + 1));
    }

    private void handleHome() {
        cursorPos(0);
    }

    private void handleEnd() {
        cursorPos(text().length());
    }

    @Override
    public void onCharTyped(@NotNull CharTypedEvent event) {
        if (!focused() || !active() || readOnly()) {
            return;
        }
        if (insertMode())
            handleInsert(cursorPos(), event.codePoint());
        else
            handleOverwrite(cursorPos(), event.codePoint());
    }

    @Override
    public void onClick(ClickEvent event) {
        if (!active()) {
            return;
        }
        if (event.button() == MouseEvent.Button.LEFT) {
            cursorPos(cursorPosFromPoint(event.position()));
        }
    }

    @Override
    public void onFocusLeft(FocusLeftEvent event) {
        cursorPos(-1);
    }

    @Override
    public void onKey(KeyEvent event) {
        if (!focused() || !active() || readOnly() || event.released()) {
            return;
        }

        switch (event.keyCode()) {
            case InputConstants.KEY_LEFT -> handleLeft();
            case InputConstants.KEY_RIGHT -> handleRight();
            case InputConstants.KEY_INSERT -> insertMode(!insertMode());
            case InputConstants.KEY_BACKSPACE -> handleBackspace(cursorPos());
            case InputConstants.KEY_DELETE -> handleDelete(cursorPos());
            case InputConstants.KEY_HOME -> handleHome();
            case InputConstants.KEY_END -> handleEnd();
        }
    }

    public boolean readOnly() {
        return readOnly;
    }

    private int cursorPosFromPoint(Point point) {
        int cursorPos = 0;
        double charX = layout().boxes().rendered().contentBox().start().x();
        for (int i = 0; i < text().length(); i++) {
            double halfWidth = appearance().font().width(text().substring(i, i + 1)) / 2.0;
            charX += halfWidth;
            if ( charX > point.x()) {
                break;
            }
            charX += halfWidth;
            cursorPos++;
        }
        return cursorPos;
    }



    public Component placeholder() {
        return placeholder;
    }

    public void placeholder(String placeholder) {
        placeholder(Component.literal(placeholder));
    }

    public void placeholder(Component placeholder) {
        this.placeholder = placeholder;
    }

    public void readOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public void renderContent(ThemeGraphics graphics, Point pMouse, Box renderedBox, float pPartialTick) {
        Point renderPos = renderedBox.start();
        if (text().isEmpty() && !focused()) {
            graphics.text(placeholder().getString(), renderPos, zIndex(), appearance().getForegroundColor().halftone(),
                    appearance().hasShadow());
        } else {
            graphics.text(text(), renderPos, zIndex(), appearance().getForegroundColor(), appearance().hasShadow());
            renderCursor(graphics, renderPos);
        }
    }

    private void renderCursor(ThemeGraphics graphics, Point renderPos) {
        if (!focused() || !active() || readOnly() || cursorPos() == -1) {
            return;
        }
        if (ClientUtil.getMinecraft().gui.getGuiTicks() % 20 < 10) {
            return;
        }

        // Assume cursor is at the end at first
        int cursorX = renderPos.xInt() + graphics.textWidth(text());
        String character = "_";
        int cursorXEnd = insertMode() ? cursorX + 1 : cursorX + graphics.textWidth(character);
        int cursorY = renderPos.yInt();
        if (cursorPos() < text().length()) {
            cursorX = renderPos.xInt() + graphics.textWidth(text().substring(0, cursorPos())) - 1;
            character = text().substring(cursorPos(), cursorPos() + 1);
            cursorXEnd = insertMode() ? cursorX + 1 : cursorX + graphics.textWidth(character);
        }

        if (insertMode()) {
            if (cursorPos() == text().length()) {
                graphics.text(character, Point.of(cursorX, cursorY), zIndex(),
                        Objects.requireNonNull(appearance().getForegroundColor()), appearance().hasShadow());
            } else {
                graphics.fill(cursorX, cursorY, cursorXEnd, cursorY + graphics.textHeight(), zIndex(),
                        appearance().getForegroundColor().color());
            }
        } else {
            if (cursorPos() < text().length()) {
                cursorX += 1;
            } else {
                cursorXEnd -= 1;
            }
            graphics.fill(cursorX, cursorY, cursorXEnd, cursorY + graphics.textHeight(), zIndex(),
                    appearance().getForegroundColor().inverted().color());
            graphics.text(character, Point.of(cursorX, cursorY), zIndex(),
                    Objects.requireNonNull(appearance().getBackgroundColor()), appearance().hasShadow());
        }
    }

    public boolean shadow() {
        return shadow;
    }

    public void shadow(boolean shadow) {
        this.shadow = shadow;
    }

    public String text() {
        return text;
    }

    public boolean insertMode() {
        return insertMode;
    }

    public int cursorPos() {
        return cursorPos;
    }

    public void text(String text) {
        this.text = text;
    }

    public static class Builder extends AbstractWidget.AbstractBuilder<TextBox.Builder> {
        private Component placeholder = Component.empty();
        private String text = "";
        private boolean shadow = false;
        private boolean readOnly = false;

        protected Builder(AbstractContainer parent) {
            super(parent);
            active(true);
            focusable(true);
        }

        @Override
        public TextBox build() {
            return new TextBox(this);
        }

        @Override
        public AbstractContainer push() {
            return parent().add(this);
        }

        public Builder placeholder(String placeholder) {
            return placeholder(Component.literal(placeholder));
        }

        public Builder placeholder(Component placeholder) {
            this.placeholder = placeholder;
            return self();
        }

        public Builder shadow(boolean shadow) {
            this.shadow = shadow;
            return self();
        }

        public boolean shadow() {
            return shadow;
        }

        public Builder readOnly(boolean readOnly) {
            this.readOnly = readOnly;
            return self();
        }

        public boolean readOnly() {
            return readOnly;
        }

        public TextBox pushAndReturn() {
            TextBox textBox = new TextBox(this);
            parent().add(textBox);
            return textBox;
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Component placeholder() {
            return placeholder;
        }

        public Builder text(String text) {
            this.text = text;
            return self();
        }

        public String text() {
            return text;
        }
    }
}
