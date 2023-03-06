package io.grasscutter.commands.args;

import lombok.Getter;
import lombok.experimental.Accessors;

/* Argument data. */
@Accessors(fluent = true)
@Getter
public final class PrefixedArgument extends Argument {
    private final String prefix;

    public PrefixedArgument(String name, String prefix, boolean optional, Class<?> type) {
        super(name, optional, type);

        this.prefix = prefix;
    }
}
