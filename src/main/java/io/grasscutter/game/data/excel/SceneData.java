package io.grasscutter.game.data.excel;

import com.google.gson.annotations.SerializedName;
import io.grasscutter.game.data.GameResource;
import io.grasscutter.game.data.Resource;
import io.grasscutter.utils.enums.game.SceneType;
import lombok.Getter;

@Resource(name = "SceneExcelConfigData.json")
@Getter public final class SceneData implements GameResource {
    @Getter(onMethod = @__(@Override))
    private int id;

    @SerializedName("type")
    private SceneType sceneType;
    private String scriptData;
}
