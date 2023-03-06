package io.grasscutter.commands.sender;

import io.grasscutter.utils.objects.lang.TextContainer;
import io.grasscutter.utils.objects.text.Text;

/* A type of command sender. */
public interface CommandSender {
    /**
     * Sends a language-driven message to the sender.
     *
     * @param message The message to send.
     */
    void sendMessage(TextContainer message);

    /**
     * Sends a raw, plain-text message to the sender.
     *
     * @param message The message to send.
     */
    void sendMessage(String message);

    /**
     * Sends a rich message to the sender.
     *
     * @param message The message to send.
     */
    void sendMessage(Text message);

    /**
     * Checks whether the sender has a permission.
     *
     * @param permission The permission to check.
     * @return Whether the sender has the permission.
     */
    boolean hasPermission(String permission);
}
