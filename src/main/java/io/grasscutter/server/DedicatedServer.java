package io.grasscutter.server;

import io.grasscutter.Grasscutter;
import io.grasscutter.auth.AuthenticationHolder;
import io.grasscutter.commands.CommandMap;
import io.grasscutter.commands.sender.CommandSender;
import io.grasscutter.commands.sender.ConsoleCommandSender;
import io.grasscutter.data.DataInterface;
import io.grasscutter.server.game.GameServer;
import io.grasscutter.server.http.HttpServer;
import io.grasscutter.utils.EncodingUtils;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.constants.Properties;
import io.grasscutter.utils.objects.lang.Language;
import io.grasscutter.utils.objects.lang.TextContainer;
import io.grasscutter.utils.objects.text.Text;
import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Arrays;
import lombok.Getter;
import org.slf4j.Logger;

/* Manages server-related aspects. */
public final class DedicatedServer {
    @Getter private static DedicatedServer instance;
    @Getter private static GameServer gameServer;
    @Getter private static HttpServer httpServer;

    @Getter private final Logger logger;
    @Getter private final AuthenticationHolder authHolder;
    @Getter private final CommandMap commandMap;
    @Getter private final CommandSender serverSender;
    private final ServerThread thread;

    @Getter private DataInterface dataInterface;

    public DedicatedServer(Logger logger, GameServer gameServer, HttpServer httpServer) {
        // Set the dedicated servers.
        DedicatedServer.gameServer = gameServer;
        DedicatedServer.httpServer = httpServer;

        // Set the dedicated server instance.
        DedicatedServer.instance = this;
        // Set the dedicated server logger.
        this.logger = logger;

        // Create the server thread.
        this.thread = new ServerThread(this);
        // Initialize the database.
        this.initializeDatabase();

        // Create server objects.
        this.authHolder = new AuthenticationHolder();
        this.commandMap = new CommandMap();
        this.serverSender = new ConsoleCommandSender();
    }

    /** Performs a global server tick. */
    public void tick() {}

    /** Starts both game & HTTP servers. */
    public void start() {
        // Start the dedicated servers.
        DedicatedServer.gameServer.start();
        DedicatedServer.httpServer.start();

        // Start the dedicated server thread.
        this.thread.start();
    }

    /** Stops both game & HTTP servers. */
    public void stop() {
        // Stop the dedicated servers.
        DedicatedServer.gameServer.interrupt();
        DedicatedServer.httpServer.stop();

        // Stop the dedicated server thread.
        this.thread.end();
        // Perform database operations.
        this.dataInterface.save();
        this.dataInterface.disconnect();
    }

    /** Performs a server reload. */
    public void reload() {
        Log.info(
                this.logger,
                new Text(new TextContainer("server.dedicated.reload.start")).color(Color.YELLOW));
        var startupTime = System.currentTimeMillis();

        // Reload the server internals.
        Grasscutter.loadConfig();

        var languageData = Grasscutter.loadLanguage();
        if (!languageData.isValid()) {
            Grasscutter.getLogger().error("Failed to load language data.");
            return;
        }
        Grasscutter.setServerLanguage(new Language(languageData));

        // Reload the dedicated servers.
        DedicatedServer.gameServer.reload();
        DedicatedServer.httpServer.reload();
        // Reload the database.
        this.dataInterface.save();
        this.dataInterface.initialize();

        // Log the time it took to reload.
        var time = EncodingUtils.toSeconds(System.currentTimeMillis() - startupTime);
        Log.info(
                this.logger,
                new Text(new TextContainer("server.dedicated.reload.done", time)).color(Color.YELLOW));
    }

    /**
     * Dumps information about all active threads.
     *
     * @return {@link ThreadInfo} as a string.
     */
    public String dumpThreads() {
        var threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);

        var builder = new StringBuilder();
        Arrays.stream(threads).forEach(thread -> builder.append('\n').append(thread));

        return builder.toString();
    }

    /** Establishes a connection to the configured database. */
    public void initializeDatabase() {
        // Get the database configuration.
        var database = Properties.DATABASE();

        try {
            // Get the interface class.
            var interfaceClass = database.interfaceType.getInterfaceClass();
            // Create a new instance of the interface class.
            this.dataInterface = interfaceClass.getDeclaredConstructor().newInstance();

            // Connect to the database.
            this.dataInterface.connect();
            // Initialize the database.
            this.dataInterface.initialize();
        } catch (Exception exception) {
            Log.error(this.logger, new TextContainer("database.initialize_error"), exception);
        }
    }
}
