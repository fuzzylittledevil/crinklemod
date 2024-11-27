package ninja.crinkle.mod.client.gui.themes;

import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.gui.textures.Texture;
import ninja.crinkle.mod.util.ClientUtil;
import ninja.crinkle.mod.util.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;

class StyleVariantTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void coalesceWith() {
        try(MockedStatic<ClientUtil> ignored = TestUtil.mockClientUtil()) {
            Texture testTexture = TestUtil.spyTexture(TestUtil.spyTheme());
            Color testColor = Color.BLACK;
            Color backgroundColor = Color.CYAN;
            StyleVariant base = StyleVariant.builder()
                    .backgroundTexture(testTexture)
                    .backgroundColor(backgroundColor)
                    .foregroundColor(testColor)
                    .build();
            Color overridenColor = Color.WHITE;
            StyleVariant variant = StyleVariant.builder()
                    .foregroundColor(overridenColor)
                    .build();
            StyleVariant result = base.coalesceWith(variant);
            assertEquals(testTexture, result.getBackgroundTexture(), "Background texture check");
            assertEquals(backgroundColor, result.getBackgroundColor(), "Background color check");
            assertEquals(overridenColor, result.getForegroundColor(), "Foreground color check");
        }
    }
}