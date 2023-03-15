package io.grasscutter.network.packets.notify.inventory;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Avatar;
import io.grasscutter.proto.AvatarAddNotifyOuterClass.AvatarAddNotify;
import io.grasscutter.proto.AvatarFightPropNotifyOuterClass;
import io.grasscutter.proto.AvatarFightPropNotifyOuterClass.AvatarFightPropNotify;
import lombok.AllArgsConstructor;

/** Combat properties for avatar have changed. {@link PacketIds#AvatarFightPropNotify}. */
@AllArgsConstructor
public final class AvatarFightProp extends BasePacket<Empty, AvatarFightPropNotify> {
    private final Avatar avatar;

    @Override
    public AvatarFightPropNotify preparePacket() {
        return AvatarFightPropNotify.newBuilder()
                .setAvatarGuid(this.avatar.getGuid())
                .putAllFightPropMap(this.avatar.getCombatProperties())
                .build();
    }
}
