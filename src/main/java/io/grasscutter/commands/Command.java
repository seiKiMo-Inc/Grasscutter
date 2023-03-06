package io.grasscutter.commands;

import io.grasscutter.commands.args.Argument;
import io.grasscutter.commands.args.Arguments;
import io.grasscutter.commands.args.PrefixedArgument;
import io.grasscutter.commands.sender.CommandSender;
import io.grasscutter.utils.enums.CommandExceptionType;
import io.grasscutter.utils.exceptions.CommandException;
import io.grasscutter.utils.objects.text.Text;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.AllArgsConstructor;

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
    public void execute(CommandSender sender, Arguments arguments) {
        sender.sendMessage(this.generateUsage());
    }

    /** Generates a usage message for the command. */
    public Text generateUsage() {
        var data = this.data;
        var builder = new StringBuilder("Usage: ");

        // Check if the command is un-ordered.
        if (!data.isOrdered()) {
            builder.append("(unordered) ");
        }

        // Add the command label.
        builder.append("/").append(data.getLabel());

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
            for (var subCommand : subCommands) argumentsList.add(subCommand.getArguments());
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
            args.forEach(
                    arg -> {
                        var name = arg.name();
                        if (arg instanceof PrefixedArgument prefixArg) {
                            // Change the first letter to uppercase.
                            name = name.substring(0, 1).toUpperCase() + name.substring(1);
                            // Add the prefix.
                            name = prefixArg.prefix() + name;
                        }

                        if (!argNames.contains(name)) argNames.add(name);

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

        return Text.of(builder.toString()).color(Color.YELLOW);
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
                executing =
                        subCommands.stream()
                                .filter(sc -> sc.getLabel().equals(arguments.get(0)))
                                .findFirst()
                                .orElse(null);
                if (executing == null) executing = this; // Not a sub-command.
                else arguments.remove(0); // Remove the sub-command argument.
            }

            // Perform executing checks.
            this.checkPermission(sender, executing); // Check permission.
            var args = this.checkArguments(sender, executing, arguments); // Check arguments.

            if (executing == this) executing.execute(sender, args); // Execute the command.
            else executing.tryExecute(sender, arguments); // Try to execute the sub-command.
        } catch (CommandException exception) {
            throw exception; // Re-throw the exception.
        } catch (InterruptedException ignored) {
            // Do nothing.
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
    private void checkPermission(CommandSender sender, Command command) throws CommandException {
        if (!sender.hasPermission(command.getPermission()))
            throw new CommandException(CommandExceptionType.INVALID_PERMISSION);
    }

    /**
     * Checks if the arguments are valid.
     *
     * @param sender The sender of the command.
     * @param command The command to be executed.
     * @param supplied The command arguments.
     * @return A container of parsed arguments.
     * @throws CommandException If the arguments are invalid.
     * @throws InterruptedException If the command was interrupted.
     */
    private Arguments checkArguments(CommandSender sender, Command command, List<String> supplied)
            throws CommandException, InterruptedException {
        var arguments = command.getArguments();

        // Check for required arguments.
        var required = arguments.stream().filter(arg -> !arg.optional()).count();
        if (arguments.size() > 0 && supplied.size() < required) {
            // Check if the command is ordered.
            if (!command.data.isOrdered()) {
                sender.sendMessage(this.generateUsage());
                throw new InterruptedException();
            }

            throw new CommandException(CommandExceptionType.INVALID_ARGUMENT)
                    .setArgs(new Object[] {command.getLabel()});
        }

        // Check for argument types.
        var args = new Arguments();
        if (command.data.isOrdered()) {
            for (var i = 0; i < arguments.size(); i++) {
                if (i >= supplied.size()) break; // No more arguments to check.
                var value = supplied.get(i); // The argument value.
                var argument = arguments.get(i); // The argument.
                var type = argument.type(); // The argument type.

                // Check the argument type.
                this.parseArgument(argument, value, type, args);
            }
        } else {
            // Check if the arguments are all prefixed.
            if (!arguments.stream().allMatch(arg -> arg instanceof PrefixedArgument))
                throw new CommandException(CommandExceptionType.INVALID_ARGUMENT)
                        .setArgs(new Object[] {command.getLabel()});

            // Split the arguments into key-value pairs.
            for (var prefixedValue : supplied) {
                // Check if the argument starts with an argument prefix.
                var argument =
                        arguments.stream()
                                .filter(arg -> arg instanceof PrefixedArgument)
                                .map(arg -> (PrefixedArgument) arg)
                                .filter(arg -> prefixedValue.startsWith(arg.prefix()))
                                .findFirst()
                                .orElse(null);
                if (argument == null) continue; // Not a prefixed argument.

                // Get the argument value.
                var argValue = prefixedValue.substring(argument.prefix().length());
                if (argValue.isEmpty()) continue; // No value provided.
                // Check the argument type.
                this.parseArgument(argument, argValue, argument.type(), args);
            }
        }

        return args;
    }

    /**
     * Parses the argument value.
     *
     * @param argument The argument.
     * @param value The argument value.
     * @param type The argument type.
     * @param args The container for arguments.
     * @throws CommandException If the argument value is invalid.
     */
    private void parseArgument(Argument argument, String value, Class<?> type, Arguments args)
            throws CommandException {
        if (type == String.class) {
            args.put(argument.name(), value);
        } else if (type == Integer.class) {
            try {
                args.put(argument.name(), Integer.parseInt(value));
            } catch (NumberFormatException exception) {
                throw new CommandException(CommandExceptionType.INVALID_ARGUMENT)
                        .setArgs(new Object[] {argument.name()});
            }
        } else if (type == Long.class) {
            try {
                args.put(argument.name(), Long.parseLong(value));
            } catch (NumberFormatException exception) {
                throw new CommandException(CommandExceptionType.INVALID_ARGUMENT)
                        .setArgs(new Object[] {argument.name()});
            }
        } else if (type == Float.class) {
            try {
                args.put(argument.name(), Float.parseFloat(value));
            } catch (NumberFormatException exception) {
                throw new CommandException(CommandExceptionType.INVALID_ARGUMENT)
                        .setArgs(new Object[] {argument.name()});
            }
        } else if (type == Double.class) {
            try {
                args.put(argument.name(), Double.parseDouble(value));
            } catch (NumberFormatException exception) {
                throw new CommandException(CommandExceptionType.INVALID_ARGUMENT)
                        .setArgs(new Object[] {argument.name()});
            }
        } else if (type == Boolean.class) {
            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                args.put(argument.name(), Boolean.parseBoolean(value));
            } else {
                throw new CommandException(CommandExceptionType.INVALID_ARGUMENT)
                        .setArgs(new Object[] {argument.name()});
            }
        }
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
