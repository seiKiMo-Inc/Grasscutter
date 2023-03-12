package io.grasscutter.network.packets;

import io.grasscutter.network.NetworkSession;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Avatar;
import io.grasscutter.proto.PacketHeadOuterClass.PacketHead;
import io.grasscutter.proto.SetPlayerBornDataReqOuterClass.SetPlayerBornDataReq;
import io.grasscutter.utils.constants.GameConstants;

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

        // Get the skill data for the avatar.
        var avatarId = message.getAvatarId();
        var depotId = switch (avatarId) {
            case GameConstants.MAIN_CHARACTER_MALE -> 504;
            case GameConstants.MAIN_CHARACTER_FEMALE -> 704;
            default -> throw new IllegalStateException("Unexpected value: " + avatarId);
        };

        // Check if the player already has a main character.
        if (player.getAvatars().count() < 1) {
            // Create the main character avatar.
            var avatar = new Avatar(avatarId);
            // TODO: Set the skill depot.

            // Add the avatar to the player's team.
            player.getAvatars().add(avatar);
            // Set the player's basic data.
            player.setMainCharacter(avatarId);
            player.setProfileIcon(avatarId);
            player.save();
        } else return;

        player.doLogin(); // Perform login sequence.
        session.send(new SetPlayerBornData()); // Send response packet.

        // TODO: Send welcome message.
    }

    @Override
    public SetPlayerBornDataRsp preparePacket() {
        return super.preparePacket();
    }
}
