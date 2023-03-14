package io.grasscutter.player;

import io.grasscutter.data.DataSerializable;
import io.grasscutter.data.FieldType;
import io.grasscutter.data.Special;
import io.grasscutter.game.inventory.PlayerInventory;
import io.grasscutter.network.NetworkSession;
import io.grasscutter.network.packets.notify.data.AvatarData;
import io.grasscutter.network.packets.notify.data.PlayerData;
import io.grasscutter.network.packets.notify.data.PlayerStore;
import io.grasscutter.network.packets.notify.data.StoreWeightLimit;
import io.grasscutter.network.packets.notify.scene.PlayerEnterScene;
import io.grasscutter.player.store.*;
import io.grasscutter.proto.MpSettingTypeOuterClass.MpSettingType;
import io.grasscutter.proto.OnlinePlayerInfoOuterClass.OnlinePlayerInfo;
import io.grasscutter.utils.ServerUtils;
import io.grasscutter.utils.constants.GameConstants;
import io.grasscutter.utils.enums.game.ClimateType;
import io.grasscutter.utils.enums.game.PlayerState;
import io.grasscutter.utils.enums.game.SceneLoadState;
import io.grasscutter.utils.interfaces.DatabaseObject;
import io.grasscutter.world.Position;
import io.grasscutter.world.Scene;
import io.grasscutter.world.World;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

/* An instance of a player. */
@DataSerializable(table = "players")
public class Player implements DatabaseObject {
    @Special(FieldType.ID)
    @Getter private int userId = Integer.MAX_VALUE; // The player's ID (shown as UID).
    @Getter private long accountId = Long.MAX_VALUE; // The player's account's ID.
    private transient long nextGameId = 0; // The next game ID to use.

    @Getter @Setter private transient NetworkSession session; // The associated session.
    @Getter @Setter private transient Account account; // The associated account.

    @Getter(onMethod_ = {@Synchronized})
    @Setter(onMethod_ = {@Synchronized})
    private transient World world; // The player's world.
    @Getter(onMethod_ = {@Synchronized})
    @Setter(onMethod_ = {@Synchronized})
    private transient Scene scene; // The player's scene.
    @Getter private transient boolean paused = false; // Whether the player is paused.
    @Getter @Setter private transient int peerId = 0; // The player's peer ID.
    @Getter @Setter private transient int weatherId = 0; // The player's weather ID.
    @Getter @Setter private transient int sceneToken = 0; // The player's enter scene token.
    @Getter @Setter private transient PlayerState state; // The player's state.
    @Getter @Setter private transient SceneLoadState sceneState; // The player's scene state.
    @Getter @Setter private transient ClimateType climate = ClimateType.CLIMATE_SUNNY; // The player's climate.

    @Getter @Setter private int nameCard = 210001; // The ID of the player's name card.
    @Getter @Setter private int profileIcon = 0; // The ID of the player's profile icon.
    @Getter @Setter private int mainCharacter = 0; // The avatar ID of the player's main character.
    @Getter @Setter private String nickName = "", signature = ""; // The nickname and signature of the player.
    @Getter @Setter private int sceneId = 3, regionId = 1; // The IDs of the scene and region the player is in.
    @Getter @Setter private MpSettingType coopEnterSetting = MpSettingType.MP_SETTING_TYPE_ENTER_AFTER_APPLY; // The Co-Op enter setting of the player.
    @Getter private Position position = GameConstants.START; // The player's current position.
    @Getter private Position rotation = GameConstants.DEFAULT_ROTATION; // The player's current rotaton.

    @Getter private PlayerProperties properties = new PlayerProperties(); // The player's properties.
    @Getter @Setter private Position lastPos = GameConstants.START; // The player's previous position.

    @Getter private boolean loggedIn = false; // Whether the player is logged in.

    /**
     * Player managers.
     * Should be instances of {@link PlayerManager}.
     */

    @Getter private final TeamManager teams
            = new TeamManager(this); // The player's team.
    @Getter private final transient AvatarStorage avatars
            = new AvatarStorage(this); // The player's avatars.
    @Getter private final transient PlayerInventory inventory
            = new PlayerInventory(this); // The player's inventory.

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
    public void doLogin() {
        // Create a world for the player.
        this.world = new World(this);
        this.world.addPlayer(this);

        // Send the player data packets.
        this.session.send(new PlayerData(this));
        this.session.send(new StoreWeightLimit());
        this.session.send(new PlayerStore(this));
        this.session.send(new AvatarData(this));

        // Send the scene load packet.
        this.session.send(new PlayerEnterScene(this));

        // Set the player state.
        this.setState(PlayerState.ACTIVE);
        // Set the log in flag.
        this.loggedIn = true;
    }

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
        this.getTeams().load();
        this.getAvatars().load();
        this.getInventory().load();
    }

    /*
     * Protocol definition conversion.
     */

    /**
     * Converts this player to an online player info.
     *
     * @return The online player info.
     */
    public OnlinePlayerInfo toOnlinePlayerInfo() {
        var info = OnlinePlayerInfo.newBuilder()
                .setUid(this.getUserId())
                .setNickname(this.getNickName())
                .setPlayerLevel(this.getProperties().getLevel())
                .setMpSettingType(this.getCoopEnterSetting())
                .setNameCardId(this.getNameCard())
                .setSignature(this.getSignature())
                .setProfilePicture(ServerUtils.profilePicture(this.getProfileIcon()));

        // Set the world data.
        if (this.getWorld() == null) {
            info.setCurPlayerNumInWorld(1);
        } else {
            info.setCurPlayerNumInWorld(this.getWorld().count());
        }

        return info.build();
    }
}
