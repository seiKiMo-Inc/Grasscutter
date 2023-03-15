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
public enum SceneType {
    SCENE_NONE(0),
    SCENE_WORLD(1),
    SCENE_DUNGEON(2),
    SCENE_ROOM(3),
    SCENE_HOME_WORLD(4),
    SCENE_HOME_ROOM(5),
    SCENE_ACTIVITY(6);

    private static final Int2ObjectMap<SceneType> map = new Int2ObjectOpenHashMap<>();
    private static final Map<String, SceneType> stringMap = new HashMap<>();

    private final int value;

    static {
        // Cache the values.
        Stream.of(SceneType.values())
                .forEach(
                        element -> {
                            map.put(element.getValue(), element);
                            stringMap.put(element.name(), element);
                        });
    }

    /**
     * Fetches the SceneType by its value.
     *
     * @param value The value of the SceneType.
     * @return The SceneType.
     */
    public static SceneType fetch(int value) {
        return map.getOrDefault(value, SCENE_NONE);
    }

    /**
     * Fetches the SceneType by its name.
     *
     * @param name The name of the SceneType.
     * @return The SceneType.
     */
    public static SceneType fetch(String name) {
        return stringMap.getOrDefault(name, SCENE_NONE);
    }
}
