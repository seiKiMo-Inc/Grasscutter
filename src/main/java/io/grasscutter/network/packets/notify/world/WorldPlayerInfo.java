package io.grasscutter.network.packets.notify.world;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.WorldPlayerInfoNotifyOuterClass.WorldPlayerInfoNotify;
import io.grasscutter.world.World;
import lombok.AllArgsConstructor;

/** Players in a world. {@link PacketIds#WorldPlayerInfoNotify}. */
@AllArgsConstructor
public final class WorldPlayerInfo extends BasePacket<Empty, WorldPlayerInfoNotify> {
    private final World world;

    @Override
    public WorldPlayerInfoNotify preparePacket() {
        var packet = WorldPlayerInfoNotify.newBuilder();

        // Add each player to the packet.
        this.world
                .getPlayers()
                .forEach(
                        player -> {
                            packet.addPlayerUidList(player.getUserId());
                            packet.addPlayerInfoList(player.toOnlinePlayerInfo());
                        });

        return packet.build();
    }
}
