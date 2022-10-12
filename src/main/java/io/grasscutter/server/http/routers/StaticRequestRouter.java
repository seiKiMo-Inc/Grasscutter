package io.grasscutter.server.http.routers;

import io.grasscutter.server.http.Router;
import io.javalin.Javalin;

/* Handles static requests such as '/' and 404s. */
public final class StaticRequestRouter implements Router {
    @Override
    public void setup(Javalin app) {
        app.get("/", ctx -> ctx.result("Hello, world!"));
        app.error(404, ctx -> ctx.result("404 Not Found"));
    }
}
