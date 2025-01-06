package ninja.crinkle.mod.client.gui.widgets;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.gui.events.*;
import ninja.crinkle.mod.client.gui.events.listeners.FocusListener;
import ninja.crinkle.mod.client.gui.events.listeners.KeyListener;
import ninja.crinkle.mod.client.gui.events.listeners.MouseListener;
import ninja.crinkle.mod.client.gui.properties.Box;
import ninja.crinkle.mod.client.gui.properties.Point;
import ninja.crinkle.mod.client.gui.renderers.ThemeGraphics;
import ninja.crinkle.mod.util.ClientUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.Optional;


public class TextBox extends AbstractWidget implements KeyListener, MouseListener, FocusListener {
    private static final Logger LOGGER = LogUtils.getLogger();
    private int cursorPos = -1;
    private int visibleStart = 0;
    private boolean insertMode = true;
    private Component placeholder;
    private boolean readOnly;
    private Selection selection;
    private boolean shadow;
    private boolean shiftDown;
    private String text;

    @Override
    public void onDoubleClick(DoubleClickEvent event) {
        if (!active() || event.consumed()) {
            return;
        }
        if (selection() != null && !selection().isEmpty()
                && selection().contains(cursorPosFromPoint(event.position()))) {
            selection(new Selection(0, text().length()));
        } else {
            // select word or whole text
            int start = cursorPos();
            int end = cursorPos();
            while (start > 0 && !Character.isWhitespace(text().charAt(start - 1))) {
                start--;
            }
            while (end < text().length() && !Character.isWhitespace(text().charAt(end))) {
                end++;
            }
            cursorPos(end);
            selection(new Selection(start, end));
            event.consumed(true);
        }
    }

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

    @Override
    public void onCharTyped(@NotNull CharTypedEvent event) {
        if (!focused() || !active() || readOnly() || event.consumed()) {
            return;
        }
        if (insertMode())
            handleInsert(cursorPos(), event.codePoint());
        else
            handleOverwrite(cursorPos(), event.codePoint());
        event.consumed(true);
    }

    @Override
    public void onKey(KeyEvent event) {
        if (!focused() || !active() || readOnly() || event.consumed()) {
            return;
        }

        shiftDown(event.isShiftDown());

        if (event.pressed()) {
            LOGGER.debug("Key pressed: {}, modifiers: {}", event.keyCode(), event.modifiers());
            switch (event.keyCode()) {
                case InputConstants.KEY_ESCAPE -> handleEscape(event);
                case InputConstants.KEY_LEFT -> handleLeft(event);
                case InputConstants.KEY_RIGHT -> handleRight(event);
                case InputConstants.KEY_INSERT -> {
                    insertMode(!insertMode());
                    event.consumed(true);
                }
                case InputConstants.KEY_BACKSPACE -> handleBackspace(event);
                case InputConstants.KEY_DELETE -> handleDelete(event);
                case InputConstants.KEY_HOME -> handleHome(event);
                case InputConstants.KEY_END -> handleEnd(event);
                case InputConstants.KEY_C -> handleCopy(event);
                case InputConstants.KEY_X -> handleCut(event);
                case InputConstants.KEY_V -> handlePaste(event);
                case InputConstants.KEY_A -> {
                    if (Screen.isSelectAll(event.keyCode())) {
                        selection(new Selection(0, text().length()));
                        cursorPos(text().length());
                        event.consumed(true);
                    }
                }
            }
        }
    }

    private void handleEscape(KeyEvent event) {
        if (focused() && selection() != null) {
            selection(null);
            event.consumed(true);
        } else if (focused()) {
            focused(false);
            event.consumed(true);
        }
    }

    public void shiftDown(boolean shiftDown) {
        this.shiftDown = shiftDown;
    }

    private void handleLeft(KeyEvent event) {
        if (selection() != null && !shiftDown()) {
            cursorPos(selection().min());
            selection(null);
            event.consumed(true);
        } else if (shiftDown()) {
            if (selection() == null) {
                selection(new Selection(cursorPos(), cursorPos()));
            }
            cursorPos(Math.max(0, cursorPos() - 1));
            selection(new Selection(cursorPos(), selection().max()));
            event.consumed(true);
        } else if (cursorPos() > 0) {
            cursorPos(Math.max(0, cursorPos() - 1));
            event.consumed(true);
        }
    }

    private void handleRight(KeyEvent event) {
        if (selection() != null && !shiftDown()) {
            cursorPos(selection().max());
            selection(null);
            event.consumed(true);
        } else if (shiftDown()) {
            if (selection() == null) {
                selection(new Selection(cursorPos(), cursorPos()));
            }
            cursorPos(Math.min(text().length(), cursorPos() + 1));
            selection(new Selection(selection().min(), cursorPos()));
            event.consumed(true);
        } else if (cursorPos() < text().length()) {
            cursorPos(Math.min(text().length(), cursorPos() + 1));
            event.consumed(true);
        }
    }

    public void insertMode(boolean insertMode) {
        this.insertMode = insertMode;
    }

    private void handleBackspace(KeyEvent event) {
        int index = cursorPos();
        if (selection() != null) {
            text(text().substring(0, selection().min()) + text().substring(selection().max()));
            cursorPos(selection().min());
            selection(null);
            event.consumed(true);
        } else if (index > 0) {
            text(text().substring(0, index - 1) + text().substring(index));
            cursorPos(index - 1);
            event.consumed(true);
        }
    }

    private void handleDelete(KeyEvent event) {
        int index = cursorPos();
        if (selection() != null) {
            text(text().substring(0, selection().min()) + text().substring(selection().max()));
            cursorPos(selection().min());
            selection(null);
            event.consumed(true);
        } else if (index < text().length()) {
            text(text().substring(0, index) + text().substring(index + 1));
            event.consumed(true);
        }
    }

    private void handleHome(KeyEvent event) {
        int newPos = 0;
        if (selection() != null) {
            newPos = selection().min();
            selection(null);
        } else if (shiftDown()) {
            if (cursorPos() == 0) {
                selection(new Selection(0, 0));
            } else {
                selection(new Selection(cursorPos(), 0));
            }
        }
        cursorPos(newPos);
        event.consumed(true);
    }

    private void handleEnd(KeyEvent event) {
        int newPos = text().length();
        if (selection() != null) {
            newPos = selection().max();
            selection(null);
        } else if (event.isShiftDown()) {
            if (cursorPos() < text().length()) {
                selection(new Selection(cursorPos(), text().length()));
            }
        }
        cursorPos(newPos);
        event.consumed(true);
    }

    private void handleCopy(KeyEvent event) {
        if (Screen.isCopy(event.keyCode()) && selection() != null) {
            ClientUtil.setClipboard(selection().text(text()));
            event.consumed(true);
        }
    }

    private void handleCut(KeyEvent event) {
        if (Screen.isCut(event.keyCode()) && selection() != null) {
            ClientUtil.setClipboard(selection().text(text()));
            text(text().substring(0, selection().min()) + text().substring(selection().max()));
            cursorPos(selection().min());
            selection(null);
            event.consumed(true);
        }
    }

    private void handlePaste(KeyEvent event) {
        if (!Screen.isPaste(event.keyCode())) {
            return;
        }
        String clipboard = Optional.of(ClientUtil.getClipboard()).orElse("");
        if (selection() != null) {
            text(text().substring(0, selection().min()) + clipboard + text().substring(selection().max()));
            cursorPos(selection().min() + clipboard.length());
            selection(null);
            event.consumed(true);
        } else {
            text(text().substring(0, cursorPos()) + clipboard + text().substring(cursorPos()));
            cursorPos(cursorPos() + clipboard.length());
            event.consumed(true);
        }
    }

    public boolean shiftDown() {
        return shiftDown;
    }

    public boolean readOnly() {
        return readOnly;
    }

    public boolean insertMode() {
        return insertMode;
    }

    private void handleInsert(int index, char c) {
        if (selection() != null && !selection().isEmpty()) {
            text(text().substring(0, selection().min()) + c + text().substring(selection().max()));
            cursorPos(selection().min() + 1);
            selection(null);
        } else if (index == text().length() || index == -1) {
            text(text() + c);
        } else {
            text(text().substring(0, index) + c + text().substring(index));
        }
        cursorPos(index + 1);
    }

    public int cursorPos() {
        return cursorPos;
    }

    private void handleOverwrite(int index, char c) {
        if (selection() != null && !selection().isEmpty()) {
            text(text().substring(0, selection().min()) + c + text().substring(selection().max()));
            cursorPos(selection().min() + 1);
            selection(null);
        } else if (index == text().length()) {
            text(text() + c);
        } else {
            text(text().substring(0, index) + c + text().substring(index + 1));
        }
        cursorPos(index + 1);
    }

    public Selection selection() {
        return selection;
    }

    public void text(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }

    public void cursorPos(int cursorPos) {
        this.cursorPos = cursorPos;

        if (cursorPos == -1) {
            // Reset to the beginning when the cursor is invalid
            visibleStart(0);
            return;
        }

        // Ensure the cursor is within the bounds of the text
        cursorPos = Math.max(0, Math.min(cursorPos, text().length()));

        // Calculate the width of the visible area
        int visibleWidth = layout().boxes().contentBox().size().width();
        Font font = appearance().font();

        // Special case: If the cursor is at the end of the text
        if (cursorPos == text().length()) {
            int textPixelWidth = font.width(text());
            if (textPixelWidth > visibleWidth) {
                // Shift `newStart` to ensure the last portion of text fits in the visible area
                int newStart = 0;
                while (font.width(text().substring(newStart)) > visibleWidth) {
                    newStart++;
                }
                visibleStart(newStart);
            } else {
                visibleStart(0); // Entire text fits in the box
            }
            return;
        }

        // General case: Adjust visibleStart based on cursor position
        int newStart = Math.min(visibleStart(), cursorPos);
        int cursorPixelPos = font.width(text().substring(newStart, cursorPos));

        if (cursorPixelPos < 0) {
            // Cursor is to the left of the visible area
            newStart = cursorPos;
        } else if (cursorPixelPos > visibleWidth) {
            // Cursor is to the right of the visible area
            while (font.width(text().substring(newStart, cursorPos)) > visibleWidth) {
                newStart++;
            }
        }

        visibleStart(newStart);
    }

    public void selection(Selection selection) {
        this.selection = selection;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (!active() || event.consumed()) {
            return;
        }
        if (event.button() == MouseEvent.Button.LEFT) {
            int start = cursorPos();
            int end = cursorPosFromPoint(event.position());
            if (selection() != null && selection.contains(end)) {
                return;
            }
            cursorPos(end);
            if (shiftDown()) {
                selection(new Selection(start, end));
            } else {
                selection(null);
            }
            event.consumed(true);
        }
    }

    private int cursorPosFromPoint(Point point) {
        int cursorPos = 0;
        double charX = layout().boxes().rendered().contentBox().start().x();
        for (int i = 0; i < text().length(); i++) {
            double halfWidth = appearance().font().width(text().substring(i, i + 1)) / 2.0;
            charX += halfWidth;
            if (charX > point.x()) {
                break;
            }
            charX += halfWidth;
            cursorPos++;
        }
        return cursorPos;
    }

    @SuppressWarnings("unused")
    public void placeholder(String placeholder) {
        placeholder(Component.literal(placeholder));
    }

    public void placeholder(Component placeholder) {
        this.placeholder = placeholder;
    }

    @SuppressWarnings("unused")
    public void readOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public int visibleStart() {
        return visibleStart;
    }

    public void visibleStart(int visibleStart) {
        this.visibleStart = visibleStart;
    }

    protected String visibleText() {
        int width = layout().boxes().contentBox().size().width();
        Font font = appearance().font();
        String visibleText = text().substring(visibleStart());

        for (int i = 1; i <= visibleText.length(); i++) { // Start at 1 to avoid substring(0, 0)
            if (font.width(visibleText.substring(0, i)) > width) {
                return visibleText.substring(0, i - 1);
            }
        }
        return visibleText;
    }

    protected int visibleCursor() {
        return cursorPos() - visibleStart();
    }

    @Override
    public void renderContent(ThemeGraphics graphics, Point pMouse, Box renderedBox, float pPartialTick) {
        Point renderPos = renderedBox.start();
        if (text().isEmpty() && !focused()) {
            graphics.text(placeholder().getString(), renderPos, zIndex(), appearance().getForegroundColor().halftone(),
                    appearance().hasShadow());
        } else {
            graphics.text(visibleText(), renderPos, zIndex(), appearance().getForegroundColor(), appearance().hasShadow());
            if (selection() != null) {
                int start = Math.max(0, selection().min() - visibleStart());
                int end = Math.min(Math.max(0, selection().max() - visibleStart()), visibleText().length());
                int xStart = renderPos.xInt() + graphics.textWidth(visibleText().substring(0, start));
                int xEnd = renderPos.xInt() + graphics.textWidth(visibleText().substring(0, end));
                graphics.fill(xStart, renderPos.yInt(), xEnd, renderPos.yInt() + graphics.textHeight(), zIndex(),
                        appearance().getForegroundColor().color());
                graphics.text(selection().text(visibleText()), Point.of(xStart, renderPos.yInt()), zIndex(),
                        Optional.ofNullable(appearance().getBackgroundColor()).orElse(Color.RAINBOW),
                        appearance().hasShadow());
            }
            renderCursor(graphics, renderPos);
        }
    }

    public Component placeholder() {
        return placeholder;
    }

    private void renderCursor(ThemeGraphics graphics, Point renderPos) {
        if (!focused() || !active() || readOnly() || cursorPos() == -1) {
            return;
        }
        if (ClientUtil.getMinecraft().gui.getGuiTicks() % 20 < 10) {
            return;
        }

        // Assume cursor is at the end at first
        int cursorX = renderPos.xInt() + graphics.textWidth(visibleText());
        String character = "_";
        int cursorXEnd = insertMode() ? cursorX + 1 : cursorX + graphics.textWidth(character);
        int cursorY = renderPos.yInt();
        if (cursorPos() < text().length() && cursorPos() >= 0) {
            cursorX = renderPos.xInt() + graphics.textWidth(visibleText().substring(0, visibleCursor())) - 1;
            character = text().substring(cursorPos(), cursorPos() + 1);
            cursorXEnd = insertMode() ? cursorX + 1 : cursorX + graphics.textWidth(character);
        }

        if (insertMode()) {
            Color foregroundColor = Optional.ofNullable(appearance().getForegroundColor()).orElse(Color.RAINBOW);
            if (selection() != null && selection().contains(cursorPos())) {
                foregroundColor = Optional.ofNullable(appearance().getBackgroundColor()).orElse(Color.RAINBOW);
            }
            if (cursorPos() == text().length()) {
                graphics.text(character, Point.of(cursorX, cursorY), zIndex(), foregroundColor,
                        appearance().hasShadow());
            } else {
                graphics.fill(cursorX, cursorY, cursorXEnd, cursorY + graphics.textHeight(), zIndex(),
                        foregroundColor.color());
            }
        } else {
            Color cursorColor = Optional.ofNullable(appearance().getForegroundColor()).orElse(Color.RAINBOW);
            if (selection() != null && selection().contains(cursorPos())) {
                cursorColor = Optional.ofNullable(appearance().getBackgroundColor()).orElse(Color.RAINBOW);
            }
            if (cursorPos() < text().length()) {
                cursorX += 1;
            } else {
                cursorXEnd -= 1;
            }
            graphics.fill(cursorX, cursorY, cursorXEnd, cursorY + graphics.textHeight(), zIndex(),
                    cursorColor.inverted().color());
            graphics.text(character, Point.of(cursorX, cursorY), zIndex(),
                    Objects.requireNonNull(appearance().getBackgroundColor()), appearance().hasShadow());
        }
    }

    @SuppressWarnings("unused")
    public boolean shadow() {
        return shadow;
    }

    @SuppressWarnings("unused")
    public void shadow(boolean shadow) {
        this.shadow = shadow;
    }

    @SuppressWarnings("unused")
    public static class Builder extends AbstractWidget.AbstractBuilder<TextBox.Builder> {
        private Component placeholder = Component.empty();
        private boolean readOnly = false;
        private boolean shadow = false;
        private String text = "";

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

        @Override
        protected Builder self() {
            return this;
        }

        public Component placeholder() {
            return placeholder;
        }

        @SuppressWarnings("unused")
        public TextBox pushAndReturn() {
            TextBox textBox = new TextBox(this);
            parent().add(textBox);
            return textBox;
        }

        public Builder readOnly(boolean readOnly) {
            this.readOnly = readOnly;
            return self();
        }

        public boolean readOnly() {
            return readOnly;
        }

        public Builder shadow(boolean shadow) {
            this.shadow = shadow;
            return self();
        }

        public boolean shadow() {
            return shadow;
        }

        public Builder text(String text) {
            this.text = text;
            return self();
        }

        public String text() {
            return text;
        }
    }

    public record Selection(int start, int end) {
        public Selection {
            if (start < 0 || end < 0) {
                throw new IllegalArgumentException("Selection start and end must be positive: start=" + start + ", " +
                        "end=" + end);
            }
        }

        public boolean contains(int index) {
            return index >= min() && index < max();
        }

        public int min() {
            return Math.min(start, end);
        }

        public int max() {
            return Math.max(start, end);
        }

        @SuppressWarnings("unused")
        public int length() {
            return Math.abs(end - start);
        }

        public String text(String text) {
            int start = Math.min(this.start, this.end);
            int end = Math.min(Math.max(this.start, this.end), text.length());
            if (isEmpty()) {
                return "";
            } else if (start < end) {
                return text.substring(start, end);
            } else {
                return text.substring(end, start);
            }
        }

        public boolean isEmpty() {
            return start == end;
        }
    }
}
