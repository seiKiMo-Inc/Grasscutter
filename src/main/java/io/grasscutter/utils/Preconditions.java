package io.grasscutter.utils;

import io.grasscutter.utils.constants.NetworkConstants;
import io.grasscutter.utils.exceptions.InvalidException;
import io.netty.buffer.ByteBuf;

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
}
