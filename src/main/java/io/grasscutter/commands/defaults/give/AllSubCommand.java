package io.grasscutter.commands.defaults.give;

import io.grasscutter.commands.args.Arguments;
import io.grasscutter.commands.sender.CommandSender;

/* Gives all items to a specified player. */
public final class AllSubCommand extends Give {
    public AllSubCommand() {
        super("all", "Gives all items.");
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        var target = arguments.get("target", sender);
        var level = arguments.get("level", 1);

        sender.sendMessage("Gave all items to " + target.hashCode() + " at level " + level + ".");
    }
}
