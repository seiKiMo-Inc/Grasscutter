package io.grasscutter.commands.args;

import lombok.Getter;
import lombok.experimental.Accessors;

/* Argument data. */
@Accessors(fluent = true)
@Getter
public class Argument {
    private final String name;
    private final boolean optional;
    private final Class<?> type;

    public Argument(String name, boolean optional, Class<?> type) {
        this.name = name;
        this.optional = optional;
        this.type = type;
    }
}
