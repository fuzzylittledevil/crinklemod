package ninja.crinkle.mod.client.gui.textures;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.server.packs.resources.ResourceManager;
import ninja.crinkle.mod.client.gui.themes.Theme;
import ninja.crinkle.mod.client.gui.themes.ThemeRegistry;
import ninja.crinkle.mod.client.gui.themes.Style;
import ninja.crinkle.mod.client.gui.themes.loader.ThemeLoader;
import ninja.crinkle.mod.util.ClientUtil;
import ninja.crinkle.mod.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TextureTest {
    Theme theme;
    TextureManager textureManager;
    ResourceManager resourceManager;
    Minecraft minecraft;

    @BeforeEach
    void setUp() {
        minecraft = mock(Minecraft.class);
        textureManager = mock(TextureManager.class);
        resourceManager = mock(ResourceManager.class);
        when(minecraft.getResourceManager()).thenReturn(resourceManager);
        when(minecraft.getTextureManager()).thenReturn(textureManager);
    }

    @Test
    void generate() {
        try(MockedStatic<ClientUtil> clientUtil = TestUtil.mockClientUtil(minecraft)) {
            // These must be here due to dependency on the static ClientUtil class
            theme = TestUtil.spyTheme();
            ThemeRegistry.INSTANCE.register(theme);
            ThemeLoader.loadConfigs(minecraft.getResourceManager());
            Texture texture = theme.getWidgetTheme("panel").getAppearance(Style.Variant.active).getBackgroundTexture();
            assertNotNull(texture, "Texture is null");
            assertEquals(TestUtil.FULL_TEXTURE_PATH, texture.resourceLocation().getPath(), "Resource location path is incorrect");
        }
    }
}