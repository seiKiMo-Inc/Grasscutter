package io.grasscutter.auth;

import io.grasscutter.utils.DatabaseUtils;
import io.grasscutter.utils.definitions.auth.LoginResultResponse;
import io.grasscutter.utils.definitions.auth.ShieldLoginRequest;
import io.grasscutter.utils.objects.lang.TextContainer;
import org.jetbrains.annotations.NotNull;

/** Login authenticator through only a username. */
public final class UsernameAuthenticator
        implements Authenticator<ShieldLoginRequest, LoginResultResponse> {
    @Override
    @NotNull public LoginResultResponse authenticate(ShieldLoginRequest request) {
        var response = new LoginResultResponse();

        // Check for account.
        var account = DatabaseUtils.fetchAccount(request.username);
        // TODO: Implement proper server checks.
        if (account == null) {
            response.message = new TextContainer("account.not_found").toString();
            response.retcode = -201; // -201: Account not found. (not in Retcode.proto)
            return response;
        }

        // Set the response data.
        var verifyData = response.data;
        var accountData = verifyData.account;
        accountData.uid = String.valueOf(account.id);
        accountData.email = account.email;
        accountData.token = account.generateSessionKey();

        return response;
    }
}
