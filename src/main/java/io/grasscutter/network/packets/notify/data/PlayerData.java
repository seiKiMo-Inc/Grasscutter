package io.grasscutter.network.packets.notify.data;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Player;
import io.grasscutter.proto.PlayerDataNotifyOuterClass.PlayerDataNotify;
import io.grasscutter.utils.ServerUtils;
import lombok.AllArgsConstructor;

/** Player properties. {@link PacketIds#PlayerDataNotify}. */
@AllArgsConstructor
public final class PlayerData extends BasePacket<Empty, PlayerDataNotify> {
    private final Player player;

    @Override
    public PlayerDataNotify preparePacket() {
        this.buildHeaderWith(2);

        var packet =
                PlayerDataNotify.newBuilder()
                        .setNickName(this.player.getNickName())
                        .setServerTime(System.currentTimeMillis())
                        .setIsFirstLoginToday(true) // TODO: Add this property to the player.
                        .setRegionId(this.player.getRegionId());

        // Set the player properties.
        this.player
                .getProperties()
                .forEach(
                        (property, value) ->
                                packet.putPropMap(property, ServerUtils.property(property, value)));

        return packet.build();
    }
}
