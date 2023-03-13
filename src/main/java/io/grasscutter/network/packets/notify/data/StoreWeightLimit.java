package io.grasscutter.network.packets.notify.data;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.StoreTypeOuterClass.StoreType;
import io.grasscutter.proto.StoreWeightLimitNotifyOuterClass.StoreWeightLimitNotify;
import io.grasscutter.utils.constants.Properties;

/** Limits for the player's inventory. {@link PacketIds#StoreWeightLimitNotify}. */
public final class StoreWeightLimit extends BasePacket<Empty, StoreWeightLimitNotify> {
    public StoreWeightLimit() {
        // Empty constructor for handling.
    }

    @Override
    public StoreWeightLimitNotify preparePacket() {
        return StoreWeightLimitNotify.newBuilder()
                .setStoreType(StoreType.STORE_TYPE_PACK)
                .setWeightLimit(Properties.LIMITS().all)
                .setWeaponCountLimit(Properties.LIMITS().weapons)
                .setReliquaryCountLimit(Properties.LIMITS().artifacts)
                .setMaterialCountLimit(Properties.LIMITS().materials)
                .setFurnitureCountLimit(Properties.LIMITS().furniture)
                .build();
    }
}
