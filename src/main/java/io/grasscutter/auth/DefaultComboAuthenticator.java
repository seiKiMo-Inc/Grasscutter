package io.grasscutter.auth;

import io.grasscutter.utils.CryptoUtils;
import io.grasscutter.utils.DatabaseUtils;
import io.grasscutter.utils.definitions.SDKResponse;
import io.grasscutter.utils.definitions.auth.GranterLoginRequest;
import io.grasscutter.utils.definitions.auth.LoginData;

/** The default session key authenticator. */
public class DefaultComboAuthenticator implements Authenticator<GranterLoginRequest> {
    @Override
    public SDKResponse authenticate(GranterLoginRequest token) {
        var loginData = token.loginData;

        // Get the account from the database.
        var account = DatabaseUtils.fetchAccount(Long.parseLong(loginData.accountId));
        if (account == null) {
            return SDKResponse.builder()
                    .message("Account not found.")
                    .retcode(-201) // -201: Account not found. (not in Retcode.proto)
                    .build();
        }

        // Check if the session keys match.
        if (!account.sessionKey.equals(loginData.token)) {
            return SDKResponse.builder()
                    .message("Session key mismatch.")
                    .retcode(-201) // -201: Account error. (not in Retcode.proto)
                    .build();
        }

        return SDKResponse.builder().data(
                LoginData.builder()
                        .comboId(String.valueOf(CryptoUtils
                                .randomNumber(100000000, 900000000)))
                        .id(String.valueOf(account.id))
                        .token(account.generateLoginToken())
                .build()).build();
    }
}
