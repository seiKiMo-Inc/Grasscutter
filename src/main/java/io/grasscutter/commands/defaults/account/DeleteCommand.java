package io.grasscutter.commands.defaults.account;

import io.grasscutter.commands.CommandData;
import io.grasscutter.commands.SubCommand;
import io.grasscutter.commands.args.Argument;
import io.grasscutter.commands.args.Arguments;
import io.grasscutter.commands.sender.CommandSender;
import io.grasscutter.utils.objects.text.Text;
import java.util.List;

/* Deletes an account. */
public final class DeleteCommand extends SubCommand {
    public DeleteCommand() {
        super(
                CommandData.builder()
                        .label("delete")
                        .description("Deletes an account.")
                        .permission("grasscutter.account.delete")
                        .arguments(
                                List.of(
                                        new Argument("username", false, String.class),
                                        new Argument("test", true, String.class)))
                        .build());
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        sender.sendMessage(Text.of("&cDeleted account."));
    }
}
