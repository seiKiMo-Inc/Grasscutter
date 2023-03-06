package io.grasscutter.commands.sender;

import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.objects.lang.TextContainer;
import io.grasscutter.utils.objects.text.Text;

/* Implementation of a command sender for the console. */
public final class ConsoleCommandSender implements CommandSender {
    @Override
    public void sendMessage(TextContainer message) {
        Log.info(message);
    }

    @Override
    public void sendMessage(String message) {
        Log.info(message);
    }

    @Override
    public void sendMessage(Text message) {
        Log.info(message.toString(true));
    }

    @Override
    public boolean hasPermission(String permission) {
        return true; // Console has all permissions.
    }
}
