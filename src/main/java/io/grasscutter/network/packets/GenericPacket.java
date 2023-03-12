package io.grasscutter.network.packets;

import com.google.protobuf.Empty;
import com.google.protobuf.GeneratedMessageV3;
import io.grasscutter.network.protocol.BasePacket;
import lombok.Getter;

/** A unique packet without any output data. */
public final class GenericPacket extends BasePacket<Empty, GeneratedMessageV3> {
    @Getter private final int packetId;

    public GenericPacket(int packetId) {
        this.packetId = packetId;
    }

    @Override
    public GeneratedMessageV3 preparePacket() {
        this.setOtherPacketId(this.packetId);
        return null;
    }
}
