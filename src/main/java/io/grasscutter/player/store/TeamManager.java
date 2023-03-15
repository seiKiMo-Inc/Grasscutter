package io.grasscutter.player.store;

import io.grasscutter.player.Player;
import io.grasscutter.player.PlayerManager;
import io.grasscutter.utils.constants.GameConstants;
import io.grasscutter.utils.interfaces.Serializable;
import io.grasscutter.utils.objects.game.TeamInfo;
import io.grasscutter.world.World;
import io.grasscutter.world.entity.EntityAvatar;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.*;
import java.util.function.BiConsumer;
import lombok.Getter;
import lombok.Setter;

/* Manages the player's team(s). */
public final class TeamManager extends PlayerManager implements Serializable {
    @Getter
    private final Map<Integer, TeamInfo> teams = new LinkedHashMap<>(); // A collection of teams.

    @Getter @Setter
    private transient TeamInfo multiplayerTeam = new TeamInfo(); // Default multiplayer team.

    @Getter private transient List<TeamInfo> temporaryTeam;

    @Getter private final transient IntSet resonances = new IntOpenHashSet(); // Team resonances.

    @Getter
    private final transient IntSet resonancesConfig =
            new IntOpenHashSet(); // Configs for team resonances.

    @Getter
    private final transient List<EntityAvatar> activeAvatars =
            new ArrayList<>(); // The player's avatars.

    @Getter @Setter private int selectedTeam = 0;
    @Getter @Setter private int selectedAvatar = 0;
    @Getter @Setter private int temporaryTeamId = -1;

    @Getter @Setter private transient int entityId = 0;

    /** Constructor for a blank team manager. */
    public TeamManager() {
        super(null);
    }

    /**
     * Constructor for a team manager.
     *
     * @param player The player to manage.
     */
    public TeamManager(Player player) {
        super(player);

        this.selectedTeam = 1;
        // Add teams to the collection.
        for (var i = 1; i <= GameConstants.TEAM_COUNT; i++) this.teams.put(i, new TeamInfo());
    }

    /**
     * For each team, run the consumer.
     *
     * @param forEach The consumer to run.
     */
    public void forEach(BiConsumer<Integer, TeamInfo> forEach) {
        this.teams.forEach(forEach);
    }

    /**
     * Gets the world of the player.
     *
     * @return The world of the player.
     */
    public World getWorld() {
        return this.getPlayer().getWorld();
    }

    /**
     * Fetches the index of the team.
     *
     * @param team The team to fetch the index of.
     * @return The index of the team.
     */
    public int getTeamId(TeamInfo team) {
        return this.teams.entrySet().stream()
                .filter(entry -> entry.getValue() == team)
                .findFirst()
                .orElse(Map.entry(-1, new TeamInfo()))
                .getKey();
    }

    /**
     * Fetches the GUID of the selected avatar.
     *
     * @return The GUID of the selected avatar.
     */
    public long getSelectedAvatarGuid() {
        return 0;
    }

    /**
     * Fetches the currently selected team.
     *
     * @return The currently selected team.
     */
    public TeamInfo getCurrentTeam() {
        return this.getTeams().get(this.getSelectedTeam());
    }

    /**
     * Fetches the currently selected avatar. This returns the avatar as an entity.
     *
     * @return The currently selected avatar.
     */
    public EntityAvatar getCurrentAvatar() {
        return this.getActiveAvatars().get(this.getSelectedAvatar());
    }

    /*
     * Serialization.
     */

    //    @Override
    //    public Map<String, Object> serialize() {
    //        // Serialize the map.
    //        var map = new HashMap<String, Object>();
    //        this.teams.forEach((key, value) ->
    //                map.put(String.valueOf(key), value.serialize()));
    //
    //        return map;
    //    }
    //
    //    @Override
    //    @SuppressWarnings("unchecked")
    //    public void deserialize(Map<String, Object> data) {
    //        // De-serialize the map.
    //        data.forEach((key, value) -> {
    //            var team = new TeamInfo();
    //            team.deserialize((Map<String, Object>) value);
    //            this.teams.put(Integer.parseInt(key), team);
    //        });
    //    }
}
