package io.grasscutter.server.game;

import io.grasscutter.network.kcp.KcpServer;
import io.grasscutter.utils.NetworkUtils;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.constants.Properties;
import io.grasscutter.utils.objects.lang.TextContainer;
import java.net.InetSocketAddress;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Handles all network traffic. Acts as the primary server. */
public final class GameServer extends KcpServer {
    @Getter private static GameServer instance;

    /** Creates a new {@link GameServer} instance. */
    public static GameServer create() {
        if (instance != null) throw new IllegalStateException("GameServer instance already exists.");

        var networkProperties = Properties.SERVER().gameServer;
        return instance =
                new GameServer(
                        NetworkUtils.createFrom(networkProperties.bindAddress, networkProperties.bindPort));
    }

    private final Logger logger = LoggerFactory.getLogger("Game");

    /**
     * @param address The network address & port to bind to.
     */
    private GameServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public synchronized void start() {
        // Get the network properties.
        var port = this.getAddress().getPort();
        var host = this.getAddress().getHostString();

        // Log the server start.
        Log.info(this.logger, new TextContainer("server.game.done", port, host));
    }

    /** Invoked when a server reload occurs. */
    public void reload() {}
}
