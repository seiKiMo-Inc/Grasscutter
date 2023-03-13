package io.grasscutter.network.packets.notify.scene;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.SceneTimeNotifyOuterClass.SceneTimeNotify;
import lombok.AllArgsConstructor;

/** The scene's time and basic data. {@link PacketIds#SceneTimeNotify}. */
@AllArgsConstructor
public final class SceneTime extends BasePacket<Empty, SceneTimeNotify> {
    private final Player player;

    @Override
    public SceneTimeNotify preparePacket() {
        return SceneTimeNotify.newBuilder()
                .setIsPaused(this.player.isPaused())
                .setSceneId(this.player.getSceneId())
                .setSceneTime(this.player.getScene().getSceneTime())
                .build();
    }
}
