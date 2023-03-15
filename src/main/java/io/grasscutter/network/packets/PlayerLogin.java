package io.grasscutter.network.packets;

import io.grasscutter.network.NetworkSession;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.PacketHeadOuterClass.PacketHead;
import io.grasscutter.proto.PlayerLoginReqOuterClass.PlayerLoginReq;
import io.grasscutter.proto.PlayerLoginRspOuterClass.PlayerLoginRsp;
import io.grasscutter.utils.Preconditions;
import io.grasscutter.utils.ServerUtils;
import io.grasscutter.utils.constants.GameConstants;
import io.grasscutter.utils.enums.KeyType;
import io.grasscutter.utils.enums.game.PlayerState;

/** Player login packet. {@link PacketIds#PlayerLoginReq} and {@link PacketIds#PlayerLoginRsp} */
public final class PlayerLogin extends BasePacket<PlayerLoginReq, PlayerLoginRsp> {
    public PlayerLogin() {
        // Empty constructor for handling.
        // Empty constructor for packet without data.
    }

    @Override
    protected void handlePacket(NetworkSession session, PacketHead header, PlayerLoginReq message) {
        // Check if the session is valid.
        Preconditions.loggedIn(session);

        // Check for a valid authentication token.
        if (!message.getToken().equals(session.getAccount().loginToken)) {
            session.close();
            return;
        }

        // Load the player from the database.
        var player = session.getPlayer();

        // Check if the player is new.
        if (player.getAvatars().count() < 1) {
            // Show the opening cutscene to the player.
            player.setState(PlayerState.CHARACTER);
            session.send(new GenericPacket(PacketIds.DoSetPlayerBornDataNotify));
        } else player.doLogin(); // Perform login sequence.

        // Send login response.
        session.send(new PlayerLogin());
    }

    @Override
    public PlayerLoginRsp preparePacket() {
        this.setKeyType(KeyType.DISPATCH);

        // Get the region data for this server.
        var regionData = ServerUtils.CURRENT_REGION.get().getRegionInfo();

        return PlayerLoginRsp.newBuilder()
                .setIsScOpen(false)
                .setIsUseAbilityHash(true)
                .setAbilityHashCode(GameConstants.ABILITY_HASH_CODE)
                .setCountryCode("US")
                .setRegisterCps("mihoyo")
                .setGameBiz(GameConstants.REGION)
                .setClientMd5(regionData.getClientDataMd5())
                .setClientDataVersion(regionData.getClientDataVersion())
                .setClientVersionSuffix(regionData.getClientVersionSuffix())
                .setClientSilenceMd5(regionData.getClientSilenceDataMd5())
                .setClientSilenceDataVersion(regionData.getClientSilenceDataVersion())
                .setClientSilenceVersionSuffix(regionData.getClientSilenceVersionSuffix())
                .build();
    }
}
