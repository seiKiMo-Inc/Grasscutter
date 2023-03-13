package io.grasscutter.network.packets.notify.world;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.PlayerWorldSceneInfoListNotifyOuterClass.PlayerWorldSceneInfoListNotify;
import io.grasscutter.proto.PlayerWorldSceneInfoOuterClass.PlayerWorldSceneInfo;
import lombok.AllArgsConstructor;

import java.util.stream.IntStream;

/** The info list of the world's scene. {@link PacketIds#PlayerWorldSceneInfoListNotify}. */
@AllArgsConstructor
public final class PlayerWorldSceneInfoList extends BasePacket<Empty, PlayerWorldSceneInfoListNotify> {
    @Override
    public PlayerWorldSceneInfoListNotify preparePacket() {
        return PlayerWorldSceneInfoListNotify.newBuilder()
                .addInfoList(PlayerWorldSceneInfo.newBuilder()
                        .setSceneId(1)
                        .setIsLocked(false))
                .addInfoList(PlayerWorldSceneInfo.newBuilder()
                        .setSceneId(3)
                        .setIsLocked(false)
                        .addSceneTagIdList(102) // Jade chamber
                        .addSceneTagIdList(113)
                        .addSceneTagIdList(117)

                        // Vanarana (Sumeru tree)
                        .addSceneTagIdList(1093) // Vana_real
                        // .addSceneTagIdList(1094) // Vana_dream
                        // .addSceneTagIdList(1095) // Vana_first
                        // .addSceneTagIdList(1096) // Vana_festival

                        // 3.1 event
                        .addSceneTagIdList(152)
                        .addSceneTagIdList(153)

                        // Pyramid
                        .addSceneTagIdList(1164) // Arena (XMSM_CWLTop)
                        .addSceneTagIdList(1166)) // Pyramid (CWL_Trans_02)

                        // Brute force
                        // .addAllSceneTagIdList(IntStream.range(1150, 1250).boxed().toList())
                .addInfoList(PlayerWorldSceneInfo.newBuilder()
                        .setSceneId(4)
                        .addSceneTagIdList(106)
                        .addSceneTagIdList(109)
                        .addSceneTagIdList(117)
                        .setIsLocked(false))
                .addInfoList(PlayerWorldSceneInfo.newBuilder()
                        .setSceneId(5)
                        .setIsLocked(false))
                .addInfoList(PlayerWorldSceneInfo.newBuilder()
                        .setSceneId(6)
                        .setIsLocked(false))
                .addInfoList(PlayerWorldSceneInfo.newBuilder()
                        .setSceneId(7)
                        .setIsLocked(false))
                .addInfoList(PlayerWorldSceneInfo.newBuilder()
                        .setSceneId(9)
                        .addAllSceneTagIdList(IntStream.range(0, 3000).boxed().toList())
                        .setIsLocked(false))
                .build();
    }
}
