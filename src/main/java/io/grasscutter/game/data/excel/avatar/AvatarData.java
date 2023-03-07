package io.grasscutter.game.data.excel.avatar;

import io.grasscutter.game.data.GameResource;
import io.grasscutter.game.data.Resource;
import io.grasscutter.utils.enums.Priority;
import lombok.Getter;

/* Game avatar data. */
@Resource(name = "AvatarExcelConfigData.json",
        priority = Priority.LOW)
public final class AvatarData implements GameResource {
    @Getter(onMethod = @__(@Override))
    private int id;

    @Override
    public void onLoad() {

    }
}
