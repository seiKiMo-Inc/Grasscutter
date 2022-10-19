package io.grasscutter.utils;

import io.grasscutter.account.Account;
import io.grasscutter.data.DataInterface;
import io.grasscutter.server.DedicatedServer;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

/* Utility methods for interfacing with the database. */
public interface DatabaseUtils {
    /**
     * Shortcut method to get the data interface.
     *
     * @return A {@link DataInterface} instance.
     */
    static DataInterface getDataInterface() {
        return DedicatedServer.getInstance().getDataInterface();
    }

    /**
     * Shortcut method to get an account by username.
     *
     * @param username The username.
     * @return The account, or null if the account does not exist.
     */
    @Nullable static Account fetchAccount(String username) {
        return DatabaseUtils.getDataInterface().get(Account.class, Map.of("username", username));
    }
}
