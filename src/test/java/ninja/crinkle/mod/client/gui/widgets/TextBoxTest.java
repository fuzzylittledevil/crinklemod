package ninja.crinkle.mod.client.gui.widgets;

import com.mojang.blaze3d.platform.InputConstants;
import ninja.crinkle.mod.client.gui.events.*;
import ninja.crinkle.mod.client.gui.managers.GuiManager;
import ninja.crinkle.mod.client.gui.properties.Point;
import ninja.crinkle.mod.client.gui.properties.Scope;
import ninja.crinkle.mod.util.ClientUtil;
import ninja.crinkle.mod.util.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TextBoxTest {
    private final TextBox textBox = spy(TextBox.builder(Container.builder(GuiManager.create()).build())
            .text("")
            .build());

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        reset(textBox);
    }

    @Test
    void onCharTyped_emptyText() {
        when(textBox.focused()).thenReturn(true);
        try(MockedStatic<ClientUtil> ignored = TestUtil.mockClientUtil()) {
            CharTypedEvent event = new CharTypedEvent(Scope.Screen, textBox, 'a', 0);
            textBox.onCharTyped(event);
            assertEquals("a", textBox.text());
        }
    }

    @Test
    void onCharTyped_insertModeMiddle() {
        when(textBox.focused()).thenReturn(true);
        //noinspection SpellCheckingInspection
        textBox.text("diaer");
        textBox.cursorPos(3);
        try(MockedStatic<ClientUtil> ignored = TestUtil.mockClientUtil()) {
            CharTypedEvent event = new CharTypedEvent(Scope.Screen, textBox, 'p', 0);
            textBox.onCharTyped(event);
            assertEquals("diaper", textBox.text());
        }
    }

    @Test
    void onCharTyped_insertModeEnd() {
        when(textBox.focused()).thenReturn(true);
        textBox.text("diaper");
        textBox.cursorPos(textBox.text().length());
        try(MockedStatic<ClientUtil> ignored = TestUtil.mockClientUtil()) {
            CharTypedEvent event = new CharTypedEvent(Scope.Screen, textBox, 's', 0);
            textBox.onCharTyped(event);
            assertEquals("diapers", textBox.text());
            assertEquals(textBox.text().length(), textBox.cursorPos());
        }
    }

    @Test
    void onCharTyped_overwriteModeMiddle() {
        when(textBox.focused()).thenReturn(true);
        when(textBox.insertMode()).thenReturn(false);
        //noinspection SpellCheckingInspection
        textBox.text("dlaper");
        textBox.cursorPos(1);
        try(MockedStatic<ClientUtil> ignored = TestUtil.mockClientUtil()) {
            CharTypedEvent event = new CharTypedEvent(Scope.Screen, textBox, 'i', 0);
            textBox.onCharTyped(event);
            assertEquals("diaper", textBox.text());
        }
    }

    @Test
    void onCharTyped_overwriteModeEnd() {
        when(textBox.focused()).thenReturn(true);
        when(textBox.insertMode()).thenReturn(false);
        textBox.text("diaper");
        textBox.cursorPos(textBox.text().length());
        try(MockedStatic<ClientUtil> ignored = TestUtil.mockClientUtil()) {
            CharTypedEvent event = new CharTypedEvent(Scope.Screen, textBox, 's', 0);
            textBox.onCharTyped(event);
            assertEquals("diapers", textBox.text());
        }
    }

    @Test
    void onKey_leftMiddle() {
        when(textBox.focused()).thenReturn(true);
        textBox.text("diaper");
        textBox.cursorPos(3);
        try(MockedStatic<ClientUtil> ignored = TestUtil.mockClientUtil()) {
            KeyEvent event = new KeyEvent(Scope.Screen, textBox, InputConstants.KEY_LEFT, 263, 0, false);
            textBox.onKey(event);
            assertEquals(2, textBox.cursorPos());
        }
    }

    @Test
    void onKey_leftStart() {
        when(textBox.focused()).thenReturn(true);
        textBox.text("diaper");
        textBox.cursorPos(0);
        try(MockedStatic<ClientUtil> ignored = TestUtil.mockClientUtil()) {
            KeyEvent event = new KeyEvent(Scope.Screen, textBox, InputConstants.KEY_LEFT, 263, 0, false);
            textBox.onKey(event);
            assertEquals(0, textBox.cursorPos());
        }
    }

    @Test
    void onKey_rightMiddle() {
        when(textBox.focused()).thenReturn(true);
        textBox.text("diaper");
        textBox.cursorPos(3);
        try(MockedStatic<ClientUtil> ignored = TestUtil.mockClientUtil()) {
            KeyEvent event = new KeyEvent(Scope.Screen, textBox, InputConstants.KEY_RIGHT, 262, 0, false);
            textBox.onKey(event);
            assertEquals(4, textBox.cursorPos());
        }
    }

    @Test
    void onKey_rightEnd() {
        when(textBox.focused()).thenReturn(true);
        textBox.text("diaper");
        textBox.cursorPos(textBox.text().length());
        try(MockedStatic<ClientUtil> ignored = TestUtil.mockClientUtil()) {
            KeyEvent event = new KeyEvent(Scope.Screen, textBox, InputConstants.KEY_RIGHT, 262, 0, false);
            textBox.onKey(event);
            assertEquals(textBox.text().length(), textBox.cursorPos());
        }
    }

    @Test
    void onClick_leftEnd() {
        when(textBox.active()).thenReturn(true);
        try (MockedStatic<ClientUtil> ignored = TestUtil.mockClientUtil()) {
            Point mouse = Point.of(0, 0);
            ClickEvent event = new ClickEvent(Scope.Screen, textBox, mouse.xInt(), mouse.yInt(), 0, true, List.of(textBox));
            textBox.onClick(event);
            assertEquals(0, textBox.cursorPos());
        }
    }

    @Test
    void onClick_Middle() {
        when(textBox.active()).thenReturn(true);
        try (MockedStatic<ClientUtil> ignored = TestUtil.mockClientUtil()) {
            textBox.text("diaper");
            int xPos = ClientUtil.getMinecraft().font.width("dia");
            ClickEvent event = new ClickEvent(Scope.Screen, textBox, xPos, 0, 0, true, List.of(textBox));
            textBox.onClick(event);
            assertEquals(3, textBox.cursorPos());
        }
    }

    @Test
    void onClick_rightEnd() {
        when(textBox.active()).thenReturn(true);
        try (MockedStatic<ClientUtil> ignored = TestUtil.mockClientUtil()) {
            textBox.text("diaper");
            int xPos = ClientUtil.getMinecraft().font.width("diaper");
            ClickEvent event = new ClickEvent(Scope.Screen, textBox, xPos, 0, 0, true, List.of(textBox));
            textBox.onClick(event);
            assertEquals(textBox.text().length(), textBox.cursorPos());
        }
    }
}