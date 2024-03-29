package io.grasscutter.utils.objects;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/* Replica of JsonObject. Includes chaining. */
public final class JObject {
    /* Type adapter for gson. */
    public static class Adapter extends TypeAdapter<JObject> {
        @Override
        public void write(JsonWriter out, JObject value) throws IOException {
            out.beginObject();
            for (var entry : value.members.entrySet()) {
                out.name(entry.getKey());
                out.jsonValue(entry.getValue().toString());
            }
            out.endObject();
        }

        @Override
        public JObject read(JsonReader in) throws IOException {
            var object = JObject.c();
            in.beginObject();
            while (in.hasNext()) {
                object.add(in.nextName(), JsonParser.parseReader(in));
            }
            in.endObject();
            return object;
        }
    }

    /**
     * Creates a new empty object.
     *
     * @return The new object.
     */
    public static JObject c() {
        return new JObject();
    }

    private final LinkedTreeMap<String, JsonElement> members = new LinkedTreeMap<>();

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

    /**
     * Returns the property holder.
     *
     * @return The property holder.
     */
    public Object json() {
        return this.members;
    }
}
