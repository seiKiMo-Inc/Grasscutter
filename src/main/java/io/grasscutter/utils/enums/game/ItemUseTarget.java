package io.grasscutter.utils.enums.game;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ItemUseTarget {
    ITEM_USE_TARGET_NONE(0),
    ITEM_USE_TARGET_CUR_AVATAR(1),
    ITEM_USE_TARGET_CUR_TEAM(2),
    ITEM_USE_TARGET_SPECIFY_AVATAR(3),
    ITEM_USE_TARGET_SPECIFY_ALIVE_AVATAR(4),
    ITEM_USE_TARGET_SPECIFY_DEAD_AVATAR(5);

    private static final Int2ObjectMap<ItemUseTarget> map = new Int2ObjectOpenHashMap<>();

    private final int value;

    static {
        // Cache the values.
        Stream.of(ItemUseTarget.values()).forEach(entry -> map.put(entry.getValue(), entry));
    }

    /**
     * Fetches an item type by its value.
     *
     * @param value The value of the item type.
     * @return The item type.
     */
    public static ItemUseTarget fetch(int value) {
        return map.getOrDefault(value, ITEM_USE_TARGET_NONE);
    }
}
