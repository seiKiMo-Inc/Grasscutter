package io.grasscutter.auth;

import org.jetbrains.annotations.Nullable;

/**
 * Handles a type of account authentication.
 * @param <P> The parameter type.
 * @param <R> The return type.
 */
public interface Authenticator<P, R> {
    /**
     * Attempt to authenticate to the account.
     * @param request The request object.
     * @return The account, or null if authentication failed.
     */
    @Nullable
    R authenticate(P request);
}