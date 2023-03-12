package io.grasscutter.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

/* Base player manager. */
@AllArgsConstructor
public abstract class PlayerManager {
    @Getter protected final transient Player player;

    /**
     * Saves the player to the database.
     */
    public void save() {
        this.player.save();
    }
}
