package io.grasscutter.game.data.excel.avatar;

import io.grasscutter.game.data.GameResource;
import io.grasscutter.game.data.Resource;
import io.grasscutter.game.data.common.CurveInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import lombok.Getter;

/* The character's leveling curves. */
@Resource(name = "AvatarCurveExcelConfigData.json")
@Getter
public final class AvatarCurveData implements GameResource {
    private int level;
    private CurveInfo[] curveInfos;

    private Map<String, Float> curveInfo;

    @Override
    public int getId() {
        return this.level;
    }

    @Override
    public void onLoad() {
        this.curveInfo = new HashMap<>();
        Stream.of(this.curveInfos).forEach(info -> this.curveInfo.put(info.getType(), info.getValue()));
    }
}
