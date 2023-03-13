package io.grasscutter.network.packets.notify.scene;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.PlayerGameTimeNotifyOuterClass.PlayerGameTimeNotify;
import lombok.AllArgsConstructor;

/** Scene peer ID notification. {@link PacketIds#PlayerGameTimeNotify}. */
@AllArgsConstructor
public final class PlayerGameTime extends BasePacket<Empty, PlayerGameTimeNotify> {
    private final Player player;

    @Override
    public PlayerGameTimeNotify preparePacket() {
        return PlayerGameTimeNotify.newBuilder()
                .setUid(this.player.getUserId())
                .setGameTime(this.player.getScene().getTime())
                .build();
    }
}
