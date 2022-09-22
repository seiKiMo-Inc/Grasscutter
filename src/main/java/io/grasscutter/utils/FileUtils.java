package io.grasscutter.utils;

import io.grasscutter.Grasscutter;

/* Utility methods for accessing the file system. */
public interface FileUtils {
    /**
     * Reads a resource from the classpath.
     * @param path The path to the resource.
     * @return The resource as a byte array.
     */
    static byte[] resource(String path) {
        try (var is = Grasscutter.class.getResourceAsStream(path)) {
            return is == null ? new byte[0] : is.readAllBytes();
        } catch (Exception exception) {
            Grasscutter.getLogger().warn("Failed to read resource: " + path);
        }

        return new byte[0];
    }
}