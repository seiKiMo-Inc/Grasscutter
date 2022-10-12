package io.grasscutter.network;

import io.grasscutter.network.kcp.KcpChannel;
import io.grasscutter.network.protocol.BasePacket;
import lombok.Getter;

/** An internal KCP-client wrapper. Serves as the interface to a client. */
public final class NetworkSession extends KcpChannel {
    @Getter private long lastPing = System.currentTimeMillis();

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
        super.send(packet.encode());
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
    protected void onConnect() {
        System.out.println("Connected!");
    }

    @Override
    protected void onDisconnect() {
        System.out.println("Disconnected!");
    }

    @Override
    protected void onMessage(KcpChannel channel, byte[] data) {
        System.out.println("Received: " + new String(data).subSequence(0, 9));
    }
}
