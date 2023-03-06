package io.grasscutter.commands;

import io.grasscutter.commands.sender.CommandSender;
import io.grasscutter.utils.enums.CommandExceptionType;
import io.grasscutter.utils.exceptions.CommandException;
import io.grasscutter.utils.objects.text.Text;
import lombok.AllArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/* Command instance. */
@AllArgsConstructor
public abstract class Command {
    private final CommandData data;

    /**
     * Invoked when the command is executed.
     *
     * @param sender The sender of the command.
     * @param arguments The command arguments.
     */
    public void execute(CommandSender sender, List<String> arguments) {
        sender.sendMessage(this.generateUsage());
    }

    /**
     * Generates a usage message for the command.
     */
    public Text generateUsage() {
        var data = this.data;
        var builder = new StringBuilder("Usage: /")
                .append(data.getLabel());

        // Check for sub commands.
        var subCommands = data.getSubCommands();
        if (subCommands != null && subCommands.size() > 0) {
            builder.append(" {");
            for (var i = 0; i < subCommands.size(); i++) {
                var subCommand = subCommands.get(i);
                builder.append(subCommand.getLabel());
                if (i != subCommands.size() - 1) builder.append("|");
            }
            builder.append("}");
        }

        // Check for arguments.
        var arguments = new HashMap<Integer, List<Argument>>();

        // Add arguments.
        var argumentsList = new ArrayList<List<Argument>>();
        argumentsList.add(data.getArguments());
        if (subCommands != null) {
            for (var subCommand : subCommands)
                argumentsList.add(subCommand.getArguments());
        }

        // Add arguments to map.
        for (var i = 0; i < argumentsList.size(); i++) {
            // Add a list to the map.
            arguments.put(i, new ArrayList<>());
            // Add arguments to the list.
            for (var args : argumentsList) {
                if (args.size() <= i) continue;
                var thisArg = args.get(i);
                if (thisArg == null) continue;
                arguments.get(i).add(thisArg);
            }
        }

        // Parse the arguments into the usage message.
        for (var i = 0; i < arguments.size(); i++) {
            var args = arguments.get(i);
            var argNames = new ArrayList<>();
            var optional = new AtomicBoolean(false);
            var mixed = new AtomicBoolean(false);

            // Add the names of arguments.
            args.forEach(arg -> {
                if (!argNames.contains(arg.name()))
                    argNames.add(arg.name());

                // Check if the argument is optional.
                if (arg.optional()) optional.set(true);
                // Check if the argument is mixed.
                else if (optional.get()) mixed.set(true);
            });

            // Add the arguments to the usage message.
            if (mixed.get()) builder.append(" (");
            else if (optional.get()) builder.append(" [");
            else builder.append(" <");

            // Add the argument names.
            for (var j = 0; j < argNames.size(); j++) {
                builder.append(argNames.get(j));
                if (j != argNames.size() - 1) builder.append("|");
            }

            // Add the closing brackets.
            if (mixed.get()) builder.append(")");
            else if (optional.get()) builder.append("]");
            else builder.append(">");
        }

        return Text.of(builder.toString())
                .color(Color.YELLOW);
    }

    /**
     * Attempts to invoke the command.
     *
     * @param sender The sender of the command.
     * @param arguments The command arguments.
     * @throws CommandException If the command could not be executed.
     */
    public final void tryExecute(CommandSender sender, List<String> arguments)
            throws CommandException {
        try {
            var executing = this; // Command to be executed.

            // Check if arguments were provided.
            // Check if there are sub-commands.
            var subCommands = this.data.getSubCommands();
            if (arguments.size() != 0 && subCommands.size() != 0) {
                // Check if the first argument is a sub-command.
                executing = subCommands.stream()
                        .filter(sc -> sc.getLabel().equals(arguments.get(0)))
                        .findFirst().orElse(null);
                if (executing == null) executing = this; // Not a sub-command.
                else arguments.remove(0); // Remove the sub-command argument.
            }

            // Perform executing checks.
            this.checkPermission(sender, executing); // Check permission.
            this.checkArguments(sender, executing, arguments); // Check arguments.

            if (executing == this) executing.execute(sender, arguments); // Execute the command.
            else executing.tryExecute(sender, arguments); // Try to execute the sub-command.
        } catch (CommandException exception) {
            throw exception; // Re-throw the exception.
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new CommandException(CommandExceptionType.ERROR_EXECUTING);
        }
    }

    /**
     * Checks if the sender has permission to execute the command.
     *
     * @param sender The sender of the command.
     * @param command The command to be executed.
     * @throws CommandException If the sender does not have permission.
     */
    private void checkPermission(CommandSender sender, Command command)
            throws CommandException {
        if (!sender.hasPermission(command.getPermission()))
            throw new CommandException(CommandExceptionType.INVALID_PERMISSION);
    }

    /**
     * Checks if the arguments are valid.
     *
     * @param sender The sender of the command.
     * @param command The command to be executed.
     * @param arguments The command arguments.
     * @throws CommandException If the arguments are invalid.
     */
    private void checkArguments(CommandSender sender, Command command, List<String> arguments)
            throws CommandException {

    }

    /*
     * Getter methods.
     */

    /**
     * Returns the command label.
     *
     * @return The command label.
     */
    public final String getLabel() {
        return this.data.getLabel();
    }

    /**
     * Returns the command description.
     *
     * @return The command description.
     */
    public final String getDescription() {
        return this.data.getDescription();
    }

    /**
     * Returns the command permission.
     *
     * @return The command permission.
     */
    public final String getPermission() {
        return this.data.getPermission();
    }

    /**
     * Returns the command arguments.
     *
     * @return The command arguments.
     */
    List<Argument> getArguments() {
        return this.data.getArguments();
    }
}
