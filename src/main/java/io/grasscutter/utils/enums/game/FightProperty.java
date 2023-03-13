package io.grasscutter.utils.enums.game;

import io.grasscutter.utils.constants.CombatProperties;
import io.grasscutter.utils.definitions.game.CompoundProperty;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/* Game combat properties. */
@AllArgsConstructor
public enum FightProperty {
    FIGHT_PROP_NONE(0),
    FIGHT_PROP_BASE_HP(1),
    FIGHT_PROP_HP(2),
    FIGHT_PROP_HP_PERCENT(3),
    FIGHT_PROP_BASE_ATTACK(4),
    FIGHT_PROP_ATTACK(5),
    FIGHT_PROP_ATTACK_PERCENT(6),
    FIGHT_PROP_BASE_DEFENSE(7),
    FIGHT_PROP_DEFENSE(8),
    FIGHT_PROP_DEFENSE_PERCENT(9),
    FIGHT_PROP_BASE_SPEED(10),
    FIGHT_PROP_SPEED_PERCENT(11),
    FIGHT_PROP_HP_MP_PERCENT(12),
    FIGHT_PROP_ATTACK_MP_PERCENT(13),
    FIGHT_PROP_CRITICAL(20),
    FIGHT_PROP_ANTI_CRITICAL(21),
    FIGHT_PROP_CRITICAL_HURT(22),
    FIGHT_PROP_CHARGE_EFFICIENCY(23),
    FIGHT_PROP_ADD_HURT(24),
    FIGHT_PROP_SUB_HURT(25),
    FIGHT_PROP_HEAL_ADD(26),
    FIGHT_PROP_HEALED_ADD(27),
    FIGHT_PROP_ELEMENT_MASTERY(28),
    FIGHT_PROP_PHYSICAL_SUB_HURT(29),
    FIGHT_PROP_PHYSICAL_ADD_HURT(30),
    FIGHT_PROP_DEFENCE_IGNORE_RATIO(31),
    FIGHT_PROP_DEFENCE_IGNORE_DELTA(32),
    FIGHT_PROP_FIRE_ADD_HURT(40),
    FIGHT_PROP_ELEC_ADD_HURT(41),
    FIGHT_PROP_WATER_ADD_HURT(42),
    FIGHT_PROP_GRASS_ADD_HURT(43),
    FIGHT_PROP_WIND_ADD_HURT(44),
    FIGHT_PROP_ROCK_ADD_HURT(45),
    FIGHT_PROP_ICE_ADD_HURT(46),
    FIGHT_PROP_HIT_HEAD_ADD_HURT(47),
    FIGHT_PROP_FIRE_SUB_HURT(50),
    FIGHT_PROP_ELEC_SUB_HURT(51),
    FIGHT_PROP_WATER_SUB_HURT(52),
    FIGHT_PROP_GRASS_SUB_HURT(53),
    FIGHT_PROP_WIND_SUB_HURT(54),
    FIGHT_PROP_ROCK_SUB_HURT(55),
    FIGHT_PROP_ICE_SUB_HURT(56),
    FIGHT_PROP_EFFECT_HIT(60),
    FIGHT_PROP_EFFECT_RESIST(61),
    FIGHT_PROP_FREEZE_RESIST(62),
    FIGHT_PROP_TORPOR_RESIST(63),
    FIGHT_PROP_DIZZY_RESIST(64),
    FIGHT_PROP_FREEZE_SHORTEN(65),
    FIGHT_PROP_TORPOR_SHORTEN(66),
    FIGHT_PROP_DIZZY_SHORTEN(67),
    FIGHT_PROP_MAX_FIRE_ENERGY(70),
    FIGHT_PROP_MAX_ELEC_ENERGY(71),
    FIGHT_PROP_MAX_WATER_ENERGY(72),
    FIGHT_PROP_MAX_GRASS_ENERGY(73),
    FIGHT_PROP_MAX_WIND_ENERGY(74),
    FIGHT_PROP_MAX_ICE_ENERGY(75),
    FIGHT_PROP_MAX_ROCK_ENERGY(76),
    FIGHT_PROP_SKILL_CD_MINUS_RATIO(80),
    FIGHT_PROP_SHIELD_COST_MINUS_RATIO(81),
    FIGHT_PROP_CUR_FIRE_ENERGY(1000),
    FIGHT_PROP_CUR_ELEC_ENERGY(1001),
    FIGHT_PROP_CUR_WATER_ENERGY(1002),
    FIGHT_PROP_CUR_GRASS_ENERGY(1003),
    FIGHT_PROP_CUR_WIND_ENERGY(1004),
    FIGHT_PROP_CUR_ICE_ENERGY(1005),
    FIGHT_PROP_CUR_ROCK_ENERGY(1006),
    FIGHT_PROP_CUR_HP(1010),
    FIGHT_PROP_MAX_HP(2000),
    FIGHT_PROP_CUR_ATTACK(2001),
    FIGHT_PROP_CUR_DEFENSE(2002),
    FIGHT_PROP_CUR_SPEED(2003),
    FIGHT_PROP_NONEXTRA_ATTACK(3000),
    FIGHT_PROP_NONEXTRA_DEFENSE(3001),
    FIGHT_PROP_NONEXTRA_CRITICAL(3002),
    FIGHT_PROP_NONEXTRA_ANTI_CRITICAL(3003),
    FIGHT_PROP_NONEXTRA_CRITICAL_HURT(3004),
    FIGHT_PROP_NONEXTRA_CHARGE_EFFICIENCY(3005),
    FIGHT_PROP_NONEXTRA_ELEMENT_MASTERY(3006),
    FIGHT_PROP_NONEXTRA_PHYSICAL_SUB_HURT(3007),
    FIGHT_PROP_NONEXTRA_FIRE_ADD_HURT(3008),
    FIGHT_PROP_NONEXTRA_ELEC_ADD_HURT(3009),
    FIGHT_PROP_NONEXTRA_WATER_ADD_HURT(3010),
    FIGHT_PROP_NONEXTRA_GRASS_ADD_HURT(3011),
    FIGHT_PROP_NONEXTRA_WIND_ADD_HURT(3012),
    FIGHT_PROP_NONEXTRA_ROCK_ADD_HURT(3013),
    FIGHT_PROP_NONEXTRA_ICE_ADD_HURT(3014),
    FIGHT_PROP_NONEXTRA_FIRE_SUB_HURT(3015),
    FIGHT_PROP_NONEXTRA_ELEC_SUB_HURT(3016),
    FIGHT_PROP_NONEXTRA_WATER_SUB_HURT(3017),
    FIGHT_PROP_NONEXTRA_GRASS_SUB_HURT(3018),
    FIGHT_PROP_NONEXTRA_WIND_SUB_HURT(3019),
    FIGHT_PROP_NONEXTRA_ROCK_SUB_HURT(3020),
    FIGHT_PROP_NONEXTRA_ICE_SUB_HURT(3021),
    FIGHT_PROP_NONEXTRA_SKILL_CD_MINUS_RATIO(3022),
    FIGHT_PROP_NONEXTRA_SHIELD_COST_MINUS_RATIO(3023),
    FIGHT_PROP_NONEXTRA_PHYSICAL_ADD_HURT(3024);

    private static final Int2ObjectMap<FightProperty> map = new Int2ObjectOpenHashMap<>();
    private static final Map<String, FightProperty> stringMap = new HashMap<>();

    /* Bindings from shorthands -> fight properties. */
    private static final Map<String, FightProperty> SHORT = Map.ofEntries(
            // Normal relic stats
            Map.entry("hp", FIGHT_PROP_HP),
            Map.entry("atk", FIGHT_PROP_ATTACK),
            Map.entry("def", FIGHT_PROP_DEFENSE),
            Map.entry("hp%", FIGHT_PROP_HP_PERCENT),
            Map.entry("atk%", FIGHT_PROP_ATTACK_PERCENT),
            Map.entry("def%", FIGHT_PROP_DEFENSE_PERCENT),
            Map.entry("em", FIGHT_PROP_ELEMENT_MASTERY),
            Map.entry("er", FIGHT_PROP_CHARGE_EFFICIENCY),
            Map.entry("hb", FIGHT_PROP_HEAL_ADD),
            Map.entry("heal", FIGHT_PROP_HEAL_ADD),
            Map.entry("cd", FIGHT_PROP_CRITICAL_HURT),
            Map.entry("cdmg", FIGHT_PROP_CRITICAL_HURT),
            Map.entry("cr", FIGHT_PROP_CRITICAL),
            Map.entry("crate", FIGHT_PROP_CRITICAL),
            Map.entry("phys%", FIGHT_PROP_PHYSICAL_ADD_HURT),
            Map.entry("dendro%", FIGHT_PROP_GRASS_ADD_HURT),
            Map.entry("geo%", FIGHT_PROP_ROCK_ADD_HURT),
            Map.entry("anemo%", FIGHT_PROP_WIND_ADD_HURT),
            Map.entry("hydro%", FIGHT_PROP_WATER_ADD_HURT),
            Map.entry("cryo%", FIGHT_PROP_ICE_ADD_HURT),
            Map.entry("electro%", FIGHT_PROP_ELEC_ADD_HURT),
            Map.entry("pyro%", FIGHT_PROP_FIRE_ADD_HURT),
            // Other stats
            Map.entry("maxhp", FIGHT_PROP_MAX_HP),
            Map.entry("dmg", FIGHT_PROP_ADD_HURT),  // This seems to get reset after attacks
            Map.entry("cdr", FIGHT_PROP_SKILL_CD_MINUS_RATIO),
            Map.entry("heali", FIGHT_PROP_HEALED_ADD),
            Map.entry("shield", FIGHT_PROP_SHIELD_COST_MINUS_RATIO),
            Map.entry("defi", FIGHT_PROP_DEFENCE_IGNORE_RATIO),
            Map.entry("resall", FIGHT_PROP_SUB_HURT),  // This seems to get reset after attacks
            Map.entry("resanemo", FIGHT_PROP_WIND_SUB_HURT),
            Map.entry("rescryo", FIGHT_PROP_ICE_SUB_HURT),
            Map.entry("resdendro", FIGHT_PROP_GRASS_SUB_HURT),
            Map.entry("reselectro", FIGHT_PROP_ELEC_SUB_HURT),
            Map.entry("resgeo", FIGHT_PROP_ROCK_SUB_HURT),
            Map.entry("reshydro", FIGHT_PROP_WATER_SUB_HURT),
            Map.entry("respyro", FIGHT_PROP_FIRE_SUB_HURT),
            Map.entry("resphys", FIGHT_PROP_PHYSICAL_SUB_HURT)
    );

    @Getter private final int id;

    static {
        // Cache the values.
        Stream.of(FightProperty.values()).forEach(element -> {
            map.put(element.getId(), element);
            stringMap.put(element.name(), element);
        });
    }

    /**
     * Fetches a property by its ID.
     *
     * @param value The ID of the property.
     * @return The property.
     */
    public static FightProperty fetch(int value) {
        return map.getOrDefault(value, FIGHT_PROP_NONE);
    }

    /**
     * Fetches a property by its name.
     *
     * @param value The name of the property.
     * @return The property.
     */
    public static FightProperty fetch(String value) {
        return stringMap.getOrDefault(value, FIGHT_PROP_NONE);
    }

    /**
     * Fetches a property by its short name.
     *
     * @param value The short name of the property.
     * @return The property.
     */
    public static FightProperty fetchShort(String value) {
        return SHORT.getOrDefault(value, FIGHT_PROP_NONE);
    }

    /**
     * Returns a set of all short names.
     *
     * @return A set of all short names.
     */
    public static Set<String> getShortNames() {
        return SHORT.keySet();
    }

    /**
     * Fetches a compound property by its result.
     *
     * @param result The result of the compound property.
     * @return The compound property.
     */
    public static CompoundProperty fetch(FightProperty result) {
        return CombatProperties.COMPOUND.get(result);
    }

    /**
     * Fetches all compound properties.
     *
     * @param consumer The consumer to accept the properties.
     */
    public static void fetch(Consumer<CompoundProperty> consumer) {
        CombatProperties.COMPOUND.values().forEach(consumer);
    }

    /**
     * Returns whether the property is a percentage.
     *
     * @param property The property.
     * @return {@code true} if so, {@code false} if not.
     */
    public static boolean isPercentage(FightProperty property) {
        return !CombatProperties.FLAT.contains(property);
    }
}
