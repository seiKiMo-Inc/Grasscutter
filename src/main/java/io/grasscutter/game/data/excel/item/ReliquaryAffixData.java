package io.grasscutter.game.data.excel.item;

import com.google.gson.annotations.SerializedName;
import io.grasscutter.game.data.GameResource;
import io.grasscutter.game.data.Resource;
import io.grasscutter.utils.enums.game.FightProperty;
import lombok.Getter;

@Resource(name = "ReliquaryAffixExcelConfigData.json")
@Getter
public final class ReliquaryAffixData implements GameResource {
    @Getter(onMethod = @__(@Override))
    private int id;

    private int depotId;
    private int groupId;

    @SerializedName("propType")
    private FightProperty property;

    @SerializedName("propValue")
    private float value;

    private int weight;
    private int upgradeWeight;
}
