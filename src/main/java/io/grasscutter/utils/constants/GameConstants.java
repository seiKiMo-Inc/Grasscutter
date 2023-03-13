package io.grasscutter.utils.constants;

import io.grasscutter.proto.SceneEntityAiInfoOuterClass.SceneEntityAiInfo;
import io.grasscutter.proto.VectorOuterClass.Vector;
import io.grasscutter.utils.ServerUtils;
import io.grasscutter.world.Position;

import java.util.stream.Stream;

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
    // The amount of teams for a player.
    int TEAM_COUNT = 4;
    // The block size of a grid.
    int[] BLOCK_SIZE = {50, 500};

    // This is where the player starts when first created.
    Position START = new Position(2747, 194, -1719);
    // The default rotation of the player.
    Position DEFAULT_ROTATION = new Position(0, 307, 0);

    // A constant for an 'Infinite' value.
    int Infinity = Integer.MAX_VALUE;
    // The default entity AI value.
    SceneEntityAiInfo.Builder DEFAULT_AI = SceneEntityAiInfo.newBuilder()
            .setIsAiOpen(true).setBornPos(Vector.newBuilder());

    // Default avatar abilities.
    int[] DEFAULT_ABILITIES = Stream.of("Avatar_DefaultAbility_VisionReplaceDieInvincible", "Avatar_DefaultAbility_AvartarInShaderChange", "Avatar_SprintBS_Invincible",
            "Avatar_Freeze_Duration_Reducer", "Avatar_Attack_ReviveEnergy", "Avatar_Component_Initializer", "Avatar_FallAnthem_Achievement_Listener")
            .map(ServerUtils::hashAbility).mapToInt(Integer::intValue).toArray();
    // The default ability hash.
    int DEFAULT_ABILITY = ServerUtils.hashAbility("Default");
}
