package io.grasscutter.auth;

import io.grasscutter.utils.DatabaseUtils;
import io.grasscutter.utils.definitions.SDKResponse;
import io.grasscutter.utils.definitions.auth.ShieldLoginRequest;
import io.grasscutter.utils.objects.lang.TextContainer;
import org.jetbrains.annotations.NotNull;

/** Login authenticator through only a username. */
public final class UsernameAuthenticator
        implements Authenticator<ShieldLoginRequest> {
    @Override
    @NotNull public SDKResponse authenticate(ShieldLoginRequest request) {
        var response = SDKResponse.builder();

        // Check for account.
        var account = DatabaseUtils.fetchAccount(request.username);
        // TODO: Implement proper server checks.
        if (account == null) {
            return response
                    .message(new TextContainer("account.not_found").toString())
                    .retcode(-201) // -201: Account not found. (not in Retcode.proto)
                    .build();
        }

        return response.data(account.generateLoginResult()).build();
    }
}
