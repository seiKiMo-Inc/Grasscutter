package io.grasscutter.server.http;

import io.grasscutter.utils.constants.Properties;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import lombok.Getter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/** Handles HTTP traffic. Primarily acts as the dispatch server. */
public final class HttpServer {
    @Getter private static HttpServer instance;

    /** Creates a {@link HttpServer} instance. */
    public static HttpServer create() {
        if (instance != null) throw new IllegalStateException("HttpServer instance already exists.");

        return instance = new HttpServer();
    }

    /**
     * Configures Javalin.
     *
     * @param config Javalin configuration instance.
     */
    private static void javalinConfig(JavalinConfig config) {
        // Set the Jetty HTTP(S) server to use.
        config.jetty.server(HttpServer::createServer);
    }

    /**
     * Creates an HTTP(S) server.
     *
     * @return Server instance.
     */
    @SuppressWarnings("resource")
    private static Server createServer() {
        var networkProperties = Properties.SERVER().httpServer;

        // Create a server & a connector.
        var server = new Server();
        var serverConnector = new ServerConnector(server);

        // Check if SSL/TLS should be used.
        if (networkProperties.useSsl) {
            var sslContext = new SslContextFactory.Server();
        }

        // Finalize the connector.
        serverConnector.setPort(networkProperties.bindPort);
        serverConnector.setHost(networkProperties.bindAddress);
        server.setConnectors(new ServerConnector[] {serverConnector});

        return server;
    }

    private final Javalin javalin;

    private HttpServer() {
        this.javalin = Javalin.create(HttpServer::javalinConfig);
    }

    /** Starts the HTTP server. */
    public void start() {
        this.javalin.start();
    }

    /** Stops the HTTP server. */
    public void stop() {
        this.javalin.stop();
    }
}
