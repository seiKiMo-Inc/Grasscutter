package io.grasscutter.utils.constants;

import io.grasscutter.Grasscutter;
import io.grasscutter.utils.FileUtils;
import io.grasscutter.utils.objects.Configuration;

import java.io.File;

/* Utility for fetching config properties. */
public interface Properties {
    File CONFIG = new File(System.getProperty("user.dir") + "/config.json");

    /** Reloads the server's configuration from file. */
    static void reload() {
        FileUtils.loadFromFile(Grasscutter.getConfig(), CONFIG);
    }

    /** Saves the server's configuration to file. */
    static void saveConfig() {
        FileUtils.saveToFile(Grasscutter.getConfig(), CONFIG);
    }

    /** Gets the currently loaded server configuration. */
    static Configuration CONFIGURATION() {
        return Grasscutter.getConfig();
    }

    /**
     * @return Server properties.
     */
    static Configuration.Server SERVER() {
        return CONFIGURATION().server;
    }

    /**
     * @return Database properties.
     */
    static Configuration.Database DATABASE() {
        return CONFIGURATION().database;
    }

    /**
     * @return Language properties.
     */
    static Configuration.Language LANGUAGE() {
        return CONFIGURATION().language;
    }
}
