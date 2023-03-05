package io.grasscutter.server.http.routers;

import com.google.gson.JsonElement;
import io.grasscutter.proto.RetcodeOuterClass.Retcode;
import io.grasscutter.server.http.Router;
import io.grasscutter.utils.definitions.SDKResponse;
import io.grasscutter.utils.objects.JObject;
import io.javalin.Javalin;
import io.javalin.http.Context;

/* Handles SDK (API) requests. */
public final class SDKRouter implements Router {
    @Override
    public void setup(Javalin app) {
        app.post("/account/risky/api/check", this::validateLogin);
    }

    /**
     * Validates a login request.
     *
     * @param ctx The request context.
     * @route /account/risky/api/check
     */
    private void validateLogin(Context ctx) {
        ctx.json(SDKResponse.builder()
                .retcode(Retcode.RETCODE_RET_SUCC.getNumber())
                .message("OK")
                .data(JObject.c()
                        .add("id", "none")
                        .add("action", "ACTION_NONE")
                        .add("geetest", (JsonElement) null)
                        .json())
                .build());
    }
}
