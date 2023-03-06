package io.grasscutter.commands.defaults;

import io.grasscutter.commands.Command;
import io.grasscutter.commands.CommandData;
import io.grasscutter.commands.defaults.account.*;

import java.util.List;

/* Account management. */
public final class AccountCommand extends Command {
    public AccountCommand() {
        super(CommandData.builder()
                .label("account")
                .description("Account management.")
                .subCommands(List.of(
                        new CreateCommand(),
                        new DeleteCommand()
                ))
                .build());
    }
}
