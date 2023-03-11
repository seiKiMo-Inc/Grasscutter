package io.grasscutter.server.game;

import io.grasscutter.network.NetworkSession;
import io.grasscutter.network.kcp.KcpListener;
import io.grasscutter.utils.NetworkUtils;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.constants.NetworkConstants;
import io.grasscutter.utils.constants.Properties;
import io.grasscutter.utils.objects.lang.TextContainer;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import kcp.highway.KcpServer;
import kcp.highway.Ukcp;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Handles all network traffic. Acts as the primary server. */
public final class GameServer extends KcpServer {
    @Getter private static GameServer instance;

    static {
        // Set the KCP channel config.
        var config = NetworkConstants.KCP_CONFIG;
        config.nodelay(true, Properties.SERVER()
                .gameServer.updateInterval, 2, true);
        config.setMtu(1400);
        config.setSndwnd(256);
        config.setRcvwnd(256);
        config.setTimeoutMillis(30 * 1000);//30s
        config.setUseConvChannel(true);
        config.setAckNoDelay(false);
    }

    /** Creates a new {@link GameServer} instance. */
    public static GameServer create() {
        if (instance != null) throw new IllegalStateException("GameServer instance already exists.");

        var networkProperties = Properties.SERVER().gameServer;
        return instance =
                new GameServer(
                        NetworkUtils.createFrom(networkProperties.bindAddress, networkProperties.bindPort));
    }

    @Getter private final InetSocketAddress address;

    private final EventLoop eventLoop = new DefaultEventLoop();
    private final Logger logger = LoggerFactory.getLogger("Game");
    private final Map<Ukcp, NetworkSession> clients = new ConcurrentHashMap<>();

    /**
     * @param address The network address & port to bind to.
     */
    private GameServer(InetSocketAddress address) {
        this.address = address;
    }

    /**
     * Invoked when the server starts.
     */
    public synchronized void start() {
        // Start the KCP server.
        super.init(new KcpListener(this.eventLoop, this.clients),
                NetworkConstants.KCP_CONFIG, this.getAddress());

        // Get the network properties.
        var port = this.getAddress().getPort();
        var host = this.getAddress().getHostString();

        // Log the server start.
        Log.info(this.logger, new TextContainer("server.game.done", port, host));
    }

    /** Stops the game server. */
    @Override
    public void stop() {
        super.stop(); // Stop the KCP server.

        // Stop the event loop.
        this.eventLoop.shutdownGracefully();
    }

    /** Invoked when a server reload occurs. */
    public void reload() {}
}
