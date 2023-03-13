package io.grasscutter.network.packets.notify.scene;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.ScenePlayerInfoNotifyOuterClass.ScenePlayerInfoNotify;
import io.grasscutter.world.World;
import lombok.AllArgsConstructor;

import static io.grasscutter.proto.ScenePlayerInfoOuterClass.ScenePlayerInfo.*;

/** Enter info for a scene's players. {@link PacketIds#ScenePlayerInfoNotify}. */
@AllArgsConstructor
public final class ScenePlayerInfo extends BasePacket<Empty, ScenePlayerInfoNotify> {
    private final World world;

    @Override
    public ScenePlayerInfoNotify preparePacket() {
        var packet = ScenePlayerInfoNotify.newBuilder();

        // Add the player info.
        this.world.forEach(player -> packet.addPlayerInfoList(newBuilder()
                .setUid(player.getUserId())
                .setPeerId(player.getPeerId())
                .setName(player.getNickName())
                .setSceneId(player.getSceneId())
                .setOnlinePlayerInfo(player.toOnlinePlayerInfo())));

        return packet.build();
    }
}
