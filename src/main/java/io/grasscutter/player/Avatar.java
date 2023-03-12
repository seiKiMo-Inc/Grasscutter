package io.grasscutter.player;

import io.grasscutter.data.DataSerializable;
import io.grasscutter.data.FieldType;
import io.grasscutter.data.Special;
import io.grasscutter.game.data.GameData;
import io.grasscutter.game.data.excel.avatar.AvatarData;
import io.grasscutter.game.data.excel.avatar.AvatarSkillDepotData;
import io.grasscutter.proto.AvatarInfoOuterClass.AvatarInfo;
import io.grasscutter.utils.ServerUtils;
import io.grasscutter.utils.enums.game.PlayerProperty;
import io.grasscutter.utils.interfaces.DatabaseObject;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/* An instance of an avatar. */
@DataSerializable(table = "avatars")
public final class Avatar implements DatabaseObject {
    @Special(FieldType.ID)
    @Getter private UUID id = UUID.randomUUID(); // The avatar object's unique identifier.
    @Getter private int avatarId = Integer.MAX_VALUE; // The avatar's ID.
    @Getter private int ownerUserId = Integer.MAX_VALUE; // The avatar's owner's game user ID (UID).

    @Getter private int skillDepotId = Integer.MAX_VALUE; // The avatar's skill depot ID.
    @Getter private Set<Integer> talentListId = new HashSet<>(); // The avatar's talent list ID.

    @Getter private transient Player owner = null; // The avatar's owner.
    @Getter private transient AvatarData data = null; // The avatar's data.
    @Getter private transient AvatarSkillDepotData skill = null; // The avatar's skill data/depot.
    @Getter private transient long guid = Long.MAX_VALUE; // The avatar's player unique ID.

    @Getter @Setter private int level = 1; // The avatar's level.
    @Getter @Setter private int experience = 0; // The avatar's experience.
    @Getter @Setter private int ascension = 0; // The avatar's ascension level.

    @Getter @Setter private int satiation = 0; // The avatar's satiation (fullness).
    @Getter @Setter private int satiationPenalty = 0; // The avatar's satiation penalty.
    @Getter @Setter private int currentHealth = 0; // The avatar's current health.
    @Getter @Setter private int currentEnergy = 0; // The avatar's current energy.

    @Getter private int creationTime = 0; // The avatar's creation time.

    @Getter private final transient Int2FloatOpenHashMap fightProperties
            = new Int2FloatOpenHashMap(); // The avatar's fight properties.
    @Getter private final transient Int2FloatOpenHashMap fightOverrides
            = new Int2FloatOpenHashMap(); // The avatar's fight properties.

    @ApiStatus.Internal
    public Avatar() {
        // Empty constructor for initialization.
    }

    /**
     * Constructor for newly created avatars.
     * Uses an avatar ID to fetch the avatar data.
     *
     * @param avatarId The avatar's ID.
     */
    public Avatar(int avatarId) {
        this(GameData.getAvatarDataMap().get(avatarId));
    }

    /**
     * Constructor for newly created avatars.
     *
     * @param avatarData The avatar's data.
     */
    public Avatar(AvatarData avatarData) {
        this.data = avatarData;
        this.avatarId = avatarData.getId();
        this.creationTime = (int) (System.currentTimeMillis() / 1000L);
    }

    /**
     * Sets the avatar's owner.
     *
     * @param owner The avatar's owner.
     */
    public void setOwner(Player owner) {
        this.owner = owner;
        this.ownerUserId = owner.getUserId();
        this.guid = owner.getNextGameId();
    }

    /**
     * Converts this object into an {@link AvatarInfo} object.
     *
     * @return The converted object.
     */
    public AvatarInfo toProto() {
        var avatarInfo = AvatarInfo.newBuilder()
                .setAvatarId(this.getAvatarId())
                .setGuid(this.getGuid())
                .setLifeState(1)
                .putAllFightPropMap(this.getFightProperties())
                .setSkillDepotId(this.getSkillDepotId())
                .setAvatarType(1)
                .setBornTime(this.getCreationTime());

        // Add the other avatar properties.
        ServerUtils.property(avatarInfo, PlayerProperty.PROP_LEVEL, this.getLevel());
        ServerUtils.property(avatarInfo, PlayerProperty.PROP_EXP, this.getExperience());
        ServerUtils.property(avatarInfo, PlayerProperty.PROP_BREAK_LEVEL, this.getAscension());
        ServerUtils.property(avatarInfo, PlayerProperty.PROP_SATIATION_VAL, this.getSatiation());
        ServerUtils.property(avatarInfo, PlayerProperty.PROP_SATIATION_PENALTY_TIME, this.getSatiationPenalty());

        return avatarInfo.build();
    }
}
