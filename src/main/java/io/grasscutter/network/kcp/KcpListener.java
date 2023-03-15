package io.grasscutter.network.kcp;

import io.grasscutter.network.NetworkSession;
import io.grasscutter.server.DedicatedServer;
import io.grasscutter.utils.EncodingUtils;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.objects.lang.TextContainer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.EventLoop;
import java.util.Map;
import kcp.highway.Ukcp;
import lombok.AllArgsConstructor;

/* KCP traffic listener & handler. */
@AllArgsConstructor
public final class KcpListener implements kcp.highway.KcpListener {
    private final EventLoop logicThread;
    private final Map<Ukcp, NetworkSession> clients;

    @Override
    public void onConnected(Ukcp ukcp) {
        // Perform offline server check.
        var server = DedicatedServer.getGameServer();
        var i = 0;
        while (server == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
                ukcp.close();
                return;
            }

            if (i++ > 5) {
                Log.error(new TextContainer("server.game.unavailable"));
                ukcp.close();
                return;
            }

            server = DedicatedServer.getGameServer();
        }

        // Create a new session for the client.
        var session = new NetworkSession(ukcp);
        session.onConnect();

        // Add the session to the client list.
        this.clients.put(ukcp, session);
    }

    @Override
    public void handleReceive(ByteBuf byteBuf, Ukcp ukcp) {
        var bytes = EncodingUtils.toByteArray(byteBuf);
        this.logicThread.execute(
                () -> {
                    try {
                        // Fetch the session for the client.
                        var session = this.clients.get(ukcp);
                        if (session == null) return;
                        // Handle the message.
                        session.onMessage(bytes);
                    } catch (Exception exception) {
                        this.handleException(exception, ukcp);
                    }
                });
    }

    @Override
    public void handleException(Throwable cause, Ukcp ukcp) {
        // Log the exception to the server console.
        Log.warn(new TextContainer("network.exception"), cause);
        // Close the connection.
        ukcp.close();
    }

    @Override
    public void handleClose(Ukcp ukcp) {
        // Remove the session from the client list.
        var session = this.clients.remove(ukcp);
        if (session == null) return;
        // Close the session.
        session.onDisconnect();
    }
}
