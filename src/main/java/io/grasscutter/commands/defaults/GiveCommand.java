package io.grasscutter.commands.defaults;

import io.grasscutter.commands.Command;
import io.grasscutter.commands.CommandData;
import io.grasscutter.commands.args.Argument;
import io.grasscutter.commands.args.Arguments;
import io.grasscutter.commands.args.PrefixedArgument;
import io.grasscutter.commands.defaults.give.AllSubCommand;
import io.grasscutter.commands.sender.CommandSender;

import java.util.List;

/* Give items to players. */
public final class GiveCommand extends Command {
    public static final Argument TARGET = new PrefixedArgument("target", "@", true, Object.class);
    public static final Argument LEVEL = new PrefixedArgument("level", "lv", true, Integer.class);

    public GiveCommand() {
        super(CommandData.builder()
                .ordered(false)
                .async(true)
                .label("give")
                .description("Give items to players.")
                .permission("grasscutter.give.item")
                .subCommands(List.of(
                        new AllSubCommand()
                ))
                .arguments(List.of(
                        TARGET, LEVEL,
                        new PrefixedArgument("item", "i", false, Long.class)
                ))
                .build());
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        var target = arguments.get("target", sender);
        var level = arguments.get("level", 1);
        var item = arguments.get("item", 0L);

        sender.sendMessage("Gave item " + item + " to " + target.hashCode() + " at level " + level + ".");
    }
}
