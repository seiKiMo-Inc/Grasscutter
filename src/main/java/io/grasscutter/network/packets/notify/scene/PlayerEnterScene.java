package io.grasscutter.network.packets.notify.scene;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.EnterReasonOuterClass.EnterReason;
import io.grasscutter.proto.EnterTypeOuterClass.EnterType;
import io.grasscutter.proto.PlayerEnterSceneNotifyOuterClass.PlayerEnterSceneNotify;
import io.grasscutter.utils.CryptoUtils;
import io.grasscutter.utils.ServerUtils;
import io.grasscutter.utils.enums.game.SceneLoadState;

/** Scene data notification. {@link PacketIds#PlayerEnterSceneNotify}. */
public final class PlayerEnterScene extends BasePacket<Empty, PlayerEnterSceneNotify> {
    private final Player player;

    /**
     * Constructor for login.
     *
     * @param player The player.
     */
    public PlayerEnterScene(Player player) {
        this.player = player;

        player.setSceneState(SceneLoadState.LOADING);
        player.setSceneToken(CryptoUtils.randomNumber(1000, 99999));
    }

    @Override
    public PlayerEnterSceneNotify preparePacket() {
        return PlayerEnterSceneNotify.newBuilder()
                .setSceneId(this.player.getSceneId())
                .setPos(this.player.getPosition().toProto())
                .setSceneBeginTime(System.currentTimeMillis())
                .setType(EnterType.ENTER_TYPE_SELF)
                .setTargetUid(this.player.getUserId())
                .setEnterSceneToken(this.player.getSceneToken())
                .setWorldLevel(this.player.getProperties().getWorldLevel())
                .setEnterReason(EnterReason.ENTER_REASON_LOGIN.getNumber())
                .setIsFirstLoginEnterScene(true) // TODO: Fetch from player.
                .setWorldType(1)
                .setSceneTransaction(ServerUtils.sceneTransaction(this.player))
                .build();
    }
}
