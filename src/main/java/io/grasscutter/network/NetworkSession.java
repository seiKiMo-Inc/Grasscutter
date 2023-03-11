package io.grasscutter.network;

import io.grasscutter.network.kcp.KcpHandler;
import io.grasscutter.network.kcp.KcpTunnel;
import io.grasscutter.network.protocol.BasePacket;
import lombok.Getter;

import kcp.highway.Ukcp;

/** An internal KCP-client wrapper. Serves as the interface to a client. */
public final class NetworkSession extends KcpTunnel implements KcpHandler {
    @Getter private long lastPing = System.currentTimeMillis();

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

    /*
     * Listener methods.
     */

    @Override
    public void onConnect() {
        System.out.println("Connected!");
    }

    @Override
    public void onDisconnect() {
        System.out.println("Disconnected!");
    }

    @Override
    public void onMessage(byte[] data) {
        System.out.println("Received: " + new String(data).subSequence(0, 9));
    }
}
