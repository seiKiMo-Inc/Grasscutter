package io.grasscutter.commands;

/* Argument data. */
public record Argument(
        String name,
        boolean optional,
        Class<?> type
) { }
