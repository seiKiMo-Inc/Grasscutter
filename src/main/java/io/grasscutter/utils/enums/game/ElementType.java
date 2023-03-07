package io.grasscutter.utils.enums.game;

import io.grasscutter.utils.ServerUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public enum ElementType {
    None		(0, FightProperty.FIGHT_PROP_CUR_FIRE_ENERGY, FightProperty.FIGHT_PROP_MAX_FIRE_ENERGY),
    Fire		(1, FightProperty.FIGHT_PROP_CUR_FIRE_ENERGY, FightProperty.FIGHT_PROP_MAX_FIRE_ENERGY, 10101, "TeamResonance_Fire_Lv2", 2),
    Water		(2, FightProperty.FIGHT_PROP_CUR_WATER_ENERGY, FightProperty.FIGHT_PROP_MAX_WATER_ENERGY, 10201, "TeamResonance_Water_Lv2", 3),
    Grass		(3, FightProperty.FIGHT_PROP_CUR_GRASS_ENERGY, FightProperty.FIGHT_PROP_MAX_GRASS_ENERGY, 10501, "TeamResonance_Grass_Lv2", 8),
    Electric	(4, FightProperty.FIGHT_PROP_CUR_ELEC_ENERGY, FightProperty.FIGHT_PROP_MAX_ELEC_ENERGY, 10401, "TeamResonance_Electric_Lv2", 7),
    Ice			(5, FightProperty.FIGHT_PROP_CUR_ICE_ENERGY, FightProperty.FIGHT_PROP_MAX_ICE_ENERGY, 10601, "TeamResonance_Ice_Lv2", 5),
    Frozen		(6, FightProperty.FIGHT_PROP_CUR_ICE_ENERGY, FightProperty.FIGHT_PROP_MAX_ICE_ENERGY),
    Wind		(7, FightProperty.FIGHT_PROP_CUR_WIND_ENERGY, FightProperty.FIGHT_PROP_MAX_WIND_ENERGY, 10301, "TeamResonance_Wind_Lv2", 4),
    Rock		(8, FightProperty.FIGHT_PROP_CUR_ROCK_ENERGY, FightProperty.FIGHT_PROP_MAX_ROCK_ENERGY, 10701, "TeamResonance_Rock_Lv2", 6),
    AntiFire	(9, FightProperty.FIGHT_PROP_CUR_FIRE_ENERGY, FightProperty.FIGHT_PROP_MAX_FIRE_ENERGY),
    Default		(255, FightProperty.FIGHT_PROP_CUR_FIRE_ENERGY, FightProperty.FIGHT_PROP_MAX_FIRE_ENERGY, 10801, "TeamResonance_AllDifferent");

    private static final Int2ObjectMap<ElementType> map = new Int2ObjectOpenHashMap<>();
    private static final Map<String, ElementType> stringMap = new HashMap<>();

    @Getter private final int value;
    @Getter private final int teamResonanceId;
    @Getter private final FightProperty curEnergyProp;
    @Getter private final FightProperty maxEnergyProp;
    @Getter private final int depotValue;
    @Getter private final int configHash;

    static {
        // Cache the values.
        Stream.of(ElementType.values()).forEach(element -> {
            map.put(element.getValue(), element);
            stringMap.put(element.name(), element);
        });
    }

    /**
     * Constructor for elements without team resonance.
     *
     * @param value The value of the element.
     * @param curEnergyProp The current energy property.
     * @param maxEnergyProp The maximum energy property.
     */
    ElementType(int value, FightProperty curEnergyProp, FightProperty maxEnergyProp) {
        this(value, curEnergyProp, maxEnergyProp, 0, null, 1);
    }

    /**
     * Constructor for elements with team resonance.
     * These elements also have a config.
     *
     * @param value The value of the element.
     * @param curEnergyProp The current energy property.
     * @param maxEnergyProp The maximum energy property.
     * @param teamResonanceId The team resonance id.
     * @param configName The name of the config.
     */
    ElementType(int value, FightProperty curEnergyProp, FightProperty maxEnergyProp, int teamResonanceId, String configName) {
        this(value, curEnergyProp, maxEnergyProp, teamResonanceId, configName, 1);
    }

    /**
     * Constructor for elements with team resonance.
     * These elements also have a config.
     * These elements also have an associated depot.
     *
     * @param value The value of the element.
     * @param curEnergyProp The current energy property.
     * @param maxEnergyProp The maximum energy property.
     * @param teamResonanceId The team resonance id.
     * @param configName The name of the config.
     * @param depotValue The value of the depot.
     */
    ElementType(int value, FightProperty curEnergyProp, FightProperty maxEnergyProp, int teamResonanceId, String configName, int depotValue) {
        this.value = value;
        this.curEnergyProp = curEnergyProp;
        this.maxEnergyProp = maxEnergyProp;
        this.teamResonanceId = teamResonanceId;
        this.depotValue = depotValue;

        if (configName != null) {
            this.configHash = ServerUtils.hashAbility(configName);
        } else {
            this.configHash = 0;
        }
    }

    /**
     * Fetches an element by its value.
     *
     * @param value The value of the element.
     * @return The element.
     */
    public static ElementType getType(int value) {
        return map.getOrDefault(value, None);
    }

    /**
     * Fetches an element by its name.
     *
     * @param name The name of the element.
     * @return The element.
     */
    public static ElementType getType(String name) {
        return stringMap.getOrDefault(name, None);
    }
}
