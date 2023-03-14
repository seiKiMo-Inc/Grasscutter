package io.grasscutter.utils;

import io.grasscutter.player.Account;
import io.grasscutter.data.DataInterface;
import io.grasscutter.data.DataSerializable;
import io.grasscutter.player.Player;
import io.grasscutter.server.DedicatedServer;
import java.util.Map;

import io.grasscutter.utils.interfaces.Serializable;
import io.grasscutter.utils.objects.Counter;
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
     * Shortcut method to get the ID field name for a database.
     *
     * @param fieldName The field name.
     * @return The ID field name.
     */
    static String getIdFieldName(String fieldName) {
        return DatabaseUtils.getDataInterface().getIdFieldName(fieldName);
    }

    /**
     * Shortcut method to save an object.
     * The object should be annotated with {@link DataSerializable}.
     *
     * @param object The object to save.
     */
    static void save(Serializable object) {
        DatabaseUtils.getDataInterface().save(object);
    }

    /**
     * Shortcut method to delete an object.
     * The object should be annotated with {@link DataSerializable}.
     *
     * @param object The object to delete.
     */
    static void delete(Serializable object) {
        DatabaseUtils.getDataInterface().purge(object);
    }

    /**
     * Shortcut method to get an account by username.
     *
     * @param username The username.
     * @return The account, or null if the account does not exist.
     */
    @Nullable static Account fetchAccount(String username) {
        return DatabaseUtils.getDataInterface().get(Account.class,
                Map.of("username", username));
    }

    /**
     * Shortcut method to get an account by userId.
     *
     * @param userId The userId.
     * @return The account, or null if the account does not exist.
     */
    @Nullable static Account fetchAccount(long userId) {
        return DatabaseUtils.getDataInterface().get(Account.class, Map.of(
                DatabaseUtils.getIdFieldName("id"), userId));
    }

    /**
     * Shortcut method to get an account by UID, or the gameUserId.
     *
     * @param uid The UID.
     * @return The account, or null if the account does not exist.
     */
    @Nullable static Account fetchAccountByUid(int uid) {
        return DatabaseUtils.getDataInterface().get(Account.class,
                Map.of("gameUserId", uid));
    }

    /**
     * Shortcut method to get a player by account.
     *
     * @param account The account.
     * @return The player, or null if the player does not exist.
     */
    @Nullable static Player fetchPlayer(Account account) {
        return DatabaseUtils.fetchPlayer(account, Player.class);
    }

    /**
     * Shortcut method to get a player by account.
     * This method allows you to specify the player class to use.
     *
     * @param account The account.
     * @param playerClass The player class to use.
     * @return The player, or null if the player does not exist.
     */
    @Nullable static <T extends Player> T fetchPlayer(Account account, Class<T> playerClass) {
        return DatabaseUtils.getDataInterface().get(playerClass,
                Map.of("accountId", account.id));
    }

    /**
     * Fetch the counter's value and increment it.
     *
     * @param counter The counter's type.
     * @return The counter's value.
     */
    static long fetchAndIncrement(String counter) {
        var dataInterface = DatabaseUtils.getDataInterface();
        var counterObject = dataInterface.get(Counter.class, Map.of(
                DatabaseUtils.getIdFieldName("id"), counter));

        // If the counter does not exist, create it.
        if (counterObject == null) {
            counterObject = new Counter();
            counterObject.type = counter;
            counterObject.value = -1;
        }

        // Increment the counter.
        counterObject.value++;
        // Save the counter.
        counterObject.save();

        return counterObject.value;
    }
}
