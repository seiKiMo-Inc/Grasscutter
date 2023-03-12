package io.grasscutter.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.ByteString;
import io.grasscutter.utils.objects.JObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/* Utility methods seen when converting data. */
public interface EncodingUtils {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(JObject.class, new JObject.Adapter())
            .setPrettyPrinting()
            .create();

    /**
     * Encodes the given string into a Base64 string.
     *
     * @param string The string to encode.
     * @return The encoded string.
     */
    static String toBase64(String string) {
        return new String(EncodingUtils.toBase64(string.getBytes()));
    }

    /**
     * Encodes the given byte array into a Base64 buffer.
     *
     * @param bytes The bytes to encode.
     * @return The encoded string.
     */
    static byte[] toBase64(byte[] bytes) {
        return Base64.getEncoder().encode(bytes);
    }

    /**
     * Decodes the given Base64 string into a string.
     *
     * @param string The string to decode.
     * @return The decoded string.
     */
    static String fromBase64(String string) {
        return new String(Base64.getDecoder().decode(string));
    }

    /**
     * Decodes the given Base64 string into a byte array.
     *
     * @param bytes The bytes to decode.
     * @return The decoded byte array.
     */
    static byte[] fromSBase64(String bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    /**
     * Decodes the given Base64 buffer into a byte array.
     *
     * @param bytes The bytes to decode.
     * @return The decoded byte array.
     */
    static byte[] fromBase64(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    /**
     * Converts a Netty {@link ByteBuf} into a hex string.
     *
     * @param buffer The buffer to convert.
     * @return The hex string.
     */
    static String toHex(ByteBuf buffer) {
        return EncodingUtils.toHex(EncodingUtils.toByteArray(buffer));
    }

    /**
     * Converts a byte array into a hex string.
     *
     * @param buffer The bytes to convert.
     * @return The hex string.
     */
    static String toHex(byte[] buffer) {
        var builder = new StringBuilder();
        for (var b : buffer) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    /**
     * Converts a Netty {@link ByteBuf} into a plain byte array.
     *
     * @param buffer The buffer to convert.
     * @return The converted buffer.
     */
    static byte[] toByteArray(ByteBuf buffer) {
        var bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes);
        return bytes;
    }

    /**
     * Converts a byte array into a Netty {@link ByteBuf}.
     *
     * @param bytes The bytes to convert.
     * @return The converted bytes.
     */
    static ByteBuf toBuffer(byte[] bytes) {
        var buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes(bytes);
        return buffer;
    }

    /**
     * Serializes and encodes an object into a JSON string.
     *
     * @param object The object to serialize.
     * @return The serialized object.
     */
    static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * Decodes and deserializes a JSON string into an object.
     *
     * @param file The file to read the JSON from.
     * @param type The type of object to deserialize.
     * @return The deserialized object.
     * @throws IOException if the file could not be read.
     */
    static <T> T fromJson(File file, Type type) throws IOException {
        return gson.fromJson(new FileReader(file), type);
    }

    /**
     * Decodes and deserializes a JSON string into an object.
     *
     * @param reader The reader to read the JSON from.
     * @param type The type of object to deserialize.
     * @return The deserialized object.
     */
    static <T> T fromJson(Reader reader, Type type) {
        return gson.fromJson(reader, type);
    }

    /**
     * De-serializes and decodes a JSON string into an object.
     *
     * @param data The data to deserialize.
     * @param type The type of object to deserialize into.
     * @return The deserialized object.
     */
    static <T> T fromJson(String data, Type type) {
        return gson.fromJson(data, type);
    }

    /**
     * Decodes and deserializes a JSON string into an object.
     *
     * @param file The file to read the JSON from.
     * @param type The type of object to deserialize.
     * @return The deserialized object.
     * @throws IOException if the file could not be read.
     */
    static <T> T fromJson(File file, Class<T> type) throws IOException {
        return gson.fromJson(new FileReader(file), type);
    }

    /**
     * Decodes and deserializes a JSON string into an object.
     *
     * @param reader The reader to read the JSON from.
     * @param type The type of object to deserialize.
     * @return The deserialized object.
     */
    static <T> T fromJson(Reader reader, Class<T> type) {
        return gson.fromJson(reader, type);
    }

    /**
     * De-serializes and decodes a JSON string into an object.
     *
     * @param data The data to deserialize.
     * @param type The type of object to deserialize into.
     * @return The deserialized object.
     */
    static <T> T fromJson(String data, Class<T> type) {
        return gson.fromJson(data, type);
    }

    /**
     * De-serializes entries from a map into an object.
     *
     * @param object The object to deserialize into.
     * @param serialized The serialized entries.
     */
    static void deserializeTo(Object object, Map<String, Object> serialized) {
        serialized.forEach(
                (key, value) -> {
                    try {
                        // Get field data.
                        var field = object.getClass().getDeclaredField(key);
                        var fieldType = field.getType();
                        // De-serialize the value.
                        var deserialized = gson.fromJson(gson.toJson(value), fieldType);

                        // Check if the field type is a list.
                        if (fieldType == List.class) {
                            // Get the list type.
                            var listType = (ParameterizedType) field.getGenericType();
                            deserialized = gson.fromJson(gson.toJson(value), listType);
                        }

                        // Write data to the field.
                        field.setAccessible(true);
                        field.set(object, deserialized);
                    } catch (ReflectiveOperationException ignored) {
                    }
                });
    }

    /**
     * Converts milliseconds into seconds.
     *
     * @param millis The milliseconds to convert.
     * @return The converted milliseconds.
     */
    static String toSeconds(long millis) {
        return String.format("%.3f", millis / 1000.0);
    }

    /**
     * Converts a byte array into a byte string.
     *
     * @param bytes The bytes to convert.
     * @return The converted bytes.
     */
    static ByteString toByteString(byte[] bytes) {
        return ByteString.copyFrom(bytes);
    }

    /**
     * Converts a 64-bit number to a byte array.
     *
     * @param value The value to convert.
     * @return The converted value.
     */
    static byte[] toBytes(long value) {
        var bytes = new byte[8];
        bytes[0] = (byte) (value >> 56);
        bytes[1] = (byte) (value >> 48);
        bytes[2] = (byte) (value >> 40);
        bytes[3] = (byte) (value >> 32);
        bytes[4] = (byte) (value >> 24);
        bytes[5] = (byte) (value >> 16);
        bytes[6] = (byte) (value >> 8);
        bytes[7] = (byte) (value);
        return bytes;
    }
}
