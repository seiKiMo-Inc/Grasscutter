package io.grasscutter.utils.objects.game;

import io.grasscutter.player.Avatar;
import io.grasscutter.player.Player;
import io.grasscutter.proto.AvatarTeamOuterClass.AvatarTeam;
import io.grasscutter.utils.interfaces.Serializable;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/* A game team of avatars. */
public final class TeamInfo implements Serializable {
    @Getter @Setter private String name;
    @Getter private IntList avatars;

    /** Constructor for a blank team. */
    public TeamInfo() {
        this.name = "";
        this.avatars = new IntArrayList();
    }

    /**
     * Constructor for a team with avatars.
     *
     * @param avatars The avatars to add.
     */
    public TeamInfo(IntList avatars) {
        this.name = "";
        this.avatars = avatars;
    }

    /**
     * Gets the amount of avatars in the team.
     *
     * @return The amount of avatars.
     */
    public int size() {
        return this.avatars.size();
    }

    /**
     * Checks if the team contains the avatar.
     *
     * @param avatar The avatar to check.
     * @return Whether the team contains the avatar.
     */
    public boolean contains(Avatar avatar) {
        return this.avatars.contains(avatar.getAvatarId());
    }

    /**
     * Adds an avatar to the team.
     *
     * @param avatar The avatar to add.
     */
    public void add(Avatar avatar) {
        this.avatars.add(avatar.getAvatarId());
    }

    /**
     * Converts the team to a protocol buffer.
     *
     * @param player The player to convert the team for.
     * @return The protocol buffer.
     */
    public AvatarTeam toProto(Player player) {
        var team = AvatarTeam.newBuilder().setTeamName(this.getName());

        // Add the avatars to the team.
        for (var i = 0; i < this.size(); i++) {
            var avatar = player.getAvatars().get(this.avatars.getInt(i));
            team.addAvatarGuidList(avatar.getGuid());
        }

        return team.build();
    }

    @Override
    public Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put("name", this.name);
        map.put("avatars", List.of(this.avatars));

        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deserialize(Map<String, Object> data) {
        this.name = (String) data.get("name");
        this.avatars = new IntArrayList((List<Integer>) data.get("avatars"));
    }
}
