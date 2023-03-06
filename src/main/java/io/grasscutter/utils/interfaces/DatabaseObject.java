package io.grasscutter.utils.interfaces;

import io.grasscutter.utils.DatabaseUtils;

/* Provides utility methods for database operations. */
public interface DatabaseObject extends Serializable {
    /**
     * Shortcut method to save this object.
     */
    default void save() {
        DatabaseUtils.save(this);
    }
}
