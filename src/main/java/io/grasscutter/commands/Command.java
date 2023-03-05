package io.grasscutter.commands;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/* Command instance. */
@AllArgsConstructor
public abstract class Command {
    private final CommandData data;

    /**
     * Invoked when the command is executed.
     * @param sender The sender of the command. Can be null.
     * @param arguments The command arguments.
     */
    public abstract void execute(@Nullable Object sender, List<String> arguments);

    /*
     * Getter methods.
     */

    /**
     * Returns the command label.
     * @return The command label.
     */
    public final String getLabel() {
        return this.data.getLabel();
    }

    /**
     * Returns the command description.
     * @return The command description.
     */
    public final String getDescription() {
        return this.data.getDescription();
    }

    /**
     * Returns the command usage.
     * @return The command usage.
     */
    public final String getUsage() {
        return this.data.getUsage();
    }
}
