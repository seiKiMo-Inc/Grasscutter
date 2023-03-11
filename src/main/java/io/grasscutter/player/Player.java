package io.grasscutter.player;

import io.grasscutter.data.DataSerializable;
import io.grasscutter.data.FieldType;
import io.grasscutter.data.Special;
import io.grasscutter.network.NetworkSession;
import io.grasscutter.utils.interfaces.DatabaseObject;
import io.grasscutter.world.Position;
import lombok.Getter;
import lombok.Setter;

/* An instance of a player. */
@DataSerializable(table = "players")
public class Player implements DatabaseObject {
    @Special(FieldType.ID)
    public long id = Long.MAX_VALUE; // The player's ID (shown as UID).
    public long accountId = Long.MAX_VALUE; // The player's account's ID.

    @Getter @Setter private transient NetworkSession session; // The associated session.
    @Getter @Setter private transient Account account; // The associated account.

    @Getter private String nickname, signature;
    @Getter private Position position;
}
