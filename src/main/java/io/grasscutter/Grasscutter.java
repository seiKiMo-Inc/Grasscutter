package io.grasscutter;

import io.grasscutter.server.game.GameServer;
import io.grasscutter.server.http.HttpServer;
import io.grasscutter.utils.objects.Configuration;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.util.logging.LogManager;

public final class Grasscutter {
    private static final long startupTime = System.currentTimeMillis();

    @Getter private static final Logger logger = LoggerFactory.getLogger(Grasscutter.class);
    @Getter private static final LineReader console = Grasscutter.createConsole();
    @Getter private static final Configuration config = new Configuration();

    static {
        // Configure application logger.
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        System.setProperty("logback.configurationFile", "src/main/resources/logback.xml");

        // Set shutdown hook for the application.
        Runtime.getRuntime().addShutdownHook(new Thread(Grasscutter::shutdown));
    }

    /**
     * Entrypoint for the application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        // Log a message to the console.
        Grasscutter.getLogger().info("Starting Grasscutter...");

        // Create server instances.
        var gameServer = GameServer.create();
        var httpServer = HttpServer.create();

        // Start the server instances.
        gameServer.start();
        httpServer.start();

        // Log a message to the console.
        var startupTime = System.currentTimeMillis() - Grasscutter.startupTime;
        Grasscutter.getLogger().info("Grasscutter started in {}ms.", startupTime);
    }

    /** Shutdown hook for the application. */
    public static void shutdown() {
        // Log message to the console.
        Grasscutter.getLogger().info("Shutting down Grasscutter...");

        // Stop server instances.
        GameServer.getInstance().stop();
        HttpServer.getInstance().stop();
    }

    /**
     * Creates a {@link LineReader}, or "console" for the application.
     *
     * @return A {@link LineReader} instance.
     * @throws RuntimeException if something impossible happened. (no dumb terminal created)
     */
    @SneakyThrows(IOException.class)
    private static LineReader createConsole() {
        Terminal terminal;
        try {
            terminal = TerminalBuilder.builder().jna(true).build();
        } catch (IOException ignored) {
            // Try to get a dumb terminal.
            terminal = TerminalBuilder.builder().dumb(true).build();
        }

        return LineReaderBuilder.builder().terminal(terminal).build();
    }
}
