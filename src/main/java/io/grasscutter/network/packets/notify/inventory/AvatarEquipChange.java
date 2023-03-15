package io.grasscutter.network.packets.notify.inventory;

import com.google.protobuf.Empty;
import io.grasscutter.game.inventory.Item;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.player.Avatar;
import io.grasscutter.proto.AvatarEquipChangeNotifyOuterClass.AvatarEquipChangeNotify;
import io.grasscutter.utils.enums.game.EquipType;
import lombok.AllArgsConstructor;

/** Change the equipped item on an avatar. {@link PacketIds#AvatarEquipChangeNotify}. */
@AllArgsConstructor
public final class AvatarEquipChange extends BasePacket<Empty, AvatarEquipChangeNotify> {
    private final Avatar avatar;
    private final Item item;

    @Override
    public AvatarEquipChangeNotify preparePacket() {
        var packet =
                AvatarEquipChangeNotify.newBuilder()
                        .setAvatarGuid(this.avatar.getGuid())
                        .setEquipType(this.item.getSlot())
                        .setItemId(this.item.getItemId())
                        .setEquipGuid(this.item.getItemGuid());

        // Set the packet's extra data.
        if (this.item.getItemData().getEquipType() == EquipType.EQUIP_WEAPON)
            packet.setWeapon(this.item.toWeaponInfo());
        // else
        //     packet.setReliquary(this.item.toReliquaryInfo());

        return packet.build();
    }
}
