package io.grasscutter.utils.enums.game;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public enum WeaponType {
	WEAPON_NONE (0),
	WEAPON_SWORD_ONE_HAND (1, 10, 5),
	WEAPON_CROSSBOW (2),
	WEAPON_STAFF (3),
	WEAPON_DOUBLE_DAGGER (4),
	WEAPON_KATANA (5),
	WEAPON_SHURIKEN (6),
	WEAPON_STICK (7),
	WEAPON_SPEAR (8),
	WEAPON_SHIELD_SMALL (9),
	WEAPON_CATALYST (10, 0, 10),
	WEAPON_CLAYMORE (11, 0, 10),
	WEAPON_BOW (12, 0, 5),
	WEAPON_POLE (13, 0, 4);

	private static final Int2ObjectMap<WeaponType> map = new Int2ObjectOpenHashMap<>();
	private static final Map<String, WeaponType> stringMap = new HashMap<>();

	@Getter private final int value;
	@Getter private int energyGainInitialProbability;
	@Getter private int energyGainIncreaseProbability;

	static {
		// Cache the values.
		Stream.of(WeaponType.values()).forEach(e -> {
			map.put(e.getValue(), e);
			stringMap.put(e.name(), e);
		});
	}

	WeaponType(int value) {
		this.value = value;
	}

	WeaponType(int value, int gainProbability, int increaseProbability) {
		this.value = value;
		this.energyGainInitialProbability = gainProbability;
		this.energyGainIncreaseProbability = increaseProbability;
	}

	/**
	 * Fetches the WeaponType by its value.
	 *
	 * @param value The value of the WeaponType.
	 * @return The WeaponType.
	 */
	public static WeaponType getType(int value) {
		return map.getOrDefault(value, WEAPON_NONE);
	}

	/**
	 * Fetches the WeaponType by its name.
	 *
	 * @param name The name of the WeaponType.
	 * @return The WeaponType.
	 */
	public static WeaponType getType(String name) {
		return stringMap.getOrDefault(name, WEAPON_NONE);
	}
}
