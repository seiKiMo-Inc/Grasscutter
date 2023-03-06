package io.grasscutter.commands.defaults;

import io.grasscutter.commands.Command;
import io.grasscutter.commands.CommandData;
import io.grasscutter.commands.args.Arguments;
import io.grasscutter.commands.sender.CommandSender;
import io.grasscutter.server.DedicatedServer;

/* Reloads the server. */
public final class ReloadCommand extends Command {
    public ReloadCommand() {
        super(
                CommandData.builder()
                        .label("reload")
                        .description("Reloads the server.")
                        .permission("grasscutter.reload")
                        .build());
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        DedicatedServer.getInstance().reload(); // Perform the reload.
    }
}
