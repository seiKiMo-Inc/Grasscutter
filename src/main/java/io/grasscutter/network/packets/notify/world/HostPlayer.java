package io.grasscutter.network.packets.notify.world;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.HostPlayerNotifyOuterClass.HostPlayerNotify;
import io.grasscutter.world.World;
import lombok.AllArgsConstructor;

/** The host of a world. {@link PacketIds#HostPlayerNotify}. */
@AllArgsConstructor
public final class HostPlayer extends BasePacket<Empty, HostPlayerNotify> {
    private final World world;

    @Override
    public HostPlayerNotify preparePacket() {
        var host = this.world.getOwner();
        return HostPlayerNotify.newBuilder()
                .setHostUid(host.getUserId())
                .setHostPeerId(host.getPeerId())
                .build();
    }
}
