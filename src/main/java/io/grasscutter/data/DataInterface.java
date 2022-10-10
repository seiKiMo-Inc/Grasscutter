package io.grasscutter.data;

import io.grasscutter.utils.interfaces.Serializable;
import io.grasscutter.utils.objects.Pair;
import java.util.Map;

/* A database interface. */
public interface DataInterface {
    /** Called before {@link #initialize()}. Should connect to the database. */
    void connect();

    /**
     * Called when the server is disconnecting from the database. Should perform any cleanup, data
     * saving, etc.
     */
    void disconnect();

    /**
     * Called when the data interface is initialized (after {@link #connect()}). Anything related to
     * initial data creation/modification should be performed here.
     */
    void initialize();

    /**
     * Saves all data to the database. This method will be called periodically and on events such as
     * {@link #disconnect()}.
     */
    void save();

    /**
     * Saves the object to the database.
     *
     * @param object The object to save. Class should be annotated with {@link DataSerializable}.
     */
    void save(Object object);

    /**
     * Loads data from the database. Filters data based on class and lookup arguments.
     *
     * @param fetch The class to fetch data for. Class should be annotated with {@link
     *     DataSerializable}.
     * @param lookupArgs The lookup arguments to filter data by.
     * @return A class instance with data loaded from the database.
     */
    <T> T get(Class<T> fetch, Map<String, Object> lookupArgs);

    /**
     * Purges the object from the database.
     *
     * @param object The object to purge. Class should be annotated with {@link DataSerializable}.
     */
    void purge(Object object);

    /**
     * Checks if an object is data serializable.
     *
     * @param object The object to check. Class should be annotated with {@link DataSerializable}.
     * @return True if the object is data serializable, false otherwise.
     */
    static boolean isSerializable(Object object) {
        return object instanceof Serializable
                && object.getClass().isAnnotationPresent(DataSerializable.class);
    }

    /**
     * Returns the name of the table to save the object to.
     *
     * @param object The object to get the table name for. Class should be annotated with {@link
     *     DataSerializable}.
     * @return The name of the table to save the object to.
     */
    static String getSerializeTable(Object object) {
        return object.getClass().getAnnotation(DataSerializable.class).table();
    }

    /**
     * Returns the ID value of the object.
     *
     * @param object The object to get the ID value for. Class should be annotated with {@link
     *     DataSerializable}.
     * @return The ID value of the object.
     */
    static Pair<String, Object> getSerializeId(Object object) {
        var fieldName = object.getClass().getAnnotation(DataSerializable.class).idField();

        try {
            var field = object.getClass().getField(fieldName);
            return new Pair<>(fieldName, field.get(object));
        } catch (Exception ignored) {
            return new Pair<>(fieldName, null);
        }
    }
}
