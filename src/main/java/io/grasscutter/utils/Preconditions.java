package io.grasscutter.utils;

import io.grasscutter.network.NetworkSession;
import io.grasscutter.network.protocol.Packet;
import io.grasscutter.player.Player;
import io.grasscutter.utils.constants.NetworkConstants;
import io.grasscutter.utils.exceptions.InvalidException;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Nullable;

/* Collection of validations. */
public interface Preconditions {
    /**
     * Validates a packet's head or foot.
     *
     * @param packet The packet to validate.
     * @throws InvalidException If the packet is invalid.
     */
    static void validPacket(ByteBuf packet) throws InvalidException {
        var part = (int) packet.readShort();
        if (part == NetworkConstants.MAGIC_1 ||
                part == NetworkConstants.MAGIC_2)
            return;

        throw new InvalidException("exception.packet");
    }

    /**
     * Validates a packet.
     *
     * @param packet The packet to validate.
     * @throws InvalidException If the packet is invalid.
     */
    static void validPacket(@Nullable Packet packet) throws InvalidException {
        if (packet == null || packet.getPacket() == null)
            throw new InvalidException("exception.packet");
    }

    /**
     * Validates a player.
     * The player should not be null.
     *
     * @param player The player to validate.
     * @throws InvalidException If the player is invalid.
     */
    static void validPlayer(@Nullable Player player) throws InvalidException {
        if (player == null)
            throw new InvalidException("exception.player");
    }

    /**
     * Validates a session.
     * The session should be logged in with a player and an account.
     *
     * @param session The session to validate.
     * @throws InvalidException If the session is invalid.
     */
    static void loggedIn(NetworkSession session) throws InvalidException {
        if (session == null || session.getPlayer() == null ||
                session.getAccount() == null)
            throw new InvalidException("exception.player");
    }
}
