package io.grasscutter.network.packets;

import io.grasscutter.network.NetworkSession;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.GetPlayerTokenReqOuterClass.GetPlayerTokenReq;
import io.grasscutter.proto.GetPlayerTokenRspOuterClass.GetPlayerTokenRsp;
import io.grasscutter.proto.PacketHeadOuterClass.PacketHead;
import io.grasscutter.proto.RetcodeOuterClass.Retcode;
import io.grasscutter.utils.DatabaseUtils;

/** Ping packet. {@link PacketIds#PingReq} and {@link PacketIds#PingRsp}. */
public final class GetPlayerToken extends BasePacket<GetPlayerTokenReq, GetPlayerTokenRsp> {
    private NetworkSession session;

    public GetPlayerToken() {
        // Empty constructor for handling.
    }

    public GetPlayerToken(NetworkSession session) {
        this.session = session;
    }

    @Override
    protected void handlePacket(NetworkSession session, PacketHead header, GetPlayerTokenReq message) {
        // Fetch the account by the account ID and token.
        var account = DatabaseUtils.fetchAccount(Long.parseLong(message.getAccountUid()));
        if (account == null || !account.loginToken.equals(message.getAccountToken())) return;

        session.setAccount(account); // Set the account for the session.
        // TODO: Check if the player has logged in on the server.

        session.send(new GetPlayerToken(session));
    }

    @Override
    public GetPlayerTokenRsp preparePacket() {
        this.buildHeader();
        this.setShouldEncrypt(true);

        return GetPlayerTokenRsp.newBuilder()
                .setUid((int) this.session.getAccount().id)
                .setIsProficientPlayer(true) // TODO: Count the number of avatars.
                .setRetcode(Retcode.RET_BLACK_UID_VALUE)
                .setMsg("FORBID_CHEATING_PLUGINS")
                .setBlackUidEndTime((int) System.currentTimeMillis() + 10000)
                .setRegPlatform(3)
                .setCountryCode("US")
                .setClientIpStr(this.session.getPrettyAddress(false))
                .build();
    }
}
