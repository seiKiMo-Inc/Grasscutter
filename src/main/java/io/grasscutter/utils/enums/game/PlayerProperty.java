package io.grasscutter.utils.enums.game;

import static io.grasscutter.utils.constants.GameConstants.Infinity;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PlayerProperty {
    PROP_NONE(0),
    PROP_EXP(1001, 0),
    PROP_BREAK_LEVEL(1002),
    PROP_SATIATION_VAL(1003),
    PROP_SATIATION_PENALTY_TIME(1004),
    PROP_LEVEL(4001, 0, 90),
    PROP_LAST_CHANGE_AVATAR_TIME(10001),
    PROP_MAX_SPRING_VOLUME(10002, 0, 8_500_000), // Maximum HP "storable" in a statue. [0, 8500000]
    PROP_CUR_SPRING_VOLUME(
            10003, true), // Current HP "stored" in a statue. [0, PROP_MAX_SPRING_VOLUME]
    PROP_IS_SPRING_AUTO_USE(10004, 0, 1), // Automatic nearby HP recovery. (boolean) [0, 1]
    PROP_SPRING_AUTO_USE_PERCENT(
            10005, 0, 100), // Maximum amount of HP to recover. (percentage) [0, 100]
    PROP_IS_FLYABLE(10006, 0, 1), // Are you in a state that disables your glider? (boolean) [0, 1]
    PROP_IS_WEATHER_LOCKED(10007, 0, 1),
    PROP_IS_GAME_TIME_LOCKED(10008, 0, 1),
    PROP_IS_TRANSFERABLE(10009, 0, 1),
    PROP_MAX_STAMINA(10010, 0, 24_000), // Player maximum stamina. (0 - 24000)
    PROP_CUR_PERSIST_STAMINA(10011, true), // Player used stamina. (0 - PROP_MAX_STAMINA)
    PROP_CUR_TEMPORARY_STAMINA(10012),
    PROP_PLAYER_LEVEL(10013, 1, 60),
    PROP_PLAYER_EXP(10014),
    PROP_PLAYER_HCOIN(10015), // Primogem count. (-inf, +inf)
    PROP_PLAYER_SCOIN(10016, 0), // Mora count. [0, +inf]
    PROP_PLAYER_MP_SETTING_TYPE(
            10017, 0, 2), // Player co-op join setting. [0 = never, 1 = always, 2 = by approval]
    PROP_IS_MP_MODE_AVAILABLE(10018, 0, 1), // Player co-op enabled. [0, 1]
    PROP_PLAYER_WORLD_LEVEL(10019, 0, 8), // Player world level. [0, 8]
    PROP_PLAYER_RESIN(
            10020, 0,
            2000), // Player stored original resin. [0, 160, 2000] - note that values above 160 require
    // refills
    PROP_PLAYER_WAIT_SUB_HCOIN(10022),
    PROP_PLAYER_WAIT_SUB_SCOIN(10023),
    PROP_IS_ONLY_MP_WITH_PS_PLAYER(
            10024, 0, 1), // Force co-op only with PlayStation users? (boolean) [0, 1]
    PROP_PLAYER_MCOIN(10025), // Genesis Crystal count. (-inf, +inf)
    PROP_PLAYER_WAIT_SUB_MCOIN(10026),
    PROP_PLAYER_LEGENDARY_KEY(10027, 0),
    PROP_IS_HAS_FIRST_SHARE(10028),
    PROP_PLAYER_FORGE_POINT(10029, 0, 300_000),
    PROP_CUR_CLIMATE_METER(10035),
    PROP_CUR_CLIMATE_TYPE(10036),
    PROP_CUR_CLIMATE_AREA_ID(10037),
    PROP_CUR_CLIMATE_AREA_CLIMATE_TYPE(10038),
    PROP_PLAYER_WORLD_LEVEL_LIMIT(10039),
    PROP_PLAYER_WORLD_LEVEL_ADJUST_CD(10040),
    PROP_PLAYER_LEGENDARY_DAILY_TASK_NUM(10041),
    PROP_PLAYER_HOME_COIN(10042, 0), // Realm currency count. [0, +inf]
    PROP_PLAYER_WAIT_SUB_HOME_COIN(10043);

    private static final Int2ObjectMap<PlayerProperty> map = new Int2ObjectOpenHashMap<>();

    @Getter private final int id, min, max;
    @Getter private final boolean dynamicRange;

    static {
        // Cache the values.
        Stream.of(PlayerProperty.values()).forEach(element -> map.put(element.getId(), element));
    }

    /**
     * Properties with a fixed, infinite maximum value.
     *
     * @param id The ID of the PlayerProperty.
     * @param min The minimum value of the PlayerProperty.
     */
    PlayerProperty(int id, int min) {
        this(id, min, Infinity, false);
    }

    /**
     * Properties with a fixed, finite range.
     *
     * @param id The ID of the PlayerProperty.
     * @param min The minimum value of the PlayerProperty.
     * @param max The maximum value of the PlayerProperty.
     */
    PlayerProperty(int id, int min, int max) {
        this(id, min, max, false);
    }

    /**
     * Properties with a fixed, finite range of the integer spectrum.
     *
     * @param id The ID of the PlayerProperty.
     */
    PlayerProperty(int id) {
        this(id, Integer.MIN_VALUE, Infinity, false);
    }

    /**
     * Properties with a dynamic range.
     *
     * @param id The ID of the PlayerProperty.
     * @param dynamicRange Whether the range is dynamic.
     */
    PlayerProperty(int id, boolean dynamicRange) {
        this(id, Integer.MIN_VALUE, Infinity, dynamicRange);
    }

    /**
     * Fetches the PlayerProperty by its ID.
     *
     * @param value The ID of the PlayerProperty.
     * @return The PlayerProperty.
     */
    public static PlayerProperty fetch(int value) {
        return map.getOrDefault(value, null);
    }
}
