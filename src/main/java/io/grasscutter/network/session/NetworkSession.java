package io.grasscutter.network.session;

import io.grasscutter.network.kcp.KcpChannel;

/**
 * An internal KCP-client wrapper.
 * Serves as the interface to a client.
 */
public final class NetworkSession extends KcpChannel {
    @Override protected void onConnect() {

    }

    @Override protected void onDisconnect() {

    }

    @Override protected void onMessage(KcpChannel channel, byte[] data) {

    }
}