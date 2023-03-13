package io.grasscutter.network.packets.scene;

import io.grasscutter.network.NetworkSession;
import io.grasscutter.network.packets.notify.scene.EnterScenePeer;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.EnterSceneReadyReqOuterClass.EnterSceneReadyReq;
import io.grasscutter.proto.EnterSceneReadyRspOuterClass.EnterSceneReadyRsp;
import io.grasscutter.proto.PacketHeadOuterClass.PacketHead;

/** Ready to enter the scene. {@link PacketIds#EnterSceneReadyReq} and {@link PacketIds#EnterSceneReadyRsp}. */
public final class EnterSceneReady extends BasePacket<EnterSceneReadyReq, EnterSceneReadyRsp> {
    private Player player;

    public EnterSceneReady() {
        // Empty constructor for handling.
    }

    public EnterSceneReady(Player player) {
        this.player = player;
    }

    @Override
    protected void handlePacket(NetworkSession session, PacketHead header, EnterSceneReadyReq message) {
        session.send(new EnterScenePeer(session.getPlayer()));
        session.send(new EnterSceneReady(session.getPlayer()));
    }

    @Override
    public EnterSceneReadyRsp preparePacket() {
        this.buildHeaderWith(11);

        return EnterSceneReadyRsp.newBuilder()
                .setEnterSceneToken(this.player.getSceneToken())
                .build();
    }
}
