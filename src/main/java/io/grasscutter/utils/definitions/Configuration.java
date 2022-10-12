package io.grasscutter.utils.definitions;

import com.google.gson.annotations.SerializedName;
import io.grasscutter.utils.enums.DataInterface;
import io.grasscutter.utils.interfaces.Serializable;

/* Container for server properties. */
public final class Configuration implements Serializable {
    public Server server = new Server(); // All server properties.
    public Database database = new Database(); // All database properties.
    public Language language = new Language(); // All language properties.

    public static class Server {
        public RunMode runAs = RunMode.HYBRID; // What the server runs as. Default: Hybrid.
        public Game gameServer = new Game(); // Game server properties.
        public Http httpServer = new Http(); // HTTP server properties.
    }

    public static class Game {
        public String bindAddress =
                "0.0.0.0"; // What address the server binds to. Default: All interfaces. (0.0.0.0)
        public int bindPort = 22102; // What port the server binds to. Default: 22102.
    }

    public static class Http {
        public Encryption encryption = new Encryption(); // All encryption properties.
        public String bindAddress =
                "0.0.0.0"; // What address the server binds to. Default: All interfaces. (0.0.0.0)
        public int bindPort = 80; // What port the server binds to. Default: 80.
    }

    public static class Encryption {
        public boolean useSsl = false; // Whether to use SSL/TLS. Default: false.
        public boolean inRouting =
                false; // Whether 'https' should be included in routes. Default: false.
        public String keyStore = ""; // The path to the key store. Default: "".
        public String keyStorePassword = "123456"; // The password to the key store. Default: "".
    }

    public static class Database {
        @SerializedName("interface")
        public DataInterface interfaceType =
                DataInterface.MONGODB; // What database interface to use. Default: MongoDB.

        public String host = "localhost"; // What host the database is on. Default: localhost.
        public int port = 27017; // What port the database is on. Default: 27017.
        public String database = "grasscutter"; // What database to use. Default: grasscutter.
        public String username = "grasscutter"; // What username to use. Default: grasscutter.
        public String password = "grasscutter"; // What password to use. Default: grasscutter.

        public String overrideUri = ""; // Override the connection URI. Default: "".
    }

    public static class Language {
        public String preferred = "system"; // The language to use. Default: system.
        public boolean force = false; // Whether to force the language. Default: false.
        public boolean unknown =
                true; // Allow using languages specified in `~/languages/`  Default: true.
    }

    public enum RunMode {
        GAME,
        DISPATCH,
        HYBRID
    }
}