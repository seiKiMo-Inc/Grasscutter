package io.grasscutter.network.packets.scene;

import io.grasscutter.network.NetworkSession;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.PacketHeadOuterClass.PacketHead;
import io.grasscutter.proto.PostEnterSceneReqOuterClass.PostEnterSceneReq;
import io.grasscutter.proto.PostEnterSceneRspOuterClass.PostEnterSceneRsp;

/** Client is ready to enter scene. {@link PacketIds#PostEnterSceneReq} and {@link PacketIds#PostEnterSceneRsp}. */
public final class PostEnterScene extends BasePacket<PostEnterSceneReq, PostEnterSceneRsp> {
    private Player player;

    public PostEnterScene() {
        // Empty constructor for handling.
    }

    /**
     * Constructor for response.
     *
     * @param player The player.
     */
    public PostEnterScene(Player player) {
        this.player = player;
    }

    @Override
    protected void handlePacket(NetworkSession session, PacketHead header, PostEnterSceneReq message) {
        session.send(new PostEnterScene(session.getPlayer())); // Send response packet.
    }

    @Override
    public PostEnterSceneRsp preparePacket() {
        return PostEnterSceneRsp.newBuilder()
                .setEnterSceneToken(this.player.getSceneToken())
                .build();
    }
}
