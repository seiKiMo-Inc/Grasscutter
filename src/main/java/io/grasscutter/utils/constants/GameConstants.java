package io.grasscutter.utils.constants;

import io.grasscutter.world.Position;

/* Constants seen in game-related handling. */
public interface GameConstants {
    // The current game version.
    String GAME_VERSION = "3.3.0";
    // The game version random key.
    String VERSION_KEY = "c25-314dd05b0b5f";

    // This is where the player starts when first created.
    Position START = new Position(2747, 194, -1719);
}
