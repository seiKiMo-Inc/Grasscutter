package io.grasscutter.network.packets.notify.inventory;

import com.google.protobuf.Empty;
import io.grasscutter.game.inventory.Item;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.ItemAddHintNotifyOuterClass.ItemAddHintNotify;
import io.grasscutter.utils.enums.game.ActionReason;
import lombok.AllArgsConstructor;

/** A popup for a new item. {@link PacketIds#ItemAddHintNotify}. */
@AllArgsConstructor
public final class ItemAddHint extends BasePacket<Empty, ItemAddHintNotify> {
    private final Item item;
    private final ActionReason reason;

    @Override
    public ItemAddHintNotify preparePacket() {
        return ItemAddHintNotify.newBuilder()
                .addItemList(this.item.toHint())
                .setReason(this.reason.getValue())
                .build();
    }
}
