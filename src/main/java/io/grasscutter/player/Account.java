package io.grasscutter.player;

import io.grasscutter.data.DataSerializable;
import io.grasscutter.data.FieldType;
import io.grasscutter.data.Special;
import io.grasscutter.utils.CryptoUtils;
import io.grasscutter.utils.EncodingUtils;
import io.grasscutter.utils.definitions.auth.VerifyData;
import io.grasscutter.utils.interfaces.DatabaseObject;
import io.grasscutter.utils.interfaces.Output;

@DataSerializable(table = "accounts")
public final class Account implements DatabaseObject {
    @Special(FieldType.ID)
    public long id = Long.MAX_VALUE; // The account's ID.

    public int gameUserId = Integer.MAX_VALUE; // The ID shown as "UID" in-game.

    public String email = ""; // The account's email.
    public String username = ""; // The account's username.
    public String password = ""; // The account's password.

    public String loginToken = ""; // A login token for the account.
    public String sessionKey = ""; // A session key for the account.

    /**
     * Generates a session token for the account.
     *
     * @return A randomly generated hex string.
     */
    public String generateSessionKey() {
        return this.saveAndFetch(
                () -> this.sessionKey = EncodingUtils.toHex(CryptoUtils.generateBytes(32)));
    }

    /**
     * Generates a login token for the account.
     *
     * @return A randomly generated hex string.
     */
    public String generateLoginToken() {
        return this.saveAndFetch(
                () -> this.loginToken = EncodingUtils.toHex(CryptoUtils.generateBytes(32)));
    }

    /*
     * Utility.
     */

    /**
     * Saves the database object and returns the field.
     *
     * @param field The field to return.
     * @return The field.
     */
    public <T> T saveAndFetch(Output<T> field) {
        var result = field.result();
        this.save();
        return result;
    }

    /*
     * Generators.
     */

    /**
     * Generates a login result for the account. Generates a unique session key.
     *
     * @return A login result.
     */
    public VerifyData generateLoginResult() {
        return this.generateLoginResult(this.generateSessionKey());
    }

    /**
     * Generates a login result for the account. Uses the provided session key.
     *
     * @param sessionKey The session key to use.
     * @return A login result.
     */
    public VerifyData generateLoginResult(String sessionKey) {
        var response = new VerifyData();
        var accountData = response.account;
        accountData.uid = String.valueOf(this.id);
        accountData.email = this.email;
        accountData.token = sessionKey;

        return response;
    }
}
