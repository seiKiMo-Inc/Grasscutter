package io.grasscutter.server.game;

import io.grasscutter.network.kcp.KcpServer;
import io.grasscutter.utils.NetworkUtils;
import io.grasscutter.utils.constants.Properties;
import java.net.InetSocketAddress;
import lombok.Getter;

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

    /**
     * @param address The network address & port to bind to.
     */
    public GameServer(InetSocketAddress address) {
        super(address);
    }
}
