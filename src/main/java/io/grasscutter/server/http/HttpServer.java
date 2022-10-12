package io.grasscutter.server.http;

import io.grasscutter.utils.NetworkUtils;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.constants.Properties;
import io.grasscutter.utils.objects.lang.TextContainer;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.util.JavalinBindException;
import lombok.Getter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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
        config.jetty.server(() -> HttpServer.createServer(true));
    }

    /**
     * Creates an HTTP(S) server.
     *
     * @return Server instance.
     */
    @SuppressWarnings("resource")
    private static Server createServer(boolean tryEncryption) {
        var networkProperties = Properties.SERVER().httpServer;

        // Create a server & a connector.
        var server = new Server();
        var serverConnector = new ServerConnector(server);

        // Check if SSL/TLS should be used.
        var encryption = networkProperties.encryption;
        if (encryption.useSsl && tryEncryption) {
            var sslContext = new SslContextFactory.Server();
            var keystoreFile = new File(encryption.keyStore);

            // Perform keystore validation.
            if (!keystoreFile.exists() || !keystoreFile.canRead()) {
                Log.warn(new TextContainer("network.no_keystore", keystoreFile.getPath()));
                return HttpServer.createServer(false); // Try again without encryption.
            }

            try {
                // Set the keystore and password.
                sslContext.setKeyStorePath(keystoreFile.getPath());
                sslContext.setKeyStorePassword(encryption.keyStorePassword);
            } catch (Exception ignored) {
                Log.warn(new TextContainer("network.bad_password", keystoreFile.getPath()));
                return HttpServer.createServer(false); // Try again without encryption.
            }

            // Create a new server connector with an SSL/TLS context.
            serverConnector = new ServerConnector(server, sslContext);
        }

        // Finalize the connector.
        serverConnector.setPort(networkProperties.bindPort);
        serverConnector.setHost(networkProperties.bindAddress);
        server.setConnectors(new ServerConnector[] {serverConnector});

        return server;
    }

    private final Javalin javalin;
    private final Logger logger;

    private HttpServer() {
        this.javalin = Javalin.create(HttpServer::javalinConfig);
        this.logger = LoggerFactory.getLogger("HTTP");
    }

    /** Starts the HTTP server. */
    public void start() {
        var networkProperties = Properties.SERVER().httpServer;
        var host = networkProperties.bindAddress;
        var port = networkProperties.bindPort;
        if (!NetworkUtils.validate(host, port)) {
            Log.warn(new TextContainer("network.invalid_address", host, port));
            System.exit(1);
            return;
        }

        try {
            // Start the server.
            this.javalin.start();
        } catch (JavalinBindException ignored) {
            Log.warn(this.logger, new TextContainer("network.bind-failed", port));
        }
    }

    /** Stops the HTTP server. */
    public void stop() {
        this.javalin.stop();
    }
}
