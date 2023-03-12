package io.grasscutter.network.packets;

import com.google.protobuf.Empty;
import io.grasscutter.network.NetworkSession;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.PacketHeadOuterClass.PacketHead;
import io.grasscutter.proto.WindSeedClientNotifyOuterClass.WindSeedClientNotify;

/** Wind seed client packet. {@link PacketIds#WindSeedClientNotify}. */
public final class WindSeedClient extends BasePacket<Empty, WindSeedClientNotify> {
    public WindSeedClient() {
        // Empty constructor for handling.
    }

    @Override
    protected void handlePacket(NetworkSession session, PacketHead header, Empty message) {
        super.handlePacket(session, header, message);
    }

    @Override
    public WindSeedClientNotify preparePacket() {
        return super.preparePacket();
    }
}
