package io.grasscutter.utils;

import io.grasscutter.Grasscutter;
import io.grasscutter.utils.constants.FileConstants;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.constants.Properties;
import io.grasscutter.utils.interfaces.Serializable;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

import io.grasscutter.utils.objects.lang.TextContainer;
import lombok.SneakyThrows;

/* Utility methods for accessing the file system. */
public interface FileUtils {
    AtomicReference<Path> GAME_RESOURCES = new AtomicReference<>();

    /**
     * Sets the references for the constants in this file.
     */
    static void setReferences() {
        GAME_RESOURCES.set(FileUtils.getGameResourcesPath());
    }

    /*
     * Path methods.
     */

    /**
     * Gets the path to the game resources location.
     * Loads the resources from a ZIP file.
     *
     * @return The path to the game resources location.
     */
    @SneakyThrows
    static Path getGameResourcesPath() {
        var resourcesPath = Path.of(Properties.SERVER().resources);
        // Check if the path exists.
        if (!Files.exists(resourcesPath)) {
            Log.error(new TextContainer("server.dedicated.resources.no_file"));
            Log.info(new TextContainer("server.dedicated.resources.add_resources",
                    resourcesPath.getFileName()));
            System.exit(1);
        }

        var fileSystem = FileSystems.newFileSystem(resourcesPath);
        var root = fileSystem.getPath("");

        try (var path = Files.find(root, 3, (p, a) -> {
            var file = p.getFileName();
            if (file == null) return false;
            return file.toString().equals("ExcelBinOutput");
        })) {
            var outputPath = path.findFirst().orElseThrow();
            resourcesPath = outputPath.getParent();
            if (resourcesPath == null) resourcesPath = root;
        } catch (NoSuchElementException ignored) {
            Log.error(new TextContainer("server.dedicated.resources.no_resources"));
        } catch (IOException ignored) {
            Log.error(new TextContainer("server.dedicated.resources.zip_error"));
        }

        return resourcesPath;
    }

    /*
     * File methods.
     */

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

    /**
     * Returns the path to a resource file.
     * Loads the resource from a TSJ, JSON, or TSV file.
     *
     * @param root The root path to the resource.
     * @param file The file name of the resource.
     * @return The path to the resource file.
     */
    static Path getResourceFile(Path root, String file) {
        var name = FileUtils.withoutExtension(file);
        for (var extension : FileConstants.RESOURCE_EXTENSIONS) {
            var to = root.resolve(name + "." + extension);
            if (Files.exists(to)) return to;
        }

        // Fallback for file creation.
        return root.resolve(name + ".tsj");
    }


    /**
     * Returns the extension of a file.
     *
     * @param file The file name.
     * @return The extension of the file.
     */
    static String extension(Path file) {
        return extension(file.toString());
    }

    /**
     * Returns the extension of a file.
     *
     * @param file The file name.
     * @return The extension of the file.
     */
    static String extension(String file) {
        var index = file.lastIndexOf('.');
        return index == -1 ? "" : file.substring(index + 1);
    }

    /**
     * Returns the name of a file.
     *
     * @param file The file name.
     * @return The name of the file.
     */
    static String withoutExtension(String file) {
        var index = file.lastIndexOf('.');
        return index == -1 ? file : file.substring(0, index);
    }
}
