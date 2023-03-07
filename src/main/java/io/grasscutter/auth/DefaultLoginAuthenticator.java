package io.grasscutter.auth;

import io.grasscutter.proto.RetcodeOuterClass.Retcode;
import io.grasscutter.utils.DatabaseUtils;
import io.grasscutter.utils.EncodingUtils;
import io.grasscutter.utils.constants.CryptoConstants;
import io.grasscutter.utils.definitions.SDKResponse;
import io.grasscutter.utils.definitions.auth.ShieldLoginRequest;
import io.grasscutter.utils.enums.KeyType;
import io.grasscutter.utils.objects.lang.TextContainer;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

/** The default on-demand login authenticator. */
public class DefaultLoginAuthenticator implements Authenticator<ShieldLoginRequest> {
    @Override
    @NotNull public SDKResponse authenticate(ShieldLoginRequest request) {
        var response = SDKResponse.builder();

        // Decode the password.
        var password = request.password;

        if (request.crypto) {
            var cipher = KeyType.AUTH.decrypt(CryptoConstants.ENCRYPTION_TYPE);
            if (cipher == null) {
                password = null;
            } else
                try {
                    var decoded = EncodingUtils.fromBase64(password.getBytes());
                    password = new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
                } catch (Exception ignored) {
                    password = null;
                }
        } else {
            password = new String(EncodingUtils.fromBase64(
                    password.getBytes()), StandardCharsets.UTF_8);
        }

        // Check the password.
        // if (password == null) {
        //     return response
        //             .message(new TextContainer("exception.error").toString())
        //             .retcode(Retcode.RETCODE_RET_FAIL.getNumber())
        //             .build();
        // }

        // Check for account.
        var account = DatabaseUtils.fetchAccount(request.username);
        // TODO: Implement proper server checks.
        // TODO: Validate the password.
        if (account == null) {
            return response
                    .message(new TextContainer("account.not_found").toString())
                    .retcode(-201) // -201: Account not found. (not in Retcode.proto)
                    .build();
        }

        return response.data(account.generateLoginResult()).build();
    }
}
