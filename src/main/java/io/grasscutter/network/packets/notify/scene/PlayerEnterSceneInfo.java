package io.grasscutter.network.packets.notify.scene;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.AbilityControlBlockOuterClass.AbilityControlBlock;
import io.grasscutter.proto.AbilitySyncStateInfoOuterClass.AbilitySyncStateInfo;
import io.grasscutter.proto.AvatarEnterSceneInfoOuterClass.AvatarEnterSceneInfo;
import io.grasscutter.proto.MPLevelEntityInfoOuterClass.MPLevelEntityInfo;
import io.grasscutter.proto.PlayerEnterSceneInfoNotifyOuterClass.PlayerEnterSceneInfoNotify;
import io.grasscutter.proto.TeamEnterSceneInfoOuterClass.TeamEnterSceneInfo;
import lombok.AllArgsConstructor;

/** The scene's entity data. {@link PacketIds#PlayerEnterSceneInfoNotify}. */
@AllArgsConstructor
public final class PlayerEnterSceneInfo extends BasePacket<Empty, PlayerEnterSceneInfoNotify> {
    private final Player player;

    @Override
    public PlayerEnterSceneInfoNotify preparePacket() {
        var teams = this.player.getTeams();
        var world = this.player.getWorld();

        var empty = AbilitySyncStateInfo.newBuilder().build();
        var packet = PlayerEnterSceneInfoNotify.newBuilder()
                .setCurAvatarEntityId(teams.getCurrentAvatar().getId())
                .setEnterSceneToken(player.getSceneToken());

        // Set the team info.
        packet.setTeamEnterInfo(TeamEnterSceneInfo.newBuilder()
                .setTeamEntityId(teams.getEntityId())
                .setTeamAbilityInfo(empty)
                .setAbilityControlBlock(AbilityControlBlock.newBuilder().build()));
        // Set the multiplayer info.
        packet.setMpLevelEntityInfo(MPLevelEntityInfo.newBuilder()
                .setEntityId(world.getLevelId())
                .setAuthorityPeerId(world.getAuthority())
                .setAbilityInfo(empty));

        // Add the player's avatars.
        for (var entity : player.getTeams().getActiveAvatars()) {
            var avatar = entity.getAvatar();
            var weapon = avatar.getWeapon();

            // Create the avatar info.
            packet.addAvatarEnterInfo(AvatarEnterSceneInfo.newBuilder()
                    .setAvatarGuid(entity.getAvatar().getGuid())
                    .setAvatarEntityId(entity.getId())
                    .setWeaponGuid(weapon.getItemGuid())
                    .setWeaponEntityId(weapon.getEntityId())
                    .setAvatarAbilityInfo(empty)
                    .setWeaponAbilityInfo(empty));
        }

        return packet.build();
    }
}
