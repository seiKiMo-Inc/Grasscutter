package io.grasscutter.auth;

import io.grasscutter.utils.definitions.SDKResponse;
import io.grasscutter.utils.definitions.auth.ShieldVerifyRequest;

/** The default token authenticator. */
public class DefaultTokenAuthenticator
        implements Authenticator<ShieldVerifyRequest> {
    @Override
    public SDKResponse authenticate(ShieldVerifyRequest token) {
        return null;
    }
}
