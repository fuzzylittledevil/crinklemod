package ninja.crinkle.mod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import ninja.crinkle.mod.client.color.Color;
import ninja.crinkle.mod.client.gui.properties.Point;
import ninja.crinkle.mod.client.gui.properties.Size;
import ninja.crinkle.mod.client.gui.textures.Texture;
import ninja.crinkle.mod.client.gui.textures.ThemeAtlas;
import ninja.crinkle.mod.client.gui.themes.StyleVariant;
import ninja.crinkle.mod.client.gui.themes.Theme;
import ninja.crinkle.mod.client.gui.themes.Style;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class TestUtil {
    public static Theme spyTheme() {
        Theme theme = spy(Theme.builder("test").name("Test").description("Test theme").version("0.0.0").build());
        Texture texture = spyTexture(theme);
        ThemeAtlas.register(new ResourceLocation(FULL_TEXTURE_PATH));
        theme.addTexture(texture);
        theme.addColor("background", Color.of("#AA7FD6"));
        theme.addColor("primary", Color.of("#D6AA7F"));
        theme.addColor("secondary", Color.of("#7FD6AA"));
        theme.addColor("inactive", Color.of("#B8A5C3"));
        theme.addColor("highlight", Color.of("#FFD766"));
        theme.addColor("accent", Color.of("#9E5ABD"));
        theme.addColor("text", Color.of("#333333"));
        theme.addColor("inactiveText", Color.of("#E7D1F2"));
        theme.addColor("highlightText", Color.of("#F0F0F0"));
        theme.addWidgetTheme(spyWidgetTheme(theme));
        return theme;
    }

    public static String FULL_TEXTURE_PATH = "theme/test/widgets/panel_background";
    public static Texture spyTexture(Theme theme) {
        Texture.Builder b = new Texture.Builder("panel_background", theme).location("widgets/panel_background");
        b.addSlice(Texture.Slice.Location.topRight, Point.of(0, 0), Size.of(5, 5));
        b.addSlice(Texture.Slice.Location.top, Point.of(5, 0), Size.of(1, 5));
        b.addSlice(Texture.Slice.Location.topLeft, Point.of(6, 0), Size.of(5, 5));
        b.addSlice(Texture.Slice.Location.right, Point.of(0, 5), Size.of(5, 1));
        b.addSlice(Texture.Slice.Location.center, Point.of(5, 5), Size.of(1, 1));
        b.addSlice(Texture.Slice.Location.left, Point.of(6, 5), Size.of(5, 1));
        b.addSlice(Texture.Slice.Location.bottomRight, Point.of(0, 6), Size.of(5, 5));
        b.addSlice(Texture.Slice.Location.bottom, Point.of(5, 6), Size.of(1, 5));
        b.addSlice(Texture.Slice.Location.bottomLeft, Point.of(6, 6), Size.of(5, 5));
        return spy(b.build());
    }

    public static Style spyWidgetTheme(Theme theme) {
        Style.Builder b = Style.builder("panel")
                .theme(theme)
                .addAppearance(Style.Variant.active, spyAppearance(theme));
        return spy(b.build());
    }

    public static StyleVariant spyAppearance(Theme theme) {
        StyleVariant.Builder b = StyleVariant.builder();
        b.backgroundTexture(theme.getTexture("panel_background"));
        b.foregroundColor(Color.of("#333333"));
        return b.build();
    }

    public static MockedStatic<ClientUtil> mockClientUtil() {
        var minecraft = mock(Minecraft.class);
        Field field = ReflectionUtils.findFields(Minecraft.class, f -> f.getName().equals("font"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        ReflectionUtils.makeAccessible(field);
        var font = mock(Font.class);
        try {
            field.set(minecraft, font);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        when(minecraft.font.width(anyString())).thenAnswer(invocation -> {
            String text = invocation.getArgument(0);
            return text.length() * 5;
        });
        Field lineHeightField = ReflectionUtils.findFields(Font.class, f -> f.getName().equals("lineHeight"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        ReflectionUtils.makeAccessible(lineHeightField);
        try {
            lineHeightField.set(font, 10);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        var textureManager = mock(TextureManager.class);
        var resourceManager = mock(ResourceManager.class);
        when(minecraft.getResourceManager()).thenReturn(resourceManager);
        when(minecraft.getTextureManager()).thenReturn(textureManager);
        return mockClientUtil(minecraft);
    }

    public static MockedStatic<ClientUtil> mockClientUtil(Minecraft mockedMinecraft) {
        var clientUtil = mockStatic(ClientUtil.class);
        clientUtil.when(ClientUtil::getMinecraft).thenReturn(mockedMinecraft);
        return clientUtil;
    }
}
