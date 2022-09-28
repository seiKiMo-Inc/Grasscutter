package io.grasscutter.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;

/* Utility methods seen when converting data. */
public interface EncodingUtils {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Encodes the given byte array into a Base64 string.
     *
     * @param bytes The bytes to encode.
     * @return The encoded string.
     */
    static byte[] toBase64(byte[] bytes) {
        return Base64.getEncoder().encode(bytes);
    }

    /**
     * Decodes the given Base64 string into a byte array.
     *
     * @param bytes The bytes to decode.
     * @return The decoded byte array.
     */
    static byte[] fromBase64(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
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
     * De-serializes and decodes a JSON string into an object.
     *
     * @param data The data to deserialize.
     * @param type The type of object to deserialize into.
     * @return The deserialized object.
     */
    static <T> T fromJson(String data, Class<T> type) {
        return gson.fromJson(data, type);
    }
}
