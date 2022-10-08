package io.grasscutter.server.http;

import io.grasscutter.utils.constants.Properties;
import io.helidon.config.Config;
import io.helidon.webserver.WebServer;
import lombok.Getter;

/** Handles HTTP traffic. Primarily acts as the dispatch server. */
public final class HttpServer {
    @Getter private static HttpServer instance;

    /** Creates a {@link HttpServer} instance. */
    public static HttpServer create() {
        if (instance != null) throw new IllegalStateException("HttpServer instance already exists.");

        return instance = new HttpServer();
    }

    private final WebServer webServer;

    private HttpServer() {
        var networkProperties = Properties.SERVER().httpServer;

        this.webServer =
                WebServer.builder()
                        .config(Config.create())
                        //                        .routing(HttpServer.configureRouting())
                        .bindAddress(networkProperties.bindAddress)
                        .port(networkProperties.bindPort)
                        .build();
    }

    /** Starts the HTTP server. */
    public void start() {
        this.webServer.start();
    }

    /** Stops the HTTP server. */
    public void stop() {
        this.webServer.shutdown();
    }
}
