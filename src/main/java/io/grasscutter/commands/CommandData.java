package io.grasscutter.commands;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/* Internal data for a command. */
@Builder @Getter
public final class CommandData {
    private final String label;
    @Builder.Default private String description = "";
    @Builder.Default private String usage = "";
    @Builder.Default private String permission = "";
    @Builder.Default private List<String> aliases = List.of();
}
