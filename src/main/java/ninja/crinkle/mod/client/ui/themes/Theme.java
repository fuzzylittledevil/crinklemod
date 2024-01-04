package ninja.crinkle.mod.client.ui.themes;

import ninja.crinkle.mod.client.color.Color;

import java.util.Map;

public class Theme {
    public static final Theme DEFAULT = new Theme(
            Map.of(
                    BoxTheme.Type.BUTTON, BoxTheme.BUTTON,
                    BoxTheme.Type.PANEL, BoxTheme.PANEL,
                    BoxTheme.Type.CHECKBOX, BoxTheme.CHECKBOX
            ),
            Color.of(0xFFD7AEFF),
            Color.WHITE,
            Color.of("#AA7FD6"),
            Color.of("#4C4E52"),
            Color.of("#00FF00"),
            Color.of("#FFFF00"),
            Color.of("#FF0000")
    );
    private final Map<BoxTheme.Type, BoxTheme> borderThemes;
    private final Color backgroundColor;
    private final Color foregroundColor;
    private final Color secondaryColor;
    private final Color inactiveColor;
    private final Color successColor;
    private final Color warningColor;
    private final Color errorColor;

    public Theme(Map<BoxTheme.Type, BoxTheme> borderThemes, Color backgroundColor, Color foregroundColor,
                 Color secondaryColor, Color inactiveColor, Color successColor, Color warningColor, Color errorColor) {
        this.borderThemes = borderThemes;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.secondaryColor = secondaryColor;
        this.inactiveColor = inactiveColor;
        this.successColor = successColor;
        this.warningColor = warningColor;
        this.errorColor = errorColor;
    }

    public BoxTheme getBorderTheme(BoxTheme.Type type) {
        return borderThemes.get(type);
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

    public Color getSuccessColor() {
        return successColor;
    }

    public Color getWarningColor() {
        return warningColor;
    }

    public Color getErrorColor() {
        return errorColor;
    }
}
