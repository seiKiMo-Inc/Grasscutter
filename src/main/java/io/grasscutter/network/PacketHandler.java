package io.grasscutter.network;

import com.google.protobuf.InvalidProtocolBufferException;
import io.grasscutter.network.protocol.Packet;
import io.grasscutter.proto.PacketHeadOuterClass.PacketHead;
import io.grasscutter.utils.Preconditions;
import io.grasscutter.utils.exceptions.InvalidException;

import java.util.HashMap;
import java.util.Map;

/* Handles packets. */
public final class PacketHandler {
    /* A map of Packet IDs -> Packet bindings. */
    private static final Map<Integer, Packet> packets = new HashMap<>();

    static {
        // Register all packets.
        for (var packet : Packet.values()) {
            packets.put(packet.getHandleId(), packet);
        }
    }

    /**
     * Handles a packet.
     *
     * @param client The client.
     * @param packetId The packet ID.
     * @param head The packet head.
     * @param data The packet data.
     * @throws InvalidProtocolBufferException If the packet is invalid.
     */
    public static void handlePacket(
            NetworkSession client,
            int packetId, byte[] head, byte[] data
    ) throws InvalidProtocolBufferException {
        // Get the packet from the ID.
        var packet = packets.get(packetId);
        Preconditions.validPacket(packet);

        // Create an instance of the packet.
        var instance = packet.newInstance();
        if (instance == null) {
            throw new InvalidException("exception.packet");
        }

        // Parse & handle the packet.
        instance.handlePacket(client,
                PacketHead.parseFrom(head),
                packet.fromData(data));
    }
}
