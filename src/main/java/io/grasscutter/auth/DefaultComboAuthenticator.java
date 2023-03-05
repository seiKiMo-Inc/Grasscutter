package io.grasscutter.auth;

import io.grasscutter.utils.definitions.SDKResponse;
import io.grasscutter.utils.definitions.auth.GranterLoginRequest;

/** The default token authenticator. */
public class DefaultComboAuthenticator
        implements Authenticator<GranterLoginRequest> {
    @Override
    public SDKResponse authenticate(GranterLoginRequest token) {
        return null;
    }
}
