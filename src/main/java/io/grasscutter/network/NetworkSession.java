package io.grasscutter.network;

import com.google.protobuf.InvalidProtocolBufferException;
import io.grasscutter.player.Account;
import io.grasscutter.network.kcp.KcpHandler;
import io.grasscutter.network.kcp.KcpTunnel;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.player.Player;
import io.grasscutter.utils.CryptoUtils;
import io.grasscutter.utils.NetworkUtils;
import io.grasscutter.utils.Preconditions;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.enums.KeyType;
import io.grasscutter.utils.exceptions.InvalidException;
import io.grasscutter.utils.objects.lang.TextContainer;
import io.grasscutter.utils.objects.text.Text;
import io.netty.buffer.Unpooled;
import lombok.Getter;

import kcp.highway.Ukcp;
import lombok.Setter;

/** An internal KCP-client wrapper. Serves as the interface to a client. */
public final class NetworkSession extends KcpTunnel implements KcpHandler {
    @Getter private long lastPing = System.currentTimeMillis();
    @Setter @Getter private boolean encrypted = false;

    @Setter @Getter private Account account = null;
    @Setter @Getter private Player player = null;

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
    @SuppressWarnings("unchecked")
    public void send(BasePacket<?, ?> packet) {
        // Log the outbound packet.
        var packetId = NetworkUtils.getIdOf(
                (Class<? extends BasePacket<?, ?>>) packet.getClass());
        var name = NetworkUtils.getNameOf(packetId);
        Log.debug(new Text(new TextContainer(
                "network.packet.outbound",
                name, packetId,
                this.getPrettyAddress(true))));

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
     * Formats the client's address.
     *
     * @param withPort Whether to include the port.
     * @return The formatted address.
     */
    public String getPrettyAddress(boolean withPort) {
        var address = this.getAddress();
        return address.getAddress()
                .getHostAddress() +
                (withPort ? ":" + address.getPort() : "");
    }

    /*
     * Listener methods.
     */

    @Override
    public void onConnect() {
        // Log the connection.
        Log.debug(new TextContainer("server.game.client.connected",
                this.getPrettyAddress(true)));
    }

    @Override
    public void onDisconnect() {
        // Log the disconnection.
        Log.debug(new TextContainer("server.game.client.disconnected",
                this.getPrettyAddress(true)));
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

                // Handle the packet.
                PacketHandler.handlePacket(this,
                        packetId, headData, packetData);
            }
        } catch (InvalidException exception) {
            // Log the exception.
            Log.error(exception.getLocalizedMessage());
        } catch (InvalidProtocolBufferException exception) {
            // Log the exception.
            Log.error(new TextContainer("exception.packet"));
        } catch (Exception ignored) {
            // Log the exception.
            Log.error(new TextContainer("server.game.client.error",
                    this.getPrettyAddress(true)));
        }
    }
}
