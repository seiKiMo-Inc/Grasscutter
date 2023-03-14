package io.grasscutter.utils.enums.game;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter public enum EquipType {
	EQUIP_NONE	   (0),
	EQUIP_BRACER   (1),
	EQUIP_NECKLACE (2),
	EQUIP_SHOES	   (3),
	EQUIP_RING	   (4),
	EQUIP_DRESS	   (5),
	EQUIP_WEAPON   (6);

	private static final Int2ObjectMap<EquipType> map = new Int2ObjectOpenHashMap<>();
	private static final Map<String, EquipType> stringMap = new HashMap<>();

	private final int value;

	static {
		// Cache the values.
		Stream.of(EquipType.values()).forEach(entry -> {
			map.put(entry.getValue(), entry);
			stringMap.put(entry.name(), entry);
		});
	}

	/**
	 * Fetches an item type by its value.
	 *
	 * @param value The value of the item type.
	 * @return The item type.
	 */
	public static EquipType fetch(int value) {
		return map.getOrDefault(value, EQUIP_NONE);
	}

	/**
	 * Fetches an item type by its name.
	 *
	 * @param name The name of the item type.
	 * @return The item type.
	 */
	public static EquipType fetch(String name) {
		return stringMap.getOrDefault(name, EQUIP_NONE);
	}
}
