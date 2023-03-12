package io.grasscutter.network.packets;

import io.grasscutter.network.NetworkSession;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.PacketHeadOuterClass.PacketHead;
import io.grasscutter.proto.SetPlayerBornDataReqOuterClass.SetPlayerBornDataReq;

import static io.grasscutter.proto.SetPlayerBornDataRspOuterClass.*;

/** Set main character packet. {@link PacketIds#SetPlayerBornDataRsp} and {@link PacketIds#SetPlayerBornDataRsp}. */
public final class SetPlayerBornData extends BasePacket<SetPlayerBornDataReq, SetPlayerBornDataRsp> {
    public SetPlayerBornData() {
        // Empty constructor for handling.
    }

    @Override
    protected void handlePacket(NetworkSession session, PacketHead header, SetPlayerBornDataReq message) {
        // Set the player's nickname.
        var player = session.getPlayer();
        player.setNickName(message.getNickName());

        // Create the main character avatar.
        // TODO: Create the avatar.

        player.doLogin(); // Perform login sequence.
        session.send(new SetPlayerBornData()); // Send response packet.

        // TODO: Send welcome message.
    }

    @Override
    public SetPlayerBornDataRsp preparePacket() {
        return super.preparePacket();
    }
}
