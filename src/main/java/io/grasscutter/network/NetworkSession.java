package io.grasscutter.network;

import io.grasscutter.network.kcp.KcpHandler;
import io.grasscutter.network.kcp.KcpTunnel;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.utils.CryptoUtils;
import io.grasscutter.utils.EncodingUtils;
import io.grasscutter.utils.Preconditions;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.enums.KeyType;
import io.grasscutter.utils.exceptions.InvalidException;
import io.grasscutter.utils.objects.lang.TextContainer;
import io.netty.buffer.Unpooled;
import lombok.Getter;

import kcp.highway.Ukcp;

/** An internal KCP-client wrapper. Serves as the interface to a client. */
public final class NetworkSession extends KcpTunnel implements KcpHandler {
    @Getter private long lastPing = System.currentTimeMillis();
    @Getter private boolean encrypted = false;

    /**
     * Creates a new network session.
     *
     * @param handle The KCP handle.
     */
    public NetworkSession(Ukcp handle) {
        super(handle);
    }

    /*
     * Network methods.
     */

    /**
     * Sends the specified packet to the client.
     *
     * @param packet The packet to send.
     */
    public void send(BasePacket<?, ?> packet) {
        // Encode the packet into the proper format.
        // Send the encoded byte array to the client.
        super.writeData(packet.encode());
    }

    /**
     * Updates the last ping time of the client.
     *
     * @param timestamp The time to update to.
     */
    public void updateLastPingTime(int timestamp) {
        this.lastPing = timestamp;
    }

    /**
     * Formats the client's address into IP:Port.
     *
     * @return The formatted address.
     */
    public String getPrettyAddress() {
        var address = this.getAddress();
        return address.getAddress()
                .getHostAddress() +
                ":" + address.getPort();
    }

    /*
     * Listener methods.
     */

    @Override
    public void onConnect() {
        // Log the connection.
        Log.debug(new TextContainer("server.game.client.connected",
                this.getPrettyAddress()));
    }

    @Override
    public void onDisconnect() {
        // Log the disconnection.
        Log.debug(new TextContainer("server.game.client.disconnected",
                this.getPrettyAddress()));
    }

    @Override
    public void onMessage(byte[] data) {
        // Decrypt the packet.
        CryptoUtils.performXor(data, this.isEncrypted() ?
                KeyType.ENCRYPT.getKey() : KeyType.DISPATCH.getKey());
        var buffer = Unpooled.wrappedBuffer(data);

        try {
            while (buffer.isReadable()) {
                // Validate the packet.
                if (buffer.readableBytes() < 12) return;
                Preconditions.validPacket(buffer);

                // Read the packet data.
                var packetId = buffer.readShort();
                var headLength = buffer.readShort();
                var packetLength = buffer.readInt();
                var headData = new byte[headLength];
                buffer.readBytes(headData);
                var packetData = new byte[packetLength];
                buffer.readBytes(packetData);

                // Validate the packet.
                Preconditions.validPacket(buffer);

                System.out.println("Packet ID: " + packetId);
                System.out.println("Head Length: " + headLength);
                System.out.println("Packet Length: " + packetLength);
                System.out.println("Head Data: " +
                        new String(EncodingUtils.toBase64(headData)));
                System.out.println("Packet Data: " +
                        new String(EncodingUtils.toBase64(packetData)));
            }
        } catch (InvalidException exception) {
            // Log the exception.
            Log.error(exception.getLocalizedMessage());
        } catch (Exception ignored) {
            // Log the exception.
            Log.error(new TextContainer("server.game.client.error", this.getPrettyAddress()));
        }
    }
}
