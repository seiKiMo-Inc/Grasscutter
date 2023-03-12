package io.grasscutter.player;

import io.grasscutter.data.DataSerializable;
import io.grasscutter.data.FieldType;
import io.grasscutter.data.Special;
import io.grasscutter.network.NetworkSession;
import io.grasscutter.player.store.*;
import io.grasscutter.utils.constants.GameConstants;
import io.grasscutter.utils.enums.game.PlayerState;
import io.grasscutter.utils.interfaces.DatabaseObject;
import io.grasscutter.world.Position;
import lombok.Getter;
import lombok.Setter;

/* An instance of a player. */
@DataSerializable(table = "players")
public class Player implements DatabaseObject {
    @Special(FieldType.ID)
    @Getter private int userId = Integer.MAX_VALUE; // The player's ID (shown as UID).
    @Getter private long accountId = Long.MAX_VALUE; // The player's account's ID.
    private transient long nextGameId = Long.MAX_VALUE; // The next game ID to use.

    @Getter @Setter private transient NetworkSession session; // The associated session.
    @Getter @Setter private transient Account account; // The associated account.

    @Getter @Setter private transient PlayerState state; // The player's state.

    @Getter @Setter private int nameCard = 210001;
    @Getter @Setter private int profileIcon = 0;
    @Getter @Setter private int mainCharacter = 0;
    @Getter @Setter private String nickName = "", signature = null;
    @Getter private Position position = GameConstants.START;

    @Getter @Setter private Position lastPos = GameConstants.START;

    /**
     * Player managers.
     * Should be instances of {@link PlayerManager}.
     */

    @Getter private final transient AvatarStorage avatars
            = new AvatarStorage(this); // The player's avatars.

    public Player() {
        // Empty constructor for initialization.
    }

    /**
     * Constructor for newly created players.
     *
     * @param userId The player's ID.
     * @param accountId The player's account's ID.
     */
    public Player(int userId, long accountId) {
        this.userId = userId;
        this.accountId = accountId;
    }

    /*
     * Player events.
     */

    /**
     * Invoked when the player finishes logging in.
     */
    public void doLogin() {}

    /*
     * Data methods.
     */

    /**
     * Returns the next game ID to use.
     *
     * @return A game ID. (GUID)
     */
    public long getNextGameId() {
        var nextId = ++this.nextGameId;
        return ((long) this.userId << 32) + nextId;
    }

    /**
     * Loads additional data from the database not saved here.
     * Call this after first initializing the object.
     */
    public void loadAllData() {

    }
}
