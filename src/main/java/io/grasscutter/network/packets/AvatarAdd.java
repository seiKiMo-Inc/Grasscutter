package io.grasscutter.network.packets;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Avatar;
import io.grasscutter.proto.AvatarAddNotifyOuterClass.AvatarAddNotify;
import lombok.AllArgsConstructor;

/** Wind seed client packet. {@link PacketIds#AvatarAddNotify}. */
@AllArgsConstructor
public final class AvatarAdd extends BasePacket<Empty, AvatarAddNotify> {
    private final Avatar avatar;
    private final boolean prompt;

    @Override
    public AvatarAddNotify preparePacket() {
        return AvatarAddNotify.newBuilder()
                .setAvatar(this.avatar.toProto())
                .setIsInTeam(this.prompt)
                .build();
    }
}
