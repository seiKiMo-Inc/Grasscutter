package io.grasscutter.utils;

import io.grasscutter.Grasscutter;
import io.grasscutter.utils.definitions.LanguageData;

import java.io.File;
import java.io.IOException;

/* Utility methods seen in language processing. */
public interface LanguageUtils {
    /**
     * Returns the system default language.
     *
     * @return System default language.
     */
    static String systemDefault() {
        return System.getProperty("user.language") + "-" + System.getProperty("user.country");
    }

    /**
     * Attempts to find the specified language. NOTE: This method uses the root logger.
     *
     * @param locale Locale to find.
     * @return Language data.
     */
    static LanguageData findLanguage(String locale) {
        locale = locale.toLowerCase(); // Normalize locale.
        var fileName = locale + ".json"; // Build file name.

        // Attempt to locale from resource.
        var resource = FileUtils.resource("lang/" + fileName);
        if (resource.length > 0) {
            // Decode the language data & return the resulting object.
            return EncodingUtils.fromJson(new String(resource), LanguageData.class);
        }

        // Attempt to load locale from file.
        var file = new File("lang/" + fileName);
        if (file.exists()) {
            try {
                // Decode the language data & return the resulting object.
                return EncodingUtils.fromJson(file, LanguageData.class);
            } catch (IOException exception) {
                Grasscutter.getLogger().error("Unable to load language file: " + fileName, exception);
            }
        }

        // Check if the language is the 'en-us' fallback.
        if (locale.equals("en-us")) return null;

        // Attempt to find the 'en-us' language.
        return LanguageUtils.findLanguage("en-us");
    }
}
