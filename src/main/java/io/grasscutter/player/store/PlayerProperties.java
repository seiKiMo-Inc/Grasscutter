package io.grasscutter.player.store;

import io.grasscutter.utils.enums.game.PlayerProperty;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/* Player properties. */
public final class PlayerProperties extends Int2IntOpenHashMap {
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
