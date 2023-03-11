package io.grasscutter.network.protocol;

import com.google.protobuf.GeneratedMessageV3;
import io.grasscutter.network.packets.*;
import io.grasscutter.proto.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** A mapping of packet IDs to {@link BasePacket}s. */
@Getter
@AllArgsConstructor
public enum Packet {
    NONE(-1, -1, null, null),
    PING(PacketIds.PingReq, PacketIds.PingRsp,
            Ping.class, PingReqOuterClass.PingReq.class),
    GET_PLAYER_TOKEN(PacketIds.GetPlayerTokenReq, PacketIds.GetPlayerTokenRsp,
            GetPlayerToken.class, GetPlayerTokenReqOuterClass.GetPlayerTokenReq.class);

    private final int handleId;
    private final int sendId;
    private final Class<? extends BasePacket<?, ?>> packet;
    private final Class<? extends GeneratedMessageV3> inbound;

    /**
     * Creates a new instance of the packet.
     *
     * @return The new instance.
     */
    public BasePacket<?, ?> newInstance() {
        try {
            return packet.getDeclaredConstructor()
                    .newInstance();
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Creates a new instance of the inbound message.
     *
     * @param data The data to parse.
     * @return The new instance.
     */
    @SuppressWarnings("PrimitiveArrayArgumentToVarargsMethod")
    public GeneratedMessageV3 fromData(byte[] data) {
        try {
            return inbound.cast(inbound
                    .getDeclaredMethod("parseFrom", byte[].class)
                    .invoke(null, data));
        } catch (Exception ignored) {
            return null;
        }
    }
}
