package io.grasscutter.network.packets.notify.data;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.PlayerStoreNotifyOuterClass.PlayerStoreNotify;
import io.grasscutter.proto.StoreTypeOuterClass.StoreType;
import io.grasscutter.utils.constants.Properties;
import lombok.AllArgsConstructor;

/** Player items. {@link PacketIds#PlayerStoreNotify}. */
@AllArgsConstructor
public final class PlayerStore extends BasePacket<Empty, PlayerStoreNotify> {
    private final Player player;

    @Override
    public PlayerStoreNotify preparePacket() {
        this.buildHeaderWith(2);

        var packet = PlayerStoreNotify.newBuilder()
                .setStoreType(StoreType.STORE_TYPE_PACK)
                .setWeightLimit(Properties.LIMITS().all);

        // Add the player's items.
        // TODO: Add the player's items.

        return packet.build();
    }
}
