package io.grasscutter.network.kcp;

/* Handles network events from the KCP instance. */
public interface KcpHandler {
    /** Invoked when the server finishes processing a client connection. */
    void onConnect();

    /** Invoked when the server finishes processing a client disconnection. */
    void onDisconnect();

    /**
     * Invoked when the server receives a message from this client.
     *
     * @param data The decoded message data.
     */
    void onMessage(byte[] data);
}
