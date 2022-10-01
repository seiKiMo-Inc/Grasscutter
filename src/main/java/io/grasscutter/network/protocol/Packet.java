package io.grasscutter.network.protocol;

import io.grasscutter.network.packets.Ping;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** A mapping of packet IDs to {@link BasePacket}s. */
@Getter
@AllArgsConstructor
public enum Packet {
    NONE(-1, -1, null),
    PING(PacketIds.PingReq, PacketIds.PingRsp, Ping.class);

    private final int handleId;
    private final int sendId;
    private final Class<? extends BasePacket<?, ?>> packet;
}
