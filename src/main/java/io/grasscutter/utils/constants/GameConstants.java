package io.grasscutter.utils.constants;

import io.grasscutter.world.Position;

/* Constants seen in game-related handling. */
public interface GameConstants {
    // The server "region".
    String REGION = "hk4e_global";

    // The current game version.
    String GAME_VERSION = "3.3.0";
    // The game version random key.
    String VERSION_KEY = "c25-314dd05b0b5f";

    // The hash code of the ability data.
    int ABILITY_HASH_CODE = 1844674;
    // The main characters. (by gender)
    int MAIN_CHARACTER_MALE = 10000005;
    int MAIN_CHARACTER_FEMALE = 10000007;

    // This is where the player starts when first created.
    Position START = new Position(2747, 194, -1719);

    // A constant for an 'Infinite' value.
    int Infinity = Integer.MAX_VALUE;
}
