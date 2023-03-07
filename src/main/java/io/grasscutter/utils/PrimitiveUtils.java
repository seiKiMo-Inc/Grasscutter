package io.grasscutter.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/* Utility methods for primitive types. */
public interface PrimitiveUtils {
    /* Primitive parsers. */
    Function<String, Object> STRING_PARSER = value -> value;
    Function<String, Object> INTEGER_PARSER = value -> (int) Double.parseDouble(value);
    Function<String, Object> LONG_PARSER = value -> (long) Double.parseDouble(value);

    Map<Type, Function<String, Object>> PRIMITIVE_PARSERS = Map.ofEntries(
            Map.entry(String.class, STRING_PARSER),
            Map.entry(Integer.class, INTEGER_PARSER),
            Map.entry(Long.class, LONG_PARSER),
            Map.entry(Float.class, Float::parseFloat),
            Map.entry(Double.class, Double::parseDouble),
            Map.entry(Boolean.class, Boolean::parseBoolean),
            Map.entry(int.class, INTEGER_PARSER),
            Map.entry(long.class, LONG_PARSER),
            Map.entry(float.class, Float::parseFloat),
            Map.entry(double.class, Double::parseDouble),
            Map.entry(boolean.class, Boolean::parseBoolean)
    );

    /**
     * Sets the first character of a string to lowercase.
     *
     * @param string The string to modify.
     * @return The modified string.
     */
    static String lowerFirst(String string) {
        return string.substring(0, 1).toLowerCase() + string.substring(1);
    }

    /**
     * Returns a string parser for the given type.
     * @param type The type to get a parser for.
     * @return The parser.
     */
    static Function<String, Object> getParser(Type type) {
        return PRIMITIVE_PARSERS.get(type);
    }

    /**
     * Splits a string by a separator.
     * Does not perform a Regex split.
     *
     * @param string The string to split.
     * @param separator The separator to split by.
     * @return The split string.
     */
    static List<String> split(String string, int separator) {
        var output = new ArrayList<String>(); var start = 0;

        // Separate the string by the separator.
        for (var next = string.indexOf(separator);
             next > 0;
             next = string.indexOf(separator, start)) {
            // Add the substring to the output.
            output.add(string.substring(start, next));
            start = next + 1; // Move the start to the next character.
        }

        // Add the last substring to the output.
        if (start < string.length())
            output.add(string.substring(start));

        return output;
    }
}
