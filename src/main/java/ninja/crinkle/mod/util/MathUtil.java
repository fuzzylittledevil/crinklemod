package ninja.crinkle.mod.util;

public class MathUtil {
    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(min, value), max);
    }
}
