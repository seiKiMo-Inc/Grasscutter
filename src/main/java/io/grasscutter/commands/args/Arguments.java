package io.grasscutter.commands.args;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/* Container of argument values. */
public final class Arguments {
    @Getter private final Map<String, Object> values = new HashMap<>();

    /**
     * Puts an argument into the container.
     *
     * @param name The name of the argument.
     * @param value The value of the argument.
     */
    public void put(String name, Object value) {
        values.put(name, value);
    }

    /**
     * Gets the value of an argument.
     *
     * @param name The name of the argument.
     * @param <T> The type of the argument.
     * @return The value of the argument.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) values.get(name);
    }

    /**
     * Gets the value of an argument, or a fallback value if the argument is not present.
     *
     * @param name The name of the argument.
     * @param fallback The fallback value.
     * @return The argument's value.
     */
    public <T> T get(String name, T fallback) {
        return values.containsKey(name) ? this.get(name) : fallback;
    }
}
