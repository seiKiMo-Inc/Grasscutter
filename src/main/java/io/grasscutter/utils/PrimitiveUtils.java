package io.grasscutter.utils;

/* Utility methods for primitive types. */
public interface PrimitiveUtils {
    /**
     * Sets the first character of a string to lowercase.
     *
     * @param string The string to modify.
     * @return The modified string.
     */
    static String lowerFirst(String string) {
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }
}
