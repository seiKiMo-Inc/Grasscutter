package io.grasscutter.utils.definitions;

import com.google.gson.JsonObject;
import io.grasscutter.Grasscutter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/* A class representing language data. */
@SuppressWarnings("unused")
public final class LanguageData {
    public Details details = new Details();

    public JsonObject system = new JsonObject();
    public JsonObject network = new JsonObject();
    public JsonObject server = new JsonObject();
    public JsonObject database = new JsonObject();
    public JsonObject account = new JsonObject();
    public JsonObject command = new JsonObject();
    public JsonObject exception = new JsonObject();
    public transient JsonObject injected = new JsonObject();

    public static class Details {
        public String name = "";
        public List<String> authors = List.of();
        public String locale = "";
        public String version = "";
    }

    /*
     * Utility methods.
     */

    /**
     * Checks if this language data object is valid.
     *
     * @return True if valid, false otherwise.
     */
    public boolean isValid() {
        // Check details.
        if (details.name.isEmpty()) return false;
        if (details.authors.isEmpty()) return false;
        if (details.locale.isEmpty()) return false;
        if (details.version.isEmpty()) return false;

        // Check system.
        if (system.entrySet().isEmpty()) return false;

        return true;
    }

    /**
     * Returns the data behind the specified key.
     *
     * @param accessor Key accessor.
     * @return Data behind the specified key.
     */
    public JsonObject find(String accessor) {
        var clazz = this.getClass();
        var fields = clazz.getFields();

        // Attempt to find a built-in key.
        for (var field : fields) {
            if (Modifier.isTransient(field.getModifiers())) continue;

            if (field.getName().equals(accessor)) {
                try {
                    return (JsonObject) field.get(this);
                } catch (IllegalAccessException exception) {
                    Grasscutter.getLogger()
                            .error("Unable to load accessor %s.".formatted(accessor), exception);
                }
            }
        }

        // Attempt to find an injected key.
        if (this.injected.has(accessor)) return this.injected.getAsJsonObject(accessor);

        return new JsonObject();
    }

    /**
     * Collects all accessors.
     *
     * @return All accessors.
     */
    public List<JsonObject> all() {
        var clazz = this.getClass();
        var fields = clazz.getFields();
        var list = new ArrayList<JsonObject>();

        for (var field : fields) {
            if (Modifier.isTransient(field.getModifiers())) continue;

            try {
                var value = field.get(this);
                if (value instanceof JsonObject object) list.add(object);
            } catch (IllegalAccessException exception) {
                Grasscutter.getLogger()
                        .error("Unable to load accessor %s.".formatted(field.getName()), exception);
            }
        }

        return list;
    }
}
