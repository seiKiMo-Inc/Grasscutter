package io.grasscutter.commands.defaults.account;

import io.grasscutter.player.Account;
import io.grasscutter.commands.CommandData;
import io.grasscutter.commands.SubCommand;
import io.grasscutter.commands.args.Argument;
import io.grasscutter.commands.args.Arguments;
import io.grasscutter.commands.sender.CommandSender;
import io.grasscutter.utils.CryptoUtils;
import io.grasscutter.utils.DatabaseUtils;
import io.grasscutter.utils.objects.lang.TextContainer;
import io.grasscutter.utils.objects.text.Text;

import java.awt.*;
import java.util.List;

/* Deletes an account. */
public final class CreateCommand extends SubCommand {
    public CreateCommand() {
        super(
                CommandData.builder()
                        .label("create")
                        .description("Creates an account.")
                        .permission("grasscutter.account.create")
                        .arguments(
                                List.of(
                                        new Argument("username", false, String.class),
                                        new Argument("userId", true, Integer.class),
                                        new Argument("password", true, String.class)))
                        .build());
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        var username = arguments.get("username", "");
        var userId = arguments.get("userId", -1);
        var password = arguments.get("password", "");

        // Check if the account already exists.
        if (DatabaseUtils.fetchAccount(username) != null) {
            sender.sendMessage(new Text(new TextContainer(
                    "command.account.username_taken", username)).color(Color.RED));
            return;
        }
        if (DatabaseUtils.fetchAccountByUid(userId) != null) {
            sender.sendMessage(new Text(new TextContainer(
                    "command.account.user_id_taken", userId)).color(Color.RED));
            return;
        }

        // Check for a valid userId.
        while (userId == -1) {
            userId = CryptoUtils.randomNumber(100000000, 999999999);
            if (DatabaseUtils.fetchAccountByUid(userId) != null) userId = -1;
        }

        // Create the account.
        var account = new Account();
        account.id = DatabaseUtils.fetchAndIncrement("accounts");
        account.password = CryptoUtils.md5Hash(password);
        account.username = username;
        account.gameUserId = userId;
        // Save the account.
        account.save();

        sender.sendMessage(Text.of("Account %s with ID %s created."
                .formatted(account.username, account.gameUserId))
                .color(Color.GREEN));
    }
}
