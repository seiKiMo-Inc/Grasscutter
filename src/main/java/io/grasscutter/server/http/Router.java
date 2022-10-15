package io.grasscutter.server.http;

import io.grasscutter.server.http.routers.*;
import io.javalin.Javalin;

/* Implementers handle HTTP requests. */
public interface Router {
    /**
     * Called when the router is registered. Implementers should register their routes here.
     *
     * @param app Javalin instance.
     */
    void setup(Javalin app);

    /**
     * Registers the default request routers.
     *
     * @param app HTTP server instance.
     */
    static void defaultSetup(HttpServer app) {
        // Register static routes.
        app.use(StaticRequestRouter.class);
        // Register internal game routes.
        app.use(DispatchRouter.class);
    }
}
