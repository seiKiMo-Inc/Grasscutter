package io.grasscutter.server.http.routers;

import io.grasscutter.server.http.Router;
import io.grasscutter.utils.definitions.auth.ShieldLoginRequest;
import io.javalin.Javalin;
import io.javalin.http.Context;

/* Handles any web-based authentication. */
public final class AuthenticationRouter implements Router {
    @Override
    public void setup(Javalin app) {
        // Username & Password login (from client).
        app.post("/hk4e_global/mdk/shield/api/login", AuthenticationRouter::shieldLogin);
        // Cached token login (from registry).
        app.post("/hk4e_global/mdk/shield/api/verify", AuthenticationRouter::shieldVerify);
        // Combo token login (from session key).
        app.post("/hk4e_global/combo/granter/login/v2/login", AuthenticationRouter::comboLogin);
    }

    /**
     * Handles a shield login request. Involves a username & password.
     *
     * @param ctx The request context.
     * @route /hk4e_global/mdk/shield/api/login
     */
    private static void shieldLogin(Context ctx) {
        // De-serialize the request body.
        var request = ctx.bodyAsClass(ShieldLoginRequest.class);
        if (request == null) return;

        // TODO: Handle login request.
        ctx.json(new Object());
    }

    /**
     * Handles a shield verify request. Involves a cached token.
     *
     * @param ctx The request context.
     * @route /hk4e_global/mdk/shield/api/verify
     */
    private static void shieldVerify(Context ctx) {}

    /**
     * Handles a combo login request. Involves a session key.
     *
     * @param ctx The request context.
     * @route /hk4e_global/combo/granter/login/v2/login
     */
    private static void comboLogin(Context ctx) {}
}
