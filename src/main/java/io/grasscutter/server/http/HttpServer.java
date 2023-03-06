package io.grasscutter.server.http;

import io.grasscutter.utils.NetworkUtils;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.constants.Properties;
import io.grasscutter.utils.objects.lang.TextContainer;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.util.JavalinBindException;
import java.io.File;
import lombok.Getter;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        // Create an HTTP configuration.
        var config = new HttpConfiguration();
        config.setSendServerVersion(false);
        // Create an HTTP connection factory.
        var connectionFactory = new HttpConnectionFactory(config);

        // Create a server & a connector.
        var server = new Server();
        var serverConnector = new ServerConnector(server, connectionFactory);

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
            serverConnector = new ServerConnector(server, sslContext, connectionFactory);
        }

        // Finalize the connector.
        serverConnector.setPort(networkProperties.bindPort);
        serverConnector.setHost(networkProperties.bindAddress);
        server.setConnectors(new ServerConnector[] { serverConnector });

        return server;
    }

    private Javalin javalin;
    private final Logger logger;

    private HttpServer() {
        this.javalin = Javalin.create(HttpServer::javalinConfig);
        this.logger = LoggerFactory.getLogger("HTTP");
    }

    /**
     * Invokes {@link Router#setup(Javalin)}.
     *
     * @param router The router class.
     * @param args Arguments to pass to the router.
     */
    public void use(Class<? extends Router> router, Object... args) {
        // Get all constructor parameters.
        Class<?>[] types = new Class<?>[args.length];
        for (var argument : args) types[args.length - 1] = argument.getClass();

        try {
            // Create a router instance & apply routes.
            var constructor = router.getDeclaredConstructor(types); // Get the constructor.
            var routerInstance = constructor.newInstance(args); // Create instance.
            routerInstance.setup(this.javalin); // Apply routes.
        } catch (Exception exception) {
            Log.warn(new TextContainer("server.http.router_error", router.getSimpleName()));
        }
    }

    /** Starts the HTTP server. */
    public void start() {
        var networkProperties = Properties.SERVER().httpServer;
        var host = networkProperties.bindAddress;
        var port = networkProperties.bindPort;
        if (!NetworkUtils.validate(host, port)) {
            Log.warn(this.logger, new TextContainer("network.invalid_address", host, port));
            System.exit(1);
            return;
        }

        try {
            // Start the server.
            this.javalin.start();
            // Log the startup & accessible endpoint.
            var protocol = networkProperties.encryption.inRouting ? "https" : "http";
            Log.info(this.logger, new TextContainer("server.http.done", port, protocol, host));
        } catch (JavalinBindException ignored) {
            Log.warn(this.logger, new TextContainer("network.bind_failed", port));
        }
    }

    /** Stops the HTTP server. */
    public void stop() {
        this.javalin.stop();
    }

    /** Invoked when a server reload occurs. */
    public void reload() {
        this.stop(); // Stop the server.
        this.javalin = Javalin.create(HttpServer::javalinConfig); // Re-create the server.
        this.start(); // Start the server.

        Router.defaultSetup(this); // Re-apply default routes.
    }
}
