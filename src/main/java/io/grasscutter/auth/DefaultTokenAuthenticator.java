package io.grasscutter.auth;

import io.grasscutter.utils.DatabaseUtils;
import io.grasscutter.utils.definitions.SDKResponse;
import io.grasscutter.utils.definitions.auth.ShieldVerifyRequest;

/** The default cached token authenticator. */
public class DefaultTokenAuthenticator implements Authenticator<ShieldVerifyRequest> {
    @Override
    public SDKResponse authenticate(ShieldVerifyRequest token) {
        // Get the account from the database.
        var account = DatabaseUtils.fetchAccount(Long.parseLong(token.accountId));
        if (account == null) {
            return SDKResponse.builder()
                    .message("Account not found.")
                    .retcode(-201) // -201: Account not found. (not in Retcode.proto)
                    .build();
        }

        // Check if the session keys match.
        if (!account.sessionKey.equals(token.token)) {
            return SDKResponse.builder()
                    .message("Session key mismatch.")
                    .retcode(-201) // -201: Account error. (not in Retcode.proto)
                    .build();
        }

        return SDKResponse.builder().data(account.generateLoginResult(account.sessionKey)).build();
    }
}
