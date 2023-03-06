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
     * Returns the name of the ID field for a table.
     *
     * @param fallback The fallback name to use if the ID field name is not found.
     * @return The name of the ID field for a table.
     */
    String getIdFieldName(String fallback);

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
     * Checks if a class is data serializable.
     *
     * @param object The class to check. Class should be annotated with {@link DataSerializable}.
     * @return True if the class is data serializable, false otherwise.
     */
    static boolean isSerializable(Class<?> object) {
        return Serializable.class.isAssignableFrom(object)
                && object.isAnnotationPresent(DataSerializable.class);
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
     * Returns the name of the table to save the object to.
     *
     * @param object The class to get the table name for. Class should be annotated with {@link
     * @return The name of the table to save the object to.
     */
    static String getSerializeTable(Class<?> object) {
        return object.getAnnotation(DataSerializable.class).table();
    }

    /**
     * Returns the ID value of the object.
     *
     * @param object The object to get the ID value for. Class should be annotated with {@link
     *     DataSerializable}.
     * @return The ID value of the object.
     */
    static Pair<String, Object> getSerializeId(Object object) {
        // Find a field annotated with @Special.
        for (var field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Special.class)) {
                field.setAccessible(true); // Make the field accessible.

                // Check if the field is an ID field.
                var data = field.getAnnotation(Special.class);
                if (data.value() == FieldType.ID) {
                    try {
                        // Return the field's value.
                        return new Pair<>(field.getName(), field.get(object));
                    } catch (IllegalAccessException ignored) {
                        return null;
                    }
                }
            }
        }

        return null;
    }
}
