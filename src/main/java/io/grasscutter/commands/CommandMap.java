package io.grasscutter.commands;

import io.grasscutter.commands.defaults.*;

import io.grasscutter.server.DedicatedServer;
import io.grasscutter.utils.enums.CommandExceptionType;
import io.grasscutter.utils.exceptions.CommandException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Maps command inputs -> command instances. */
public final class CommandMap {
    private static final Map<String, Command> commands = new HashMap<>();

    /**
     * Retrieves the instance of the command map.
     *
     * @return The instance of the command map.
     */
    public static CommandMap getInstance() {
        return DedicatedServer.getInstance().getCommandMap();
    }

    /**
     * Creates a new command map.
     * Registers default commands.
     */
    public CommandMap() {
        this.register(new ReloadCommand());
        this.register(new AccountCommand());
    }

    /**
     * Registers a command.
     *
     * @param command The command to register.
     */
    public void register(Command command) {
        commands.put(command.getLabel(), command);
    }

    /**
     * Executes a command.
     * No sender was specified.
     *
     * @param label The command label.
     * @param arguments The command arguments.
     */
    public void execute(String label, List<String> arguments) {
        // Get the command instance.
        var command = commands.get(label);
        if (command == null)
            throw new CommandException(CommandExceptionType.NOT_FOUND);

        // Execute the command.
        command.tryExecute(DedicatedServer.getInstance()
                .getServerSender(), arguments);
    }
}
