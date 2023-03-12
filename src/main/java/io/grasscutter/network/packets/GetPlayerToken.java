package io.grasscutter.network.packets;

import io.grasscutter.network.NetworkSession;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.GetPlayerTokenReqOuterClass.GetPlayerTokenReq;
import io.grasscutter.proto.GetPlayerTokenRspOuterClass.GetPlayerTokenRsp;
import io.grasscutter.proto.PacketHeadOuterClass.PacketHead;
import io.grasscutter.proto.RetcodeOuterClass.Retcode;
import io.grasscutter.utils.CryptoUtils;
import io.grasscutter.utils.DatabaseUtils;
import io.grasscutter.utils.EncodingUtils;
import io.grasscutter.utils.Preconditions;
import io.grasscutter.utils.constants.CryptoConstants;
import io.grasscutter.utils.constants.GameConstants;
import io.grasscutter.utils.enums.KeyType;
import io.grasscutter.utils.enums.PlayerState;

import java.nio.ByteBuffer;
import java.util.Objects;

/** Ping packet. {@link PacketIds#PingReq} and {@link PacketIds#PingRsp}. */
public final class GetPlayerToken extends BasePacket<GetPlayerTokenReq, GetPlayerTokenRsp> {
    private NetworkSession session;

    private int banExpires = -1;

    private boolean encrypted = false;
    private String seed, seedSignature;

    public GetPlayerToken() {
        // Empty constructor for handling.
    }

    private GetPlayerToken(NetworkSession session) {
        this.session = session;
    }

    @Override
    protected void handlePacket(NetworkSession session, PacketHead header, GetPlayerTokenReq message) {
        // Fetch the account by the account ID and token.
        var account = DatabaseUtils.fetchAccount(Long.parseLong(message.getAccountUid()));
        if (account == null || !account.loginToken.equals(message.getAccountToken())) return;

        session.setAccount(account); // Set the account for the session.
        // TODO: Check if the player has logged in on the server.
        // TODO: Check if the player is banned from logging in.

        // Fetch the player by the account.
        var player = DatabaseUtils.fetchPlayer(account);
        if (player == null) {
            // Create a new player.
            player = new Player(
                    account.gameUserId,
                    account.id);
            player.save(); // Save the player.
        }
        Preconditions.validPlayer(player);

        // Set the player for the session.
        player.setSession(session);
        player.setAccount(account);
        session.setPlayer(player);

        player.loadAllData(); // Load the player's data.
        player.setState(PlayerState.LOGIN); // Set the player's state.
        session.setEncrypted(true); // Enable packet encryption.

        // Check if the seed is encrypted.
        this.encrypted = message.getKeyId() > 0;
        if (this.encrypted) try {
            var cipher = KeyType.SIGNING.decrypt(CryptoConstants.ENCRYPTION_TYPE);

            // Get the client seed.
            var clientSeed = EncodingUtils.fromSBase64(message.getClientRandKey());
            var clientSeedDecrypted = ByteBuffer.wrap(cipher.doFinal(clientSeed)).getLong();

            // Get the server seed.
            var serverSeed = ByteBuffer.wrap(new byte[8])
                    .putLong(CryptoConstants.ENCRYPT_SEED ^ clientSeedDecrypted)
                    .array();
            cipher = KeyType.valueOf("PUBLIC_" + message.getKeyId())
                    .encrypt(CryptoConstants.ENCRYPTION_TYPE);
            var serverSeedEncrypted = cipher.doFinal(serverSeed);

            // Create a signature for the server seed.
            var signature = KeyType.SIGNING
                    .signature(CryptoConstants.SIGNATURE_TYPE);
            Objects.requireNonNull(signature)
                    .update(serverSeed);

            this.seed = new String(EncodingUtils.toBase64(serverSeedEncrypted));
            this.seedSignature = new String(EncodingUtils.toBase64(signature.sign()));
        } catch (Exception ignored) { // Backwards compatability with the UserAssembly.dll patch.
            var clientSeed = EncodingUtils.fromBase64(message.getClientRandKey().getBytes());
            var serverSeed = EncodingUtils.toBytes(CryptoConstants.ENCRYPT_SEED);
            CryptoUtils.performXor(clientSeed, serverSeed);

            this.seed = new String(EncodingUtils.toBase64(clientSeed));
            this.seedSignature = CryptoConstants.LEGACY_SEED_SIGNATURE;
        }

        // Send the response.
        this.session = session;
        session.send(this);
    }

    @Override
    public GetPlayerTokenRsp preparePacket() {
        this.buildHeader();
        this.setKeyType(KeyType.DISPATCH);

        var player = this.session.getPlayer();
        var account = this.session.getAccount();
        var baseResponse = GetPlayerTokenRsp.newBuilder()
                .setUid(player.getUserId())
                .setAccountType(1)
                .setIsProficientPlayer(false) // TODO: Check if the player is proficient.
                .setPlatformType(3)
                .setRegPlatform(3)
                .setCountryCode("US")
                .setClientIpStr(this.session.getPrettyAddress(false));

        if (this.banExpires > 0) {
            // Send the response associated with a ban.
            return baseResponse
                    .setRetcode(Retcode.RET_BLACK_UID_VALUE)
                    .setMsg("FORBID_CHEATING_PLUGINS")
                    .setBlackUidEndTime(this.banExpires)
                    .build();
        }

        // Set headers for a successful login.
        baseResponse = baseResponse
                .setChannelId(1)
                .setToken(account.loginToken)
                .setSecretKeySeed(CryptoConstants.ENCRYPT_SEED)
                .setSecurityCmdBuffer(CryptoConstants.ENCRYPT_SEED_BUFFER.get())
                .setClientVersionRandomKey(GameConstants.VERSION_KEY);
        // Set the headers for an encrypted login.
        if (this.encrypted) {
            baseResponse = baseResponse
                    .setServerRandKey(this.seed)
                    .setSign(this.seedSignature);
        }

        return baseResponse.build();
    }
}
