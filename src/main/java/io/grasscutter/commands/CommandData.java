package io.grasscutter.commands;

import io.grasscutter.commands.args.Argument;
import io.grasscutter.utils.enums.Executor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/* Internal data for a command. */
@Builder @Getter
public final class CommandData {
    private final String label;
    @Builder.Default private String description = "";
    @Builder.Default private String permission = "";
    @Builder.Default private List<String> aliases = List.of();
    @Builder.Default private boolean async = false;

    @Builder.Default private List<SubCommand> subCommands = List.of();
    @Builder.Default private List<Argument> arguments = List.of();
    @Builder.Default private Executor executor = Executor.ALL;
    @Builder.Default private boolean ordered = true;
}
