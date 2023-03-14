package io.grasscutter.player.store;

import io.grasscutter.utils.enums.game.PlayerProperty;
import io.grasscutter.utils.interfaces.Serializable;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

import java.util.HashMap;
import java.util.Map;

/* Player properties. */
public final class PlayerProperties extends Int2IntOpenHashMap implements Serializable {
    @Override
    public Map<String, Object> serialize() {
        // Serialize the map.
        var map = new HashMap<String, Object>();
        this.forEach((key, value) ->
                map.put(String.valueOf(key), value));

        return map;
    }

    @Override
    public void deserialize(Map<String, Object> data) {
        // De-serialize the map.
        data.forEach((key, value) ->
                this.put(Integer.parseInt(key), (int) value));
    }

    /**
     * Fetches a player's property.
     *
     * @param property The property to fetch.
     * @return The property's value.
     */
    public int getProperty(PlayerProperty property) {
        return this.get(property.getId());
    }

    /**
     * @return The player's world level property.
     */
    public int getWorldLevel() {
        return this.getProperty(PlayerProperty.PROP_PLAYER_WORLD_LEVEL);
    }

    /**
     * @return The player's level property.
     */
    public int getLevel() {
        return this.getProperty(PlayerProperty.PROP_PLAYER_LEVEL);
    }
}
