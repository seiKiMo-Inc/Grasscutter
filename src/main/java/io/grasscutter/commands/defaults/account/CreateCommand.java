package io.grasscutter.commands.defaults.account;

import io.grasscutter.commands.args.Argument;
import io.grasscutter.commands.args.Arguments;
import io.grasscutter.commands.CommandData;
import io.grasscutter.commands.SubCommand;
import io.grasscutter.commands.sender.CommandSender;
import io.grasscutter.utils.objects.text.Text;

import java.util.List;

/* Deletes an account. */
public final class CreateCommand extends SubCommand {
    public CreateCommand() {
        super(CommandData.builder()
                .label("create")
                .description("Creates an account.")
                .permission("grasscutter.account.create")
                .arguments(List.of(
                        new Argument("username", false, String.class),
                        new Argument("userId", true, Long.class),
                        new Argument("password", true, String.class)
                ))
                .build());
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        sender.sendMessage(Text.of("&aCreated account."));
    }
}
