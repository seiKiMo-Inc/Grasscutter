package io.grasscutter.utils.interfaces;

import com.google.gson.reflect.TypeToken;
import io.grasscutter.data.FieldType;
import io.grasscutter.data.Special;
import io.grasscutter.utils.DataUtils;
import io.grasscutter.utils.DatabaseUtils;
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
     * Serializes this object into a key-value map. Objects to be serialized must: - Not be transient.
     * - Not be static.
     *
     * @return The serialized object.
     */
    default Map<String, Object> serialize() {
        // Find fields and create data map.
        var fields = getClass().getDeclaredFields();
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
                // Check if the value is not a primitive.
                if (!DataUtils.isPrimitive(value)) {
                    // Check if the field is serializable.
                    if (value instanceof Serializable serializable) {
                        // Serialize the value.
                        value = serializable.serialize();
                    } else continue; // Skip.
                }

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
        var fields = getClass().getDeclaredFields();

        // Iterate through fields.
        for (var field : fields) {
            // Skip if transient or static.
            if (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            // Make the field accessible.
            field.setAccessible(true);

            try {
                // Get field name.
                var name = field.getName();
                // Check if the field is special.
                if (field.isAnnotationPresent(Special.class)) {
                    // Check if the field is an ID field.
                    var special = field.getAnnotation(Special.class);
                    if (special.value() != FieldType.ID) continue; // Skip.

                    // Update the name depending on the database.
                    name = DatabaseUtils.getIdFieldName(name);
                }

                // Get the field value.
                var value = data.get(name);
                if (value == null) {
                    continue;
                }

                // De-serialize and set.
                if (value instanceof Map) {
                    var instance = field.getType().getConstructor().newInstance();
                    EncodingUtils.deserializeTo(instance, (Map<String, Object>) value);
                    field.set(this, instance);
                } else field.set(this, value);
            } catch (ReflectiveOperationException ignored) {
                // Ignore.
            }
        }
    }
}
