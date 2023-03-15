package io.grasscutter.network.protocol;

import com.google.protobuf.GeneratedMessageV3;
import io.grasscutter.network.packets.*;
import io.grasscutter.network.packets.notify.*;
import io.grasscutter.network.packets.notify.data.*;
import io.grasscutter.network.packets.notify.inventory.*;
import io.grasscutter.network.packets.notify.scene.*;
import io.grasscutter.network.packets.notify.world.*;
import io.grasscutter.network.packets.scene.*;
import io.grasscutter.proto.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** A mapping of packet IDs to {@link BasePacket}s. */
@Getter
@AllArgsConstructor
public enum Packet {
    NONE(-1, -1, null, null),
    PING(PacketIds.PingReq, PacketIds.PingRsp, Ping.class, PingReqOuterClass.PingReq.class),
    GET_PLAYER_TOKEN(
            PacketIds.GetPlayerTokenReq,
            PacketIds.GetPlayerTokenRsp,
            GetPlayerToken.class,
            GetPlayerTokenReqOuterClass.GetPlayerTokenReq.class),
    PLAYER_LOGIN(
            PacketIds.PlayerLoginReq,
            PacketIds.PlayerLoginRsp,
            PlayerLogin.class,
            PlayerLoginReqOuterClass.PlayerLoginReq.class),
    SET_PLAYER_BORN_DATA(
            PacketIds.SetPlayerBornDataReq,
            PacketIds.SetPlayerBornDataRsp,
            SetPlayerBornData.class,
            SetPlayerBornDataReqOuterClass.SetPlayerBornDataReq.class),
    ENTER_SCENE_DONE(
            PacketIds.EnterSceneDoneReq,
            PacketIds.EnterSceneDoneRsp,
            EnterSceneDone.class,
            EnterSceneDoneReqOuterClass.EnterSceneDoneReq.class),
    ENTER_SCENE_READY(
            PacketIds.EnterSceneReadyReq,
            PacketIds.EnterSceneReadyRsp,
            EnterSceneReady.class,
            EnterSceneReadyReqOuterClass.EnterSceneReadyReq.class),
    POST_ENTER_SCENE(
            PacketIds.PostEnterSceneReq,
            PacketIds.PostEnterSceneRsp,
            PostEnterScene.class,
            PostEnterSceneReqOuterClass.PostEnterSceneReq.class),
    SCENE_INIT_FINISH(
            PacketIds.SceneInitFinishReq,
            PacketIds.SceneInitFinishRsp,
            SceneInitFinish.class,
            SceneInitFinishReqOuterClass.SceneInitFinishReq.class),
    AVATAR_DATA(-1, PacketIds.AvatarDataNotify, AvatarData.class, null),
    PLAYER_DATA(-1, PacketIds.PlayerDataNotify, PlayerData.class, null),
    PLAYER_STORE(-1, PacketIds.PlayerStoreNotify, PlayerStore.class, null),
    STORE_WEIGHT_LIMIT(-1, PacketIds.StoreWeightLimitNotify, StoreWeightLimit.class, null),
    ENTER_SCENE_PEER(-1, PacketIds.EnterScenePeerNotify, EnterScenePeer.class, null),
    PLAYER_ENTER_SCENE(-1, PacketIds.PlayerEnterSceneNotify, PlayerEnterScene.class, null),
    PLAYER_ENTER_SCENE_INFO(
            -1, PacketIds.PlayerEnterSceneInfoNotify, PlayerEnterSceneInfo.class, null),
    PLAYER_GAME_TIME(-1, PacketIds.PlayerGameTimeNotify, PlayerGameTime.class, null),
    SCENE_AREA_WEATHER(-1, PacketIds.SceneAreaWeatherNotify, SceneAreaWeather.class, null),
    SCENE_ENTITY_APPEAR(-1, PacketIds.SceneEntityAppearNotify, SceneEntityAppear.class, null),
    SCENE_ENTITY_DISAPPEAR(
            -1, PacketIds.SceneEntityDisappearNotify, SceneEntityDisappear.class, null),
    SCENE_PLAYER_INFO(-1, PacketIds.ScenePlayerInfoNotify, ScenePlayerInfo.class, null),
    SCENE_TEAM_UPDATE(-1, PacketIds.SceneTeamUpdateNotify, SceneTeamUpdate.class, null),
    SCENE_TIME(-1, PacketIds.SceneTimeNotify, SceneTime.class, null),
    SYNC_SCENE_PLAY_TEAM_ENTITY(
            -1, PacketIds.SyncScenePlayTeamEntityNotify, SyncScenePlayTeamEntity.class, null),
    HOST_PLAYER(-1, PacketIds.HostPlayerNotify, HostPlayer.class, null),
    PLAYER_WORLD_SCENE_INFO_LIST(
            -1, PacketIds.PlayerWorldSceneInfoListNotify, PlayerWorldSceneInfoList.class, null),
    WORLD_DATA(-1, PacketIds.WorldDataNotify, WorldData.class, null),
    WORLD_PLAYER_INFO(-1, PacketIds.WorldPlayerInfoNotify, WorldPlayerInfo.class, null),
    AVATAR_ADD(-1, PacketIds.AvatarAddNotify, AvatarAdd.class, null),
    SERVER_TIME(-1, PacketIds.ServerTimeNotify, ServerTime.class, null),
    SYNC_TEAM_ENTITY(-1, PacketIds.SyncTeamEntityNotify, SyncTeamEntity.class, null),
    WIND_SEED_CLIENT(-1, PacketIds.WindSeedClientNotify, WindSeedClient.class, null),
    ITEM_ADD_HINT(-1, PacketIds.ItemAddHintNotify, ItemAddHint.class, null),
    AVATAR_EQUIP_CHANGE(-1, PacketIds.AvatarEquipChangeNotify, AvatarEquipChange.class, null);

    /**
     * Converts the name of a packet to a packet identifier.
     *
     * @param packetName The name of the packet.
     * @return The packet identifier.
     */
    public static Packet fromPacketName(String packetName) {
        // Split the name by uppercase letters.
        var split = packetName.split("(?=[A-Z])");

        var builder = new StringBuilder();
        for (var part : split) {
            // Append the part to the builder.
            builder.append(part.toLowerCase());
            builder.append('_');
        }

        // Remove the last underscore.
        builder.deleteCharAt(builder.length() - 1);

        try {
            // Get the packet by the name.
            return Packet.valueOf(builder.toString().toUpperCase());
        } catch (Exception ignored) {
            return Packet.NONE;
        }
    }

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
            return packet.getDeclaredConstructor().newInstance();
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
            return inbound.cast(inbound.getDeclaredMethod("parseFrom", byte[].class).invoke(null, data));
        } catch (Exception ignored) {
            return null;
        }
    }
}
