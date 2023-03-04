package io.grasscutter.utils.objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LinkedTreeMap;

/* Replica of JsonObject. Includes chaining. */
public final class JObject extends JsonElement {
    /**
     * Creates a new empty object.
     * @return The new object.
     */
    public static JObject c() {
        return new JObject();
    }

    private final LinkedTreeMap<String, JsonElement> members = new LinkedTreeMap<>();

    @Override
    public JsonElement deepCopy() {
        var copy = new JObject();
        for (var entry : this.members.entrySet()) {
            copy.add(entry.getKey(), entry.getValue().deepCopy());
        }
        return copy;
    }

    /**
     * Adds a member to this object.
     *
     * @param name The name of the member.
     * @param value The value of the member.
     * @return This object.
     */
    public JObject add(String name, JsonElement value) {
        this.members.put(name, value);
        return this;
    }

    /**
     * Adds a member to this object.
     *
     * @param name The name of the member.
     * @param value The value of the member.
     * @return This object.
     */
    public JObject add(String name, String value) {
        return this.add(name, value == null ? JsonNull.INSTANCE : new JsonPrimitive(value));
    }

    /**
     * Adds a member to this object.
     *
     * @param name The name of the member.
     * @param value The value of the member.
     * @return This object.
     */
    public JObject add(String name, Number value) {
        return this.add(name, value == null ? JsonNull.INSTANCE : new JsonPrimitive(value));
    }

    /**
     * Adds a member to this object.
     *
     * @param name The name of the member.
     * @param value The value of the member.
     * @return This object.
     */
    public JObject add(String name, Boolean value) {
        return this.add(name, value == null ? JsonNull.INSTANCE : new JsonPrimitive(value));
    }

    /**
     * Adds a member to this object.
     *
     * @param name The name of the member.
     * @param value The value of the member.
     * @return This object.
     */
    public JObject add(String name, Character value) {
        return this.add(name, value == null ? JsonNull.INSTANCE : new JsonPrimitive(value));
    }
}
