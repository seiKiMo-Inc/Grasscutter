package io.grasscutter.commands;

/* Implementation of a secondary command. */
public abstract class SubCommand extends Command {
    public SubCommand(CommandData data) {
        super(data);
    }
}
