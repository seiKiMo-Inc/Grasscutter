package io.grasscutter.auth;

import io.grasscutter.utils.definitions.SDKResponse;
import org.jetbrains.annotations.Nullable;

/**
 * Handles a type of account authentication.
 *
 * @param <P> The parameter type.
 */
public interface Authenticator<P> {
    /**
     * Attempt to authenticate to the account.
     *
     * @param request The request object.
     * @return The account, or null if authentication failed.
     */
    @Nullable SDKResponse authenticate(P request);
}
