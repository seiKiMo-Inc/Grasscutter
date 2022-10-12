package io.grasscutter.utils;

import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.Packet;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.utils.enums.KeyType;
import java.net.InetSocketAddress;
import java.util.Arrays;

/* Utility methods seen in server networking. */
public interface NetworkUtils {
    /**
     * Identifies the packet ID for sending the packet.
     *
     * @param packetClass The packet class.
     * @return A packet ID in {@link PacketIds}.
     */
    static int getSendIdOf(Class<? extends BasePacket<?, ?>> packetClass) {
        return Arrays.stream(Packet.values())
                .filter(packet -> packet.getPacket() == packetClass)
                .findFirst()
                .orElse(Packet.NONE)
                .getSendId();
    }

    /**
     * Performs an XOR bitwise operation using the provided key.
     *
     * @param buffer The buffer to encrypt.
     * @param keyType The key type to use.
     */
    static void encryptBuffer(byte[] buffer, KeyType keyType) {
        var key = keyType.getKey();
        NetworkUtils.encryptBuffer(buffer, key);
    }

    /**
     * Performs an XOR bitwise operation on the given byte array.
     *
     * @param buffer The buffer to XOR.
     * @param key The key to XOR with.
     */
    static void encryptBuffer(byte[] buffer, byte[] key) {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] ^= key[i % key.length];
        }
    }

    /**
     * Creates an {@link InetSocketAddress} from the given host and port.
     *
     * @param address The address to parse.
     * @param port The port to parse.
     * @return The parsed address.
     */
    static InetSocketAddress createFrom(String address, int port) {
        return new InetSocketAddress(address, port);
    }

    /**
     * Validates a given host and port.
     *
     * @param address The address to validate.
     * @param port The port to validate.
     * @return Whether the address and port are valid.
     */
    static boolean validate(String address, int port) {
        return address != null && !address.isEmpty() && port > 0 && port < 65536;
    }
}
