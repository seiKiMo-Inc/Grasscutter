package io.grasscutter.network.packets.scene;

import io.grasscutter.network.NetworkSession;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.EnterSceneDoneReqOuterClass.EnterSceneDoneReq;
import io.grasscutter.proto.EnterSceneDoneRspOuterClass.EnterSceneDoneRsp;
import io.grasscutter.proto.PacketHeadOuterClass.PacketHead;
import io.grasscutter.utils.enums.game.SceneLoadState;

/** Client has finished entering scene. {@link PacketIds#EnterSceneDoneReq} and {@link PacketIds#EnterSceneDoneRsp}. */
public final class EnterSceneDone extends BasePacket<EnterSceneDoneReq, EnterSceneDoneRsp> {
    private Player player;

    public EnterSceneDone() {
        // Empty constructor for handling.
    }

    /**
     * Constructor for response.
     *
     * @param player The player.
     */
    public EnterSceneDone(Player player) {
        this.player = player;
    }

    @Override
    protected void handlePacket(NetworkSession session, PacketHead header, EnterSceneDoneReq message) {
        var player = session.getPlayer();
        // Set the scene state to loaded.
        player.setSceneState(SceneLoadState.LOADED);

        // Spawn entities.
        var scene = player.getScene();
        scene.spawnPlayer(player);
        scene.showEntities(player);

        // TODO: Send location packets.

        // Send response packet.
        session.send(new EnterSceneDone(player));
    }

    @Override
    public EnterSceneDoneRsp preparePacket() {
        return EnterSceneDoneRsp.newBuilder()
                .setEnterSceneToken(this.player.getSceneToken())
                .build();
    }
}
