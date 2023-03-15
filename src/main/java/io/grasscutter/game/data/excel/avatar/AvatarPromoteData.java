package io.grasscutter.game.data.excel.avatar;

import com.google.gson.annotations.SerializedName;
import io.grasscutter.game.data.GameResource;
import io.grasscutter.game.data.Resource;
import io.grasscutter.game.data.common.BasicItemData;
import lombok.Getter;

@Resource(name = "AvatarPromoteExcelConfigData.json")
@Getter
public final class AvatarPromoteData implements GameResource {
    @SerializedName("avatarPromoteId")
    private int avatarId;

    private int promoteLevel;

    @SerializedName("scoinCost")
    private int cost;

    private BasicItemData[] costItems;

    @SerializedName("unlockMaxLevel")
    private int maxLevel;

    private BasicItemData[] addProps;
    private int requiredPlayerLevel;

    @Override
    public int getId() {
        return (this.avatarId << 8) + this.promoteLevel;
    }
}
