package io.grasscutter.game.data;

import io.grasscutter.utils.FileUtils;

import java.nio.file.Path;

/* Defines a class as a game resource. */
public interface GameResource {
    /**
     * The resource ID.
     * @return A unique ID for the resource.
     */
    int getId();

    /**
     * Invoked when this resource loads.
     */
    void onLoad();

    /**
     * Returns a path to the game resource.
     *
     * @param file The file name.
     * @return The path to the game resource.
     */
    static Path getResourcePath(String file) {
        return FileUtils.getResourceFile(FileUtils.GAME_RESOURCES.get(), file);
    }

    /**
     * Returns a path to the excel data.
     *
     * @param file The file name.
     * @return The path to the excel data.
     */
    static Path getExcelPath(String file) {
        return FileUtils.getResourceFile(FileUtils.GAME_RESOURCES
                .get().resolve("ExcelBinOutput"), file);
    }
}
