package io.grasscutter.network.packets;

import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.network.NetworkSession;
import io.grasscutter.proto.PacketHeadOuterClass.PacketHead;
import io.grasscutter.proto.PingReqOuterClass.PingReq;
import io.grasscutter.proto.PingRspOuterClass.PingRsp;

/** Ping packet. {@link PacketIds#PingReq} and {@link PacketIds#PingRsp}. */
public final class Ping extends BasePacket<PingReq, PingRsp> {
    private int sequenceId, time;

    public Ping() {
        // Empty constructor for handling.
    }

    private Ping(int sequenceId, int time) {
        this.sequenceId = sequenceId;
        this.time = time;
    }

    @Override
    public void handlePacket(NetworkSession session, PacketHead header, PingReq message) {
        session.updateLastPingTime(message.getClientTime());
        session.send(new Ping(header.getClientSequenceId(), message.getClientTime()));
    }

    @Override
    public PingRsp preparePacket() {
        this.buildHeaderWith(this.sequenceId);

        return PingRsp.newBuilder().setClientTime(time).build();
    }
}
