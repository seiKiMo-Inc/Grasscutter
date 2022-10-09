package io.grasscutter.server;

import io.grasscutter.server.game.GameServer;
import io.grasscutter.server.http.HttpServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Arrays;

/* Manages server-related aspects. */
public final class DedicatedServer {
    @Getter private static DedicatedServer instance;
    @Getter private static GameServer gameServer;
    @Getter private static HttpServer httpServer;
    @Getter private final Logger logger;

    private final ServerThread thread;

    public DedicatedServer(Logger logger, GameServer gameServer, HttpServer httpServer) {
        // Set the dedicated servers.
        DedicatedServer.gameServer = gameServer;
        DedicatedServer.httpServer = httpServer;

        // Set the dedicated server instance.
        DedicatedServer.instance = this;
        // Set the dedicated server logger.
        this.logger = logger;

        // Create the server thread.
        this.thread = new ServerThread(this);
    }

    /** Performs a global server tick. */
    public void tick() {}

    /** Starts both game & HTTP servers. */
    public void start() {
        // Start the dedicated servers.
        DedicatedServer.gameServer.start();
        DedicatedServer.httpServer.start();

        // Start the dedicated server thread.
        this.thread.start();
    }

    /** Stops both game & HTTP servers. */
    public void stop() {
        // Stop the dedicated servers.
        DedicatedServer.gameServer.interrupt();
        DedicatedServer.httpServer.stop();

        // Stop the dedicated server thread.
        this.thread.end();
    }

    /**
     * Dumps information about all active threads.
     *
     * @return {@link ThreadInfo} as a string.
     */
    public String dumpThreads() {
        var threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);

        var builder = new StringBuilder();
        Arrays.stream(threads).forEach(thread -> builder.append('\n').append(thread));

        return builder.toString();
    }
}
