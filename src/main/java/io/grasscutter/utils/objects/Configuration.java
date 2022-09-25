package io.grasscutter.utils.objects;

import io.grasscutter.utils.interfaces.Serializable;

/* Container for server properties. */
public final class Configuration implements Serializable {
    public Server server = new Server(); // All server properties.

    public static class Server {
        public RunMode runAs = RunMode.GAME; // What the server runs as. Default: Game.
        public Game gameServer = new Game(); // Game server properties.
    }

    public static class Game {
        public String bindAddress =
                "0.0.0.0"; // What address the server binds to. Default: All interfaces. (0.0.0.0)
        public int bindPort = 22102; // What port the server binds to. Default: 22102.
    }

    public enum RunMode {
        GAME,
        DISPATCH,
        HYBRID
    }
}
