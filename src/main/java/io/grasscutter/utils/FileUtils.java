package io.grasscutter.utils;

import io.grasscutter.Grasscutter;
import io.grasscutter.utils.interfaces.Serializable;
import java.io.File;
import java.nio.file.Files;
import java.util.Map;
import lombok.SneakyThrows;

/* Utility methods for accessing the file system. */
public interface FileUtils {
    /**
     * Reads a resource from the classpath.
     *
     * @param path The path to the resource.
     * @return The resource as a byte array.
     */
    static byte[] resource(String path) {
        try (var is = Grasscutter.class.getResourceAsStream("/" + path)) {
            return is == null ? new byte[0] : is.readAllBytes();
        } catch (Exception exception) {
            Grasscutter.getLogger().warn("Failed to read resource: " + path);
        }

        return new byte[0];
    }

    /**
     * Loads a file's data into an object.
     *
     * @param object A serializable object to load into.
     * @param file The file to read the data from.
     */
    @SneakyThrows
    static void loadFromFile(Serializable object, File file) {
        // De-serialize data from the file.
        Map<String, Object> data = EncodingUtils.fromJson(file, Serializable.SERIALIZE_TYPE);
        // Load the data into the object.
        object.deserialize(data);
    }

    /**
     * Saves an object's data to a file.
     *
     * @param object A serializable object to save.
     * @param file The file to save the data to.
     */
    @SneakyThrows
    static void saveToFile(Serializable object, File file) {
        // Serialize the object.
        Map<String, Object> data = object.serialize();
        var encoded = EncodingUtils.toJson(data);
        // Write the data to the file.
        Files.writeString(file.toPath(), encoded);
    }
}
