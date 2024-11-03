package ninja.crinkle.mod.client.gui.themes;

import com.mojang.logging.LogUtils;
import ninja.crinkle.mod.config.ClientConfig;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum ThemeRegistry {
    INSTANCE;

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String DEFAULT_THEME_ID = "default";
    private final Map<String, Theme> themes = new HashMap<>();

    public static Theme current() {
        Theme theme = INSTANCE.getTheme(ClientConfig.themeId());
        if (theme == null) {
            LOGGER.warn("Theme {} not found, using default", ClientConfig.themeId());
            theme = INSTANCE.getTheme(DEFAULT_THEME_ID);
        }
        return theme;
    }

    public static Theme getDefault() {
        return Optional.ofNullable(INSTANCE.getTheme(DEFAULT_THEME_ID)).orElse(Theme.EMPTY);
    }

    public void register(Theme theme) {
        themes.put(theme.getId(), theme);
    }

    public Theme getTheme(String id) {
        return themes.get(id);
    }

    public Map<String, Theme> getThemes() {
        return themes;
    }
}
