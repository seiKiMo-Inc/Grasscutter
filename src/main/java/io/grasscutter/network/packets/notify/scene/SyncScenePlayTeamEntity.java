package io.grasscutter.network.packets.notify.scene;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.SyncScenePlayTeamEntityNotifyOuterClass.SyncScenePlayTeamEntityNotify;
import lombok.AllArgsConstructor;

/** Sync team data for the player. {@link PacketIds#SyncScenePlayTeamEntityNotify}. */
@AllArgsConstructor
public final class SyncScenePlayTeamEntity
        extends BasePacket<Empty, SyncScenePlayTeamEntityNotify> {
    private final Player player;

    @Override
    public SyncScenePlayTeamEntityNotify preparePacket() {
        return SyncScenePlayTeamEntityNotify.newBuilder().setSceneId(this.player.getSceneId()).build();
    }
}
