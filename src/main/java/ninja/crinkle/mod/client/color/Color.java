package ninja.crinkle.mod.client.color;

import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class Color {
    public static final Color RED = new Color(0xFFFF0000);
    public static final Color GREEN = new Color(0xFF00FF00);
    public static final Color BLUE = new Color(0xFF0000FF);
    public static final Color CYAN = new Color(0xFF00FFFF);
    public static final Color MAGENTA = new Color(0xFFFF00FF);
    public static final Color YELLOW = new Color(0xFFFFFF00);
    public static final Color ORANGE = new Color(0xFFFFA500);
    public static final Color PINK = new Color(0xFFFFC0CB);
    public static final Color PURPLE = new Color(0xFF800080);
    public static final Color BROWN = new Color(0xFFA52A2A);
    public static final Color WHITE = new Color(0xFFFFFFFF);
    public static final Color BLACK = new Color(0xFF000000);
    // public static final Color TRANSPARENT = new Color(0);
    public static final Color DEFAULT_TEXT = new Color(0xFF404040);
    public static final Color RAINBOW = new Color(-1);
    private final int color;

    public Color(int color) {
        this.color = color;
    }

    public static Color rainbow(long speed, long offset) {
        double hue = (System.currentTimeMillis() + offset) % speed / (double) speed;
        return Color.of(java.awt.Color.HSBtoRGB((float) hue, 1, 1));
    }

    @Contract("_ -> new")
    public static @NotNull Color of(@NotNull ChatFormatting formatting) {
        return new Color(Optional.ofNullable(formatting.getColor()).orElseThrow());
    }

    @Contract("_ -> new")
    public static @NotNull Color of(int color) {
        return new Color(color);
    }

    @Contract("_ -> new")
    public static @NotNull Color ofABGR(int color) {
        return new Color(((color & 0xFF000000) | ((color & 0x00FF0000) >> 16) | (color & 0x0000FF00) | ((color & 0x000000FF) << 16)));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Color of(double red, double green, double blue) {
        return Color.of(red, green, blue, 255);
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull Color of(double red, double green, double blue, double alpha) {
        return new Color((int) (alpha * 255) << 24 | (int) (red * 255) << 16 | (int) (green * 255) << 8 | (int) (blue * 255));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Color of(int red, int green, int blue) {
        return Color.of(red, green, blue, 255);
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull Color of(int red, int green, int blue, int alpha) {
        return new Color(alpha << 24 | red << 16 | green << 8 | blue);
    }

    @Contract("_ -> new")
    public static @NotNull Color of(@NotNull String hex) {
        if (hex.startsWith("0x")) hex = hex.substring(2);
        if (hex.startsWith("#")) hex = hex.substring(1);
        int color = Integer.parseInt(hex, 16);
        return new Color(color).withAlpha(1);
    }

    public static int brightness(int color, double amount) {
        int r = (int) ((color >> 16 & 0xFF) * amount);
        int g = (int) ((color >> 8 & 0xFF) * amount);
        int b = (int) ((color & 0xFF) * amount);
        return (color & 0xFF000000) | (r << 16) | (g << 8) | b;
    }

    public double getRed() {
        return (color >> 16 & 0xFF) / 255.0;
    }

    public double getGreen() {
        return (color >> 8 & 0xFF) / 255.0;
    }

    public double getBlue() {
        return (color & 0xFF) / 255.0;
    }

    public double getAlpha() {
        return (color >> 24 & 0xFF) / 255.0;
    }

    public Color halftone() {
        return new Color((color & 0xFF000000) | ((color >> 1) & 0x7F7F7F));
    }

    public Color inverted() {
        return new Color((color & 0xFF000000) | (~color & 0xFFFFFF));
    }

    @Contract("_ -> new")
    public @NotNull Color withAlpha(double alpha) {
        int a = (int) (alpha * 255);
        return new Color((color & 0x00FFFFFF) | (a << 24));
    }

    public int ABGR() {
        return ((color & 0xFF000000) | ((color & 0x00FF0000) >> 16) | (color & 0x0000FF00) | ((color & 0x000000FF) << 16));
    }

    public int color() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Color) obj;
        return this.color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }

    @Override
    public String toString() {
        return "Color[" +
                "color=" + color + ']';
    }

    public Color get() {
        if (this == RAINBOW) return Color.rainbow(1000, 0);
        return this;
    }
}
