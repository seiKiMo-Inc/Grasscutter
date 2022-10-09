package io.grasscutter;

import io.grasscutter.server.DedicatedServer;
import io.grasscutter.server.game.GameServer;
import io.grasscutter.server.http.HttpServer;
import io.grasscutter.utils.LanguageUtils;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.constants.Properties;
import io.grasscutter.utils.definitions.LanguageData;
import io.grasscutter.utils.objects.Configuration;
import io.grasscutter.utils.objects.lang.Language;
import io.grasscutter.utils.objects.lang.TextContainer;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.LogManager;

public final class Grasscutter {
    private static final long startupTime = System.currentTimeMillis();

    @Getter private static final Logger logger = LoggerFactory.getLogger(Grasscutter.class);
    @Getter private static final LineReader console = Grasscutter.createConsole();
    @Getter private static final Configuration config = new Configuration();

    @Getter private static DedicatedServer dedicatedServer;
    @Getter private static Language serverLanguage;

    private static boolean hasStarted = false;

    static {
        // Configure application logger.
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        System.setProperty("logback.configurationFile", "src/main/resources/logback.xml");

        // Set shutdown hook for the application.
        Runtime.getRuntime().addShutdownHook(new Thread(Grasscutter::shutdown));
    }

    /**
     * Entrypoint for the application. Priority can be found below. (top to bottom)
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        // Load the server language data.
        var languageData = Grasscutter.loadLanguage();
        if (!languageData.isValid()) {
            Grasscutter.getLogger().error("Failed to load language data.");
            return;
        }
        Grasscutter.serverLanguage = new Language(languageData);

        // Log a message to the console.
        Log.info(new TextContainer("system.startup.loading"));

        // Create server instances.
        var gameServer = GameServer.create();
        var httpServer = HttpServer.create();
        Grasscutter.dedicatedServer = new DedicatedServer(Grasscutter.logger, gameServer, httpServer);

        // Start the dedicated server.
        Grasscutter.dedicatedServer.start();

        // Log a message to the console.
        var startupTime = System.currentTimeMillis() - Grasscutter.startupTime;
        Log.info(new TextContainer("system.startup.done", startupTime));

        // Flatten language keys into memory.
        Grasscutter.serverLanguage.loadAllKeys();

        // Set the has started flag.
        Grasscutter.hasStarted = true;
    }

    /** Shutdown hook for the application. */
    public static void shutdown() {
        // Log message to the console.
        Grasscutter.getLogger().info("Shutting down Grasscutter...");
        // Check if the server successfully started.
        if (!Grasscutter.hasStarted) return;

        // Stop the dedicated server.
        Grasscutter.dedicatedServer.stop();
    }

    /**
     * Creates a {@link LineReader}, or "console" for the application.
     *
     * @return A {@link LineReader} instance.
     * @throws RuntimeException if something impossible happened. (no dumb terminal created)
     */
    @SneakyThrows(IOException.class)
    private static LineReader createConsole() {
        Terminal terminal;
        try {
            terminal = TerminalBuilder.builder().jna(true).build();
        } catch (IOException ignored) {
            // Try to get a dumb terminal.
            terminal = TerminalBuilder.builder().dumb(true).build();
        }

        return LineReaderBuilder.builder().terminal(terminal).build();
    }

    /**
     * Loads language data depending on: - the selected host system language - the preferred language
     * in the configuration
     *
     * @return Loaded language data.
     */
    private static LanguageData loadLanguage() {
        var language = Properties.LANGUAGE();
        var langCode = Locale.getDefault().toString();

        // Determine the language code to look for.
        if (language.preferred.equals("system")) {
            langCode = LanguageUtils.systemDefault();
        } else {
            langCode = language.preferred;
        }

        // Load the language data.
        var data = LanguageUtils.findLanguage(langCode);
        if (data == null) {
            Grasscutter.getLogger()
                    .warn("Could not find language data for language code '{}'.", langCode);
        }

        return data == null ? new LanguageData() : data;
    }
}
