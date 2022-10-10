package io.grasscutter.utils.objects.lang;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.grasscutter.utils.definitions.LanguageData;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;

/* Represents a language. */
public final class Language {
    @Getter private final LanguageData data;
    private final Map<String, String> loaded = new ConcurrentHashMap<>();

    /**
     * Initializes a language cache. Cached data is stored in memory.
     *
     * @param data Language data.
     */
    public Language(LanguageData data) {
        this.data = data;
    }

    /**
     * Loads the specified language key.
     *
     * @param key Key to load. (e.g. "system.startup")
     * @return The value of the specified key.
     */
    private String loadKey(String key) {
        return this.loaded.computeIfAbsent(
                key,
                k -> {
                    // Find & validate keys.
                    var keys = k.split("\\.");
                    if (keys.length < 2) return "";

                    // Find the data for the parent key.
                    var accessor = this.data.find(keys[0]);

                    var index = 1;
                    var value = "";
                    var found = false;

                    while (true) {
                        if (index == keys.length) break;

                        var currentKey = keys[index++];
                        if (accessor.has(currentKey)) {
                            var element = accessor.get(currentKey);
                            if (element.isJsonObject()) accessor = element.getAsJsonObject();
                            else {
                                found = true;
                                value = element.getAsString();
                                break;
                            }
                        } else break;
                    }

                    return found ? value : "";
                });
    }

    /**
     * Loads all translation keys into memory.
     *
     * @param loadFrom A collection of keys to load.
     */
    private void loadAllKeys(JsonObject loadFrom) {
        for (var entry : loadFrom.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();

            if (value.isJsonObject()) this.loadAllKeys(value.getAsJsonObject());
            else this.loaded.put(key, value.getAsString());
        }
    }

    /** Loads all translation keys into memory. Should be called after server is fully initialized. */
    public void loadAllKeys() {
        this.data.all().forEach(this::loadAllKeys);
    }

    /**
     * Performs language injection. Allows plugins to inject their own language keys. This process
     * cannot override existing keys.
     *
     * @param data The language data to append.
     */
    @SuppressWarnings("unchecked")
    public void inject(Map<String, Object> data) {
        for (var entry : data.entrySet()) {
            var value = entry.getValue();
            if (value instanceof Map<?, ?>) this.inject((Map<String, Object>) value);
            else if (value instanceof String)
                this.data.injected.add(entry.getKey(), new JsonPrimitive((String) value));
        }
    }

    /**
     * Gets the value of the specified key. Attempts to load the key if it is not already loaded.
     *
     * @param key Key to get. (e.g. "system.startup")
     * @return The string from the translation data.
     */
    public String translate(String key) {
        return this.loadKey(key);
    }
}
