package io.grasscutter;

import io.grasscutter.server.DedicatedServer;
import io.grasscutter.server.game.GameServer;
import io.grasscutter.server.http.HttpServer;
import io.grasscutter.server.http.Router;
import io.grasscutter.utils.EncodingUtils;
import io.grasscutter.utils.FileUtils;
import io.grasscutter.utils.LanguageUtils;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.constants.Properties;
import io.grasscutter.utils.definitions.Configuration;
import io.grasscutter.utils.definitions.LanguageData;
import io.grasscutter.utils.exceptions.CommandException;
import io.grasscutter.utils.objects.lang.Language;
import io.grasscutter.utils.objects.lang.TextContainer;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.LogManager;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public final class Grasscutter {
    private static final long startupTime = System.currentTimeMillis();

    @Getter private static final Logger logger = LoggerFactory.getLogger("Grasscutter");
    @Getter private static final LineReader console = Grasscutter.createConsole();
    @Getter private static final Configuration config = new Configuration();

    @Getter private static DedicatedServer dedicatedServer;
    @Getter @Setter private static Language serverLanguage;

    private static boolean hasStarted = false;

    static {
        // Configure application logger.
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        System.setProperty("logback.configurationFile", "src/main/resources/logback.xml");
        // Check if debug is enabled.
        if (Properties.DEBUG) {
            System.setProperty("log.level", "DEBUG");
        }

        // Load the server configuration.
        Grasscutter.loadConfig();

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
        if (Properties.DEBUG) Log.debug(new TextContainer("system.debug_enabled"));
        Log.info(new TextContainer("system.startup.loading"));

        // Set references.
        FileUtils.setReferences();

        // Create server instances.
        var gameServer = GameServer.create();
        var httpServer = HttpServer.create();
        Grasscutter.dedicatedServer = new DedicatedServer(Grasscutter.logger, gameServer, httpServer);

        // Start the dedicated server.
        Grasscutter.dedicatedServer.start();

        // Log a message to the console.
        var startupTime = System.currentTimeMillis() - Grasscutter.startupTime;
        var formattedTime = EncodingUtils.toSeconds(startupTime);
        Log.info(new TextContainer("system.startup.done", formattedTime));

        // Flatten language keys into memory.
        Grasscutter.serverLanguage.loadAllKeys();

        // Set the has started flag.
        Grasscutter.hasStarted = true;
        // Call for additional setup.
        Grasscutter.additionalSetup();
    }

    /** Shutdown hook for the application. */
    public static void shutdown() {
        // Log message to the console.
        Log.info(new TextContainer("system.shutdown"));
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

    /** Sets up the console for input. */
    private static void setupConsole() {
        while (true) {
            try {
                var line = Grasscutter.console.readLine("> ");
                if (line == null) continue;

                // Check if the line is empty.
                if (line.isEmpty()) continue;
                var content = line.trim().split(" ");

                // Handle the line as a command.
                var label = content[0];
                var args = new ArrayList<>(Arrays.asList(content).subList(1, content.length));
                DedicatedServer.getInstance().getCommandMap().execute(label, args);
            } catch (UserInterruptException ignored) {
                // Ignore this exception.
            } catch (EndOfFileException ignored) {
                // Ignore this exception.
            } catch (CommandException commandException) {
                Log.error(commandException.getLocalizedMessage());
            } catch (IOError ioException) {
                Log.error(new TextContainer("system.console_error"), ioException);
                return;
            } catch (Exception exception) {
                Log.error(new TextContainer("system.console_error"), exception);
            }
        }
    }

    /**
     * Loads language data depending on: - the selected host system language - the preferred language
     * in the configuration
     *
     * @return Loaded language data.
     */
    public static LanguageData loadLanguage() {
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

    /** Loads the server configuration file. */
    public static void loadConfig() {
        // Check if the configuration file exists.
        var config = new File(System.getProperty("user.dir"), "config.json");
        if (!config.exists()) {
            // Save the default configuration.
            Grasscutter.getLogger().warn("No configuration file found, creating a default one.");
            FileUtils.saveToFile(Grasscutter.config, config);
            return;
        }

        // Load the configuration file.
        FileUtils.loadFromFile(Grasscutter.config, config);
    }

    /** Performs additional setup for the application. This is called after the server has started. */
    public static void additionalSetup() {
        // Register default HTTP routes.
        Router.defaultSetup(DedicatedServer.getHttpServer());

        // Start the console.
        Grasscutter.setupConsole();
    }
}
