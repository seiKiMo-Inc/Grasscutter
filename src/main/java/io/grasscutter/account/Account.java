package io.grasscutter.account;

import io.grasscutter.data.DataSerializable;
import io.grasscutter.utils.interfaces.Serializable;

@DataSerializable(table = "accounts")
public final class Account implements Serializable {
    public long id = Long.MAX_VALUE; // The account's UID.
}
