package io.grasscutter.utils.constants;

import io.grasscutter.utils.definitions.game.CompoundProperty;
import io.grasscutter.utils.enums.game.FightProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.grasscutter.utils.enums.game.FightProperty.*;

public interface CombatProperties {
    /* Collection of base-statistics. */
    List<FightProperty> FLAT = Arrays.asList(
            FIGHT_PROP_BASE_HP, FIGHT_PROP_HP, FIGHT_PROP_BASE_ATTACK, FIGHT_PROP_ATTACK, FIGHT_PROP_BASE_DEFENSE,
            FIGHT_PROP_DEFENSE, FIGHT_PROP_HEALED_ADD, FIGHT_PROP_CUR_FIRE_ENERGY, FIGHT_PROP_CUR_ELEC_ENERGY,
            FIGHT_PROP_CUR_WATER_ENERGY, FIGHT_PROP_CUR_GRASS_ENERGY, FIGHT_PROP_CUR_WIND_ENERGY, FIGHT_PROP_CUR_ICE_ENERGY,
            FIGHT_PROP_CUR_ROCK_ENERGY, FIGHT_PROP_CUR_HP, FIGHT_PROP_MAX_HP, FIGHT_PROP_CUR_ATTACK, FIGHT_PROP_CUR_DEFENSE);

    /* Collection of compound properties. */
    Map<FightProperty, CompoundProperty> COMPOUND = Map.ofEntries(
            Map.entry(FIGHT_PROP_MAX_HP, new CompoundProperty(FIGHT_PROP_MAX_HP, FIGHT_PROP_BASE_HP, FIGHT_PROP_HP_PERCENT, FIGHT_PROP_HP)),
            Map.entry(FIGHT_PROP_CUR_ATTACK, new CompoundProperty(FIGHT_PROP_CUR_ATTACK, FIGHT_PROP_BASE_ATTACK, FIGHT_PROP_ATTACK_PERCENT, FIGHT_PROP_ATTACK)),
            Map.entry(FIGHT_PROP_CUR_DEFENSE, new CompoundProperty(FIGHT_PROP_CUR_DEFENSE, FIGHT_PROP_BASE_DEFENSE, FIGHT_PROP_DEFENSE_PERCENT, FIGHT_PROP_DEFENSE))
    );
}
