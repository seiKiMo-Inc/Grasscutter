package io.grasscutter.player;

import io.grasscutter.data.DataSerializable;
import io.grasscutter.data.FieldType;
import io.grasscutter.data.Special;
import io.grasscutter.network.NetworkSession;
import io.grasscutter.utils.constants.GameConstants;
import io.grasscutter.utils.enums.PlayerState;
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

    @Getter @Setter private transient NetworkSession session; // The associated session.
    @Getter @Setter private transient Account account; // The associated account.

    @Getter @Setter private transient PlayerState state; // The player's state.

    @Getter private int nameCard = 210001;
    @Getter private String nickname = "", signature = null;
    @Getter private Position position = GameConstants.START;

    @Getter @Setter private Position lastPos = GameConstants.START;

    /**
     * Loads additional data from the database not saved here.
     * Call this after first initializing the object.
     */
    public void loadAllData() {

    }
}
