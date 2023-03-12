package io.grasscutter.network.packets;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.GetPlayerTokenRspOuterClass.GetPlayerTokenRsp;
import io.grasscutter.proto.PacketHeadOuterClass.PacketHead;

/** Wind seed client packet. {@link PacketIds#WindSeedClientNotify}. */
public final class WindSeedClient extends BasePacket<Empty, GetPlayerTokenRsp> {
    public WindSeedClient() {
        // Empty constructor for handling.
    }

    @Override
    public GetPlayerTokenRsp preparePacket() {
        return super.preparePacket();
    }
}
