package ninja.crinkle.mod.client.color;

import net.minecraft.ChatFormatting;
import ninja.crinkle.mod.util.MathUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record Color(int color) {
    public static final Color WHITE = new Color(0xFFFFFFFF);
    public static final Color BLACK = new Color(0xFF000000);
    public static final Color TRANSPARENT = new Color(0);
    public static final Color DEFAULT_TEXT = new Color(0xFF404040);

    public static Color of(ChatFormatting formatting) {
        return new Color(Optional.ofNullable(formatting.getColor()).orElseThrow());
    }

    public static Color of(int color) {
        return new Color(color);
    }

    public static Color of(double red, double green, double blue) {
        return Color.of(red, green, blue, 255);
    }

    public static Color of(double red, double green, double blue, double alpha) {
        return new Color((int) (alpha * 255) << 24 | (int) (red * 255) << 16 | (int) (green * 255) << 8 | (int) (blue * 255));
    }

    public static Color of(int red, int green, int blue) {
        return Color.of(red, green, blue, 255);
    }

    public static Color of(int red, int green, int blue, int alpha) {
        return new Color(alpha << 24 | red << 16 | green << 8 | blue);
    }

    public static Color of(String hex) {
        if (hex.startsWith("0x")) hex = hex.substring(2);
        if (hex.startsWith("#")) hex = hex.substring(1);
        int color = Integer.parseInt(hex, 16);
        return new Color(color);
    }

    public static int grayscale(int color) {
        int gray = (int) (0.21 * ((color >> 16) & 0xFF) + 0.72 * ((color >> 8) & 0xFF) + 0.07 * (color & 0xFF));
        return (color & 0xFF000000) | (gray << 16) | (gray << 8) | gray;
    }

    public static int brightness(int color, double amount) {
        int r = (int) ((color >> 16 & 0xFF) * amount);
        int g = (int) ((color >> 8 & 0xFF) * amount);
        int b = (int) ((color & 0xFF) * amount);
        return (color & 0xFF000000) | (r << 16) | (g << 8) | b;
    }

    public static int invert(int color) {
        int alpha = color & 0xFF000000;
        int rgb = color & 0x00FFFFFF;
        return alpha | (~rgb & 0x00FFFFFF);
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

    @Contract("_ -> new")
    public @NotNull Color withAlpha(double alpha) {
        int a = (int) (alpha * 255);
        return new Color((color & 0x00FFFFFF) | (a << 24));
    }

    @Contract("_ -> new")
    public @NotNull Color brightness(double amount) {
        return Color.of(brightness(color, amount));
    }

    public Color lerp(Color color, double amount) {
        return Color.of(
                MathUtil.lerp(getRed(), color.getRed(), amount),
                MathUtil.lerp(getGreen(), color.getGreen(), amount),
                MathUtil.lerp(getBlue(), color.getBlue(), amount),
                MathUtil.lerp(getAlpha(), color.getAlpha(), amount)
        );
    }

    public int ABGR() {
        return ((color & 0xFF000000) | ((color & 0x00FF0000) >> 16) | (color & 0x0000FF00) | ((color & 0x000000FF) << 16));
    }
}
