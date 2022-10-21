package io.grasscutter.auth;

import io.grasscutter.account.Account;
import io.grasscutter.utils.definitions.auth.LoginResultResponse;
import io.grasscutter.utils.definitions.auth.ShieldLoginRequest;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Holds {@link Authenticator} instances.
 */
public final class AuthenticationHolder {
    @Setter private Authenticator<ShieldLoginRequest, LoginResultResponse> loginAuth;
    @Setter private Authenticator<Object, Account> tokenAuth;
    @Setter private Authenticator<Object, Account> comboAuth;

    @Setter private BiFunction<String, String, Account> loginHandler = AuthenticationHolder::defaultLogin;
    @Setter private Function<String, Boolean> resetHandler = AuthenticationHolder::defaultReset;

    public AuthenticationHolder() {
        this.loginAuth = new DefaultLoginAuthenticator();
        this.tokenAuth = new DefaultTokenAuthenticator();
        this.comboAuth = new DefaultComboAuthenticator();
    }

    /**
     * Attempt to:
     * - Find the account through username.
     * - Compare the password to the stored.
     * @param username The username.
     * @param password The encrypted password.
     */
    @Nullable
    public Account login(String username, String password) {
        return this.loginHandler.apply(username, password);
    }

    /**
     * Attempt to reset the password of an account.
     * @param username The username of the account to lookup.
     * @return The account, or null if the account does not exist.
     */
    public boolean resetPassword(String username) {
        return this.resetHandler.apply(username);
    }

    /*
     * Default authentication handlers.
     */

    /**
     * The default login method.
     * @param username The username.
     * @param password The encrypted password.
     * @return The account, or null if the account does not exist.
     */
    @Nullable
    private static Account defaultLogin(String username, String password) {

    }

    /**
     * The default reset password method.
     * @param username The username of the account to lookup.
     * @return True if the reset action was successful, false if an error occurred.
     */
    private static boolean defaultReset(String username) {

    }
}
