package io.grasscutter.network.packets.scene;

import io.grasscutter.network.NetworkSession;
import io.grasscutter.network.packets.GenericPacket;
import io.grasscutter.network.packets.notify.ServerTime;
import io.grasscutter.network.packets.notify.SyncTeamEntity;
import io.grasscutter.network.packets.notify.scene.*;
import io.grasscutter.network.packets.notify.world.HostPlayer;
import io.grasscutter.network.packets.notify.world.PlayerWorldSceneInfoList;
import io.grasscutter.network.packets.notify.world.WorldData;
import io.grasscutter.network.packets.notify.world.WorldPlayerInfo;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.PacketHeadOuterClass.PacketHead;
import io.grasscutter.proto.SceneInitFinishReqOuterClass.SceneInitFinishReq;
import io.grasscutter.proto.SceneInitFinishRspOuterClass.SceneInitFinishRsp;
import io.grasscutter.utils.enums.game.SceneLoadState;

/** Scene initialization finished. {@link PacketIds#SceneInitFinishReq} and {@link PacketIds#SceneInitFinishRsp}. */
public final class SceneInitFinish extends BasePacket<SceneInitFinishReq, SceneInitFinishRsp> {
    private Player player;

    public SceneInitFinish() {
        // Empty constructor for handling.
    }

    public SceneInitFinish(Player player) {
        this.player = player;
    }

    @Override
    protected void handlePacket(NetworkSession session, PacketHead header, SceneInitFinishReq message) {
        var player = session.getPlayer();

        // Send world info packets.
        session.send(new ServerTime());
        session.send(new WorldPlayerInfo(player.getWorld()));
        session.send(new WorldData(player.getWorld()));
        session.send(new PlayerWorldSceneInfoList());
        session.send(new GenericPacket(PacketIds.SceneForceUnlockNotify));
        session.send(new HostPlayer(player.getWorld()));

        // Send scene info packets.
        session.send(new SceneTime(player));
        session.send(new PlayerGameTime(player));
        session.send(new PlayerEnterSceneInfo(player));
        session.send(new SceneAreaWeather(player));
        session.send(new ScenePlayerInfo(player.getWorld()));
        session.send(new SceneTeamUpdate(player));

        // Send sync packets.
        session.send(new SyncTeamEntity(player));
        session.send(new SyncScenePlayTeamEntity(player));

        // Send response packet.
        session.send(new SceneInitFinish(player));

        // Set the scene loading state.
        session.getPlayer().setSceneState(SceneLoadState.INIT);
    }

    @Override
    public SceneInitFinishRsp preparePacket() {
        this.buildHeaderWith(11);

        return SceneInitFinishRsp.newBuilder()
                .setEnterSceneToken(this.player.getSceneToken())
                .build();
    }
}
