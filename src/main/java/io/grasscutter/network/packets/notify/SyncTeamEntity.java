package io.grasscutter.network.packets.notify;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.AbilitySyncStateInfoOuterClass.AbilitySyncStateInfo;
import io.grasscutter.proto.SyncTeamEntityNotifyOuterClass.SyncTeamEntityNotify;
import io.grasscutter.proto.TeamEntityInfoOuterClass.TeamEntityInfo;
import lombok.AllArgsConstructor;

/** Sync team data for the player. {@link PacketIds#SyncTeamEntityNotify}. */
@AllArgsConstructor
public final class SyncTeamEntity extends BasePacket<Empty, SyncTeamEntityNotify> {
    private final Player player;

    @Override
    public SyncTeamEntityNotify preparePacket() {
        var packet = SyncTeamEntityNotify.newBuilder().setSceneId(this.player.getSceneId());

        // Add multiplayer data.
        var world = this.player.getWorld();
        if (world.isMultiplayer())
            for (var player : world) {
                // Skip if same player.
                if (player == this.player) continue;

                // Set team info.
                packet.addTeamEntityInfoList(
                        TeamEntityInfo.newBuilder()
                                .setTeamEntityId(player.getTeams().getEntityId())
                                .setAuthorityPeerId(player.getPeerId())
                                .setTeamAbilityInfo(AbilitySyncStateInfo.newBuilder()));
            }

        return packet.build();
    }
}
