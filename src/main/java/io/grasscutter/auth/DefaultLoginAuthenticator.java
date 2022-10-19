package io.grasscutter.auth;

import io.grasscutter.proto.RetcodeOuterClass.Retcode;
import io.grasscutter.utils.DatabaseUtils;
import io.grasscutter.utils.EncodingUtils;
import io.grasscutter.utils.constants.CryptoConstants;
import io.grasscutter.utils.definitions.auth.LoginResultResponse;
import io.grasscutter.utils.definitions.auth.ShieldLoginRequest;
import io.grasscutter.utils.enums.KeyType;
import io.grasscutter.utils.objects.lang.TextContainer;
import java.nio.charset.StandardCharsets;
import org.jetbrains.annotations.NotNull;

/** The default login authenticator. */
public final class DefaultLoginAuthenticator
        implements Authenticator<ShieldLoginRequest, LoginResultResponse> {
    @Override
    @NotNull public LoginResultResponse authenticate(ShieldLoginRequest request) {
        var response = new LoginResultResponse();

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
        }

        // Check the password.
        if (password == null) {
            response.message = new TextContainer("exception.error").toString();
            response.retcode = Retcode.RETCODE_RET_FAIL.getNumber();
            return response;
        }

        // Check for account.
        var account = DatabaseUtils.fetchAccount(request.username);
        // TODO: Implement proper server checks.
        // TODO: Validate the password.
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
