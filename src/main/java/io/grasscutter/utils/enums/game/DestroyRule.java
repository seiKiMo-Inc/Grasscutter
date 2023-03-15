package io.grasscutter.utils.enums.game;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DestroyRule {
    DESTROY_NONE(0),
    DESTROY_RETURN_MATERIAL(1);

    private static final Int2ObjectMap<DestroyRule> map = new Int2ObjectOpenHashMap<>();
    private static final Map<String, DestroyRule> stringMap = new HashMap<>();

    private final int value;

    static {
        // Cache the values.
        Stream.of(DestroyRule.values())
                .forEach(
                        entry -> {
                            map.put(entry.getValue(), entry);
                            stringMap.put(entry.name(), entry);
                        });
    }

    /**
     * Fetches a destroy rule by its value.
     *
     * @param value The value of the destroy rule.
     * @return The destroy rule.
     */
    public static DestroyRule fetch(int value) {
        return map.getOrDefault(value, DESTROY_NONE);
    }

    /**
     * Fetches a destroy rule by its name.
     *
     * @param name The name of the destroy rule.
     * @return The destroy rule.
     */
    public static DestroyRule fetch(String name) {
        return stringMap.getOrDefault(name, DESTROY_NONE);
    }
}
