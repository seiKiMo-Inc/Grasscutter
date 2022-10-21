package io.grasscutter.account;

import io.grasscutter.data.DataSerializable;
import io.grasscutter.data.FieldType;
import io.grasscutter.data.Special;
import io.grasscutter.utils.CryptoUtils;
import io.grasscutter.utils.EncodingUtils;
import io.grasscutter.utils.interfaces.Serializable;

@DataSerializable(table = "accounts")
public final class Account implements Serializable {
    @Special(FieldType.ID) public long id = Long.MAX_VALUE; // The account's ID.
    public long gameUserID = Long.MAX_VALUE; // The ID shown as "UID" in-game.

    public String email = ""; // The account's email.
    public String username = ""; // The account's username.
    public String password = ""; // The account's password.

    public String token = ""; // A login token for the account.
    public String sessionKey = ""; // A session key for the account.

    /**
     * Generates a login token for the account.
     * @return A randomly generated hex string.
     */
    public String generateSessionKey() {
        return this.sessionKey = EncodingUtils.toHex(CryptoUtils.generateBytes(32));
    }
}
