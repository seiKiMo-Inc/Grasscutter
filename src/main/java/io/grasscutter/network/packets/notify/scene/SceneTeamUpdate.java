package io.grasscutter.network.packets.notify.scene;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.AbilitySyncStateInfoOuterClass.AbilitySyncStateInfo;
import io.grasscutter.proto.SceneTeamAvatarOuterClass.SceneTeamAvatar;
import io.grasscutter.proto.SceneTeamUpdateNotifyOuterClass.SceneTeamUpdateNotify;
import lombok.AllArgsConstructor;

/** Scene avatars update notification. {@link PacketIds#SceneTeamUpdateNotify}. */
@AllArgsConstructor
public final class SceneTeamUpdate extends BasePacket<Empty, SceneTeamUpdateNotify> {
    private final Player player;

    @Override
    public SceneTeamUpdateNotify preparePacket() {
        var world = this.player.getWorld();

        var packet = SceneTeamUpdateNotify.newBuilder()
                .setIsInMp(world.isMultiplayer());

        for (var player : world) {
            var teams = player.getTeams();
            for (var entity : teams.getActiveAvatars()) {
                var avatar = entity.getAvatar();
                var weapon = avatar.getWeapon();
                var active = teams.getCurrentAvatar() == entity;

                // Create the avatar info.
                var avatarInfo = SceneTeamAvatar.newBuilder()
                        .setPlayerUid(player.getUserId())
                        .setAvatarGuid(avatar.getGuid())
                        .setSceneId(player.getSceneId())
                        .setEntityId(entity.getId())
                        .setSceneEntityInfo(entity.toProto())
                        .setWeaponGuid(weapon.getItemGuid())
                        .setWeaponEntityId(weapon.getEntityId())
                        .setIsPlayerCurAvatar(active)
                        .setIsOnScene(active)
                        .setAvatarAbilityInfo(AbilitySyncStateInfo.newBuilder())
                        .setWeaponAbilityInfo(AbilitySyncStateInfo.newBuilder())
                        .setAbilityControlBlock(entity.getAbilities());

                // Add additional data for multiplayer.
                if (world.isMultiplayer()) {
                    avatarInfo.setAvatarInfo(avatar.toProto());
                    avatarInfo.setSceneAvatarInfo(entity.toSceneAvatarInfo());
                }

                // Add the avatar info to the packet.
                packet.addSceneTeamAvatarList(avatarInfo);
            }
        }

        return packet.build();
    }
}
