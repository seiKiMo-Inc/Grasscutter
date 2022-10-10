package io.grasscutter.utils.interfaces;

import com.google.gson.reflect.TypeToken;
import io.grasscutter.utils.EncodingUtils;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Complex serialization. Can automatically serialize the implementing class. Can automatically
 * deserialize to the implementing class.
 */
public interface Serializable {
    // This is the type that data is serialized into.
    Type SERIALIZE_TYPE = new TypeToken<Map<String, Object>>() {}.getType();

    /**
     * Serializes this object into a key-value map. Objects to be serialized must: - Be a public
     * field. - Not be transient. - Not be static.
     *
     * @return The serialized object.
     */
    default Map<String, Object> serialize() {
        // Find fields and create data map.
        var fields = getClass().getFields();
        var dataMap = new HashMap<String, Object>();

        // Iterate through fields.
        for (var field : fields) {
            // Skip if transient or static.
            if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            // Make the field accessible.
            field.setAccessible(true);

            try {
                // Get field name and value.
                var name = field.getName();
                var value = field.get(this);

                // Add to map.
                dataMap.put(name, value);
            } catch (IllegalAccessException ignored) {
                // Ignore.
            }
        }

        return dataMap;
    }

    /**
     * Deserializes the given map into this object.
     *
     * @param data The data to deserialize.
     */
    @SuppressWarnings("unchecked")
    default void deserialize(Map<String, Object> data) {
        // Find fields and create data map.
        var fields = getClass().getFields();

        // Iterate through fields.
        for (var field : fields) {
            // Skip if transient or static.
            if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            // Make the field accessible.
            field.setAccessible(true);

            try {
                // Get field name and value.
                var name = field.getName();
                var value = data.get(name);
                // Check if the value is null.
                if (value == null) {
                    continue;
                }

                // De-serialize and set.
                var instance = field.getType().getConstructor().newInstance();
                EncodingUtils.deserializeTo(instance, (Map<String, Object>) value);
                field.set(this, instance);
            } catch (ReflectiveOperationException ignored) {
                // Ignore.
            }
        }
    }
}
