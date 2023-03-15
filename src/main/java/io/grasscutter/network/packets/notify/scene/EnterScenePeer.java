package io.grasscutter.network.packets.notify.scene;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.EnterScenePeerNotifyOuterClass.EnterScenePeerNotify;
import lombok.AllArgsConstructor;

/** Scene peer ID notification. {@link PacketIds#EnterScenePeerNotify}. */
@AllArgsConstructor
public final class EnterScenePeer extends BasePacket<Empty, EnterScenePeerNotify> {
    private final Player player;

    @Override
    public EnterScenePeerNotify preparePacket() {
        return EnterScenePeerNotify.newBuilder()
                .setDestSceneId(this.player.getSceneId())
                .setPeerId(this.player.getPeerId())
                .setHostPeerId(this.player.getWorld().getOwner().getPeerId())
                .setEnterSceneToken(this.player.getSceneToken())
                .build();
    }
}
