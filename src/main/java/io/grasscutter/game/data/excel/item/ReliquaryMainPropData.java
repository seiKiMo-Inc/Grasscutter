package io.grasscutter.game.data.excel.item;

import com.google.gson.annotations.SerializedName;
import io.grasscutter.game.data.GameResource;
import io.grasscutter.game.data.Resource;
import io.grasscutter.utils.enums.game.FightProperty;
import lombok.Getter;

@Resource(name = "ReliquaryMainPropExcelConfigData.json")
@Getter public final class ReliquaryMainPropData implements GameResource {
    @Getter(onMethod = @__(@Override))
    private int id;
    private int propDepotId;

    @SerializedName("propType")
    private FightProperty property;

    private int weight;
}
