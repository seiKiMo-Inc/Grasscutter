package io.grasscutter.data;

import java.util.Map;

/* A database interface. */
public interface DataInterface {
    /**
     * Called before {@link #initialize()}.
     * Should connect to the database.
     */
    void connect();

    /**
     * Called when the server is disconnecting from the database.
     * Should perform any cleanup, data saving, etc.
     */
    void disconnect();

    /**
     * Called when the data interface is initialized (after {@link #connect()}).
     * Anything related to initial data creation/modification should be performed here.
     */
    void initialize();

    /**
     * Saves all data to the database.
     * This method will be called periodically and on events such as {@link #disconnect()}.
     */
    void save();

    /**
     * Saves the object to the database.
     * @param object The object to save. Class should be annotated with {@link DataSerializable}.
     */
    void save(Object object);

    /**
     * Loads data from the database.
     * Filters data based on class and lookup arguments.
     * @param fetch The class to fetch data for. Class should be annotated with {@link DataSerializable}.
     * @param lookupArgs The lookup arguments to filter data by.
     * @return A class instance with data loaded from the database.
     */
    <T> T get(Class<T> fetch, Map<String, Object> lookupArgs);

    /**
     * Purges the object from the database.
     * @param object The object to purge. Class should be annotated with {@link DataSerializable}.
     */
    void purge(Object object);
}