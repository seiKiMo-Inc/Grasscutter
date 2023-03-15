package io.grasscutter.game.data.excel.avatar;

import com.google.gson.annotations.SerializedName;
import io.grasscutter.game.data.GameResource;
import io.grasscutter.game.data.Resource;
import io.grasscutter.game.data.common.FightPropertyData;
import io.grasscutter.utils.enums.Priority;
import java.util.ArrayList;
import lombok.Getter;

/* Avatar talent details. */
@Resource(name = "AvatarTalentExcelConfigData.json", priority = Priority.HIGHEST)
@Getter
public final class AvatarTalentData implements GameResource {
    @Getter(onMethod = @__(@Override))
    @SerializedName("talentId")
    private int id;

    private int prevTalent;
    private long nameTextMapHash;
    private String icon;
    private int mainCostItemId;
    private int mainCostItemCount;
    private String openConfig;
    private FightPropertyData[] addProps;
    private float[] paramList;

    @Override
    public void onLoad() {
        var parsed = new ArrayList<FightPropertyData>(this.getAddProps().length);
        for (var prop : this.getAddProps()) {
            if (prop.getPropType() != null || prop.getValue() == 0f) {
                prop.onLoad();
                parsed.add(prop);
            }
        }

        this.addProps = parsed.toArray(new FightPropertyData[0]);
    }
}
