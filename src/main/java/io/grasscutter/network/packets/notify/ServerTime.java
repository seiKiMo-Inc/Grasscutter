package io.grasscutter.network.packets.notify;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.ServerTimeNotifyOuterClass.ServerTimeNotify;

/** The server's set time. {@link PacketIds#ServerTimeNotify}. */
public final class ServerTime extends BasePacket<Empty, ServerTimeNotify> {
    @Override
    public ServerTimeNotify preparePacket() {
        return ServerTimeNotify.newBuilder()
                .setServerTime(System.currentTimeMillis())
                .build();
    }
}
