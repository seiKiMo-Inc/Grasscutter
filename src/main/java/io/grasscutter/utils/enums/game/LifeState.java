package io.grasscutter.utils.enums.game;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter public enum LifeState {
	LIFE_NONE   (0),
	LIFE_ALIVE  (1),
	LIFE_DEAD   (2),
	LIFE_REVIVE (3);

	private static final Int2ObjectMap<LifeState> map = new Int2ObjectOpenHashMap<>();
	private static final Map<String, LifeState> stringMap = new HashMap<>();

	private final int value;

	static {
		// Cache the values.
		Stream.of(LifeState.values()).forEach(element -> {
			map.put(element.getValue(), element);
			stringMap.put(element.name(), element);
		});
	}

	/**
	 * Fetches the LifeState by its value.
	 *
	 * @param value The value of the LifeState.
	 * @return The LifeState.
	 */
	public static LifeState fetch(int value) {
		return map.getOrDefault(value, LIFE_NONE);
	}

	/**
	 * Fetches the LifeState by its name.
	 *
	 * @param name The name of the LifeState.
	 * @return The LifeState.
	 */
	public static LifeState fetch(String name) {
		return stringMap.getOrDefault(name, LIFE_NONE);
	}
}
