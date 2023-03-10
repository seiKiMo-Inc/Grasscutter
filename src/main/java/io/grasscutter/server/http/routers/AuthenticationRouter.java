package io.grasscutter.server.http.routers;

import io.grasscutter.server.DedicatedServer;
import io.grasscutter.server.http.Router;
import io.grasscutter.utils.EncodingUtils;
import io.grasscutter.utils.definitions.auth.GranterLoginRequest;
import io.grasscutter.utils.definitions.auth.LoginTokenData;
import io.grasscutter.utils.definitions.auth.ShieldLoginRequest;
import io.grasscutter.utils.definitions.auth.ShieldVerifyRequest;
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

        // Perform a login.
        var response = DedicatedServer.getInstance().getAuthHolder().login(request);

        // Send the response.
        ctx.json(response);
    }

    /**
     * Handles a shield verify request. Involves a cached token.
     *
     * @param ctx The request context.
     * @route /hk4e_global/mdk/shield/api/verify
     */
    private static void shieldVerify(Context ctx) {
        // De-serialize the request body.
        var request = ctx.bodyAsClass(ShieldVerifyRequest.class);
        if (request == null) return;

        // Perform a login.
        var response = DedicatedServer.getInstance().getAuthHolder().login(request);

        // Send the response.
        ctx.json(response);
    }

    /**
     * Handles a combo login request. Involves a session key.
     *
     * @param ctx The request context.
     * @route /hk4e_global/combo/granter/login/v2/login
     */
    private static void comboLogin(Context ctx) {
        // De-serialize the request body.
        var request = ctx.bodyAsClass(GranterLoginRequest.class);
        if (request == null) return;

        // De-serialize the login data.
        request.loginData = EncodingUtils.fromJson(
                request.rawLoginData, LoginTokenData.class);

        // Perform a login.
        var response = DedicatedServer.getInstance().getAuthHolder().login(request);

        // Send the response.
        ctx.result(response.toString());
    }
}
