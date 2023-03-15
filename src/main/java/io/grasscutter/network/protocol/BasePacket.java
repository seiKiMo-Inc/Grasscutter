package io.grasscutter.network.protocol;

import com.google.protobuf.GeneratedMessageV3;
import io.grasscutter.network.NetworkSession;
import io.grasscutter.proto.PacketHeadOuterClass.PacketHead;
import io.grasscutter.utils.CryptoUtils;
import io.grasscutter.utils.NetworkUtils;
import io.grasscutter.utils.constants.NetworkConstants;
import io.grasscutter.utils.enums.KeyType;
import io.grasscutter.utils.objects.Buffer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a packet handler. Can be sent over the network. Can be handled from a client packet.
 *
 * @param <I> The input packet. (handling)
 * @param <O> The output packet. (sending)
 */
public abstract class BasePacket<I extends GeneratedMessageV3, O extends GeneratedMessageV3> {
    public BasePacket() {
        // Empty constructor.
    }

    /*
     * Receiving.
     */

    /**
     * Handles an incoming packet.
     *
     * @param session The session sending the packet.
     * @param header The packet's header.
     * @param message The message to handle.
     */
    @SuppressWarnings("unchecked")
    public final void handlePacket(NetworkSession session, PacketHead header, Object message) {
        this.handlePacket(session, header, (I) message);
    }

    /**
     * Handles an incoming packet.
     *
     * @param session The session sending the packet.
     * @param header The packet's header.
     * @param message The message to handle.
     */
    protected void handlePacket(NetworkSession session, PacketHead header, I message) {}

    /*
     * Sending.
     */

    @Getter private boolean shouldBuildHeader = false;
    @Getter private PacketHead packetHeader = null;

    @Setter(AccessLevel.PROTECTED)
    private boolean shouldEncrypt = true;

    @Setter(AccessLevel.PROTECTED)
    private KeyType keyType = null;

    @Setter(AccessLevel.PROTECTED)
    private int otherPacketId = -1;

    /**
     * Calculates the size of the encoded packet.
     *
     * @return A size in bytes.
     */
    private int getBufferSize(byte[] header, byte[] data) {
        return 2 + 2 + 2 + 4 + header.length + data.length + 2;
    }

    /**
     * Creates a packet header. Builds it with the provided sequence ID.
     *
     * @param clientSequence The client sequence ID.
     */
    public final void buildHeaderWith(int clientSequence) {
        this.packetHeader =
                PacketHead.newBuilder()
                        .setClientSequenceId(clientSequence)
                        .setSentMs(System.currentTimeMillis())
                        .build();
    }

    /** Enables building a packet header on send. */
    public final void buildHeader() {
        this.shouldBuildHeader = true;
    }

    /**
     * Encodes this packet for sending.
     *
     * @param session The session to send the packet to.
     * @return The encoded packet.
     */
    public final byte[] encode(NetworkSession session) {
        // Encode data into buffers.
        var packet = this.preparePacket();
        var data = packet == null ? new byte[0] : packet.toByteArray();
        var header = this.packetHeader == null ? new byte[0] : this.packetHeader.toByteArray();

        // Get the packet's ID.
        var packetId = this.otherPacketId == -1 ? NetworkUtils.getIdOf(this) : this.otherPacketId;

        // Construct packet.
        var buffer =
                new Buffer(this.getBufferSize(header, data))
                        .writeUint16(NetworkConstants.MAGIC_1)
                        .writeUint16(packetId)
                        .writeUint16(header.length)
                        .writeUint32(data.length)
                        .writeBytes(header)
                        .writeBytes(data)
                        .writeUint16(NetworkConstants.MAGIC_2)
                        .finish();

        // Encrypt packet with XOR if needed.
        if (this.shouldEncrypt) {
            // Get the key type.
            var key =
                    this.keyType == null
                            ? session.isEncrypted() ? KeyType.ENCRYPT : KeyType.DISPATCH
                            : this.keyType;
            CryptoUtils.performXor(buffer, key);
        }

        return buffer;
    }

    /**
     * Prepares the packet for sending. Should use data provided in a constructor.
     *
     * @return An instance of {@link O} to encode and send.
     */
    public O preparePacket() {
        return null;
    }
}
