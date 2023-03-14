package io.grasscutter.utils.enums.game;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter public enum ItemType {
	ITEM_NONE		(0),
	ITEM_VIRTUAL	(1),
	ITEM_MATERIAL	(2),
	ITEM_RELIQUARY	(3),
	ITEM_WEAPON		(4),
	ITEM_DISPLAY	(5),
	ITEM_FURNITURE	(6);

	private static final Int2ObjectMap<ItemType> map = new Int2ObjectOpenHashMap<>();
	private static final Map<String, ItemType> stringMap = new HashMap<>();

	private final int value;

	static {
		// Cache the values.
		Stream.of(ItemType.values()).forEach(element -> {
			map.put(element.getValue(), element);
			stringMap.put(element.name(), element);
		});
	}

	/**
	 * Fetches an item type by its value.
	 *
	 * @param value The value of the item type.
	 * @return The item type.
	 */
	public static ItemType fetch(int value) {
		return map.getOrDefault(value, ITEM_NONE);
	}

	/**
	 * Fetches an item type by its name.
	 *
	 * @param name The name of the item type.
	 * @return The item type.
	 */
	public static ItemType fetch(String name) {
		return stringMap.getOrDefault(name, ITEM_NONE);
	}
}
