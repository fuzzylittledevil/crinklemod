package ninja.crinkle.mod.util;

import org.jetbrains.annotations.NotNull;

public class StringUtil {
    public static @NotNull String snake(@NotNull String s) {
        return s.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
    }
}
