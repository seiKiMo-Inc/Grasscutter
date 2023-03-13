package io.grasscutter.utils.enums.game;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter public enum ClimateType {
	CLIMATE_NONE         (0),
	CLIMATE_SUNNY        (1),
	CLIMATE_CLOUDY       (2),
	CLIMATE_RAIN         (3),
	CLIMATE_THUNDERSTORM (4),
	CLIMATE_SNOW         (5),
	CLIMATE_MIST         (6);

	private static final Int2ObjectMap<ClimateType> map = new Int2ObjectOpenHashMap<>();
	private static final Map<String, ClimateType> stringMap = new HashMap<>();

	private final int value;

	static {
		// Cache the values.
		Stream.of(ClimateType.values()).forEach(element -> {
			map.put(element.getValue(), element);
			stringMap.put(element.name(), element);
		});
	}

	/**
	 * Returns the short name of the climate type.
	 *
	 * @return The short name.
	 */
	public String getShortName() {
		return this.name().substring(8).toLowerCase();
	}

	/**
	 * Fetch the climate type by its value.
	 *
	 * @param value The value.
	 * @return The climate type.
	 */
	public static ClimateType fetch(int value) {
		return map.getOrDefault(value, CLIMATE_NONE);
	}

	/**
	 * Fetch the climate type by its name.
	 *
	 * @param name The name.
	 * @return The climate type.
	 */
	public static ClimateType fetch(String name) {
		return stringMap.getOrDefault(name, CLIMATE_NONE);
	}

	/**
	 * Fetch the climate type by its short name.
	 *
	 * @param shortName The short name.
	 * @return The climate type.
	 */
	public static ClimateType fetchShort(String shortName) {
		var name = "CLIMATE_" + shortName.toUpperCase();
		return stringMap.getOrDefault(name, CLIMATE_NONE);
	}
}
