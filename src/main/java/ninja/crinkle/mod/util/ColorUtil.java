package ninja.crinkle.mod.util;

public class ColorUtil {
    public static int darken(int color, float amount) {
        int r = (int) ((color >> 16 & 0xFF) * amount);
        int g = (int) ((color >> 8 & 0xFF) * amount);
        int b = (int) ((color & 0xFF) * amount);
        return (color & 0xFF000000) | (r << 16) | (g << 8) | b;
    }
}
