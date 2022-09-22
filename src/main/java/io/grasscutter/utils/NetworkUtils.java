package io.grasscutter.utils;

import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.Packet;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.utils.enums.KeyType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.Arrays;

/* Utility methods seen in server networking. */
public interface NetworkUtils {
    /**
     * Identifies the packet ID for sending the packet.
     * @param packetClass The packet class.
     * @return A packet ID in {@link PacketIds}.
     */
    static int getSendIdOf(Class<? extends BasePacket<?, ?>> packetClass) {
        return Arrays.stream(Packet.values())
                .filter(packet -> packet.getPacket() == packetClass)
                .findFirst().orElse(Packet.NONE)
                .getSendId();
    }

    /**
     * Performs an XOR bitwise operation using the provided key.
     * @param buffer The buffer to encrypt.
     * @param keyType The key type to use.
     */
    static void encryptBuffer(byte[] buffer, KeyType keyType) {
        var key = keyType.getKey();
        NetworkUtils.encryptBuffer(buffer, key);
    }

    /**
     * Performs an XOR bitwise operation on the given byte array.
     * @param buffer The buffer to XOR.
     * @param key The key to XOR with.
     */
    static void encryptBuffer(byte[] buffer, byte[] key) {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] ^= key[i % key.length];
        }
    }

    /**
     * Converts a Netty {@link ByteBuf} into a plain byte array.
     * @param buffer The buffer to convert.
     * @return The converted buffer.
     */
    static byte[] toByteArray(ByteBuf buffer) {
        var bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes); return bytes;
    }

    /**
     * Converts a byte array into a Netty {@link ByteBuf}.
     * @param bytes The bytes to convert.
     * @return The converted bytes.
     */
    static ByteBuf toBuffer(byte[] bytes) {
        var buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes(bytes); return buffer;
    }
}