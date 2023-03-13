package io.grasscutter.network.packets.notify.world;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.WorldDataNotifyOuterClass.WorldDataNotify;
import io.grasscutter.utils.ServerUtils;
import io.grasscutter.world.World;
import lombok.AllArgsConstructor;

/** The properties of a world. {@link PacketIds#WorldDataNotify}. */
@AllArgsConstructor
public final class WorldData extends BasePacket<Empty, WorldDataNotify> {
    private final World world;

    @Override
    public WorldDataNotify preparePacket() {
        return WorldDataNotify.newBuilder()
                .putWorldPropMap(1, ServerUtils.property(1, this.world.getWorldLevel()))
                .putWorldPropMap(2, ServerUtils.property(2, this.world.isMultiplayer() ? 1 : 0))
                .build();
    }
}
