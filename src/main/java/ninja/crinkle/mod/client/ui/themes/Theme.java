package ninja.crinkle.mod.client.ui.themes;

import ninja.crinkle.mod.client.color.Color;

import java.util.Map;

public class Theme {
    public static final Theme DEFAULT = new Theme(
            Map.of(
                    BoxTheme.Size.SMALL, BoxTheme.GRAY_SMALL,
                    BoxTheme.Size.MEDIUM, BoxTheme.GRAY_MEDIUM,
                    BoxTheme.Size.LARGE, BoxTheme.GRAY_LARGE
            ),
            Color.of(0xFFD7AEFF),
            Color.WHITE,
            Color.of("#AA7FD6"),
            Color.of("#4C4E52"));
    private final Map<BoxTheme.Size, BoxTheme> borderThemes;
    private final Color backgroundColor;
    private final Color foregroundColor;
    private final Color secondaryColor;
    private final Color inactiveColor;

    public Theme(Map<BoxTheme.Size, BoxTheme> borderThemes, Color backgroundColor, Color foregroundColor, Color secondaryColor, Color inactiveColor) {
        this.borderThemes = borderThemes;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.secondaryColor = secondaryColor;
        this.inactiveColor = inactiveColor;
    }

    public BoxTheme getBorderTheme(BoxTheme.Size size) {
        return borderThemes.get(size);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public Color getInactiveColor() {
        return inactiveColor;
    }
}
