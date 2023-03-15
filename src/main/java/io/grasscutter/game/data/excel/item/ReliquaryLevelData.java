package io.grasscutter.game.data.excel.item;

import io.grasscutter.game.data.GameResource;
import io.grasscutter.game.data.Resource;
import io.grasscutter.utils.definitions.game.RelicLevelProperty;
import io.grasscutter.utils.enums.game.FightProperty;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import java.util.List;
import lombok.Getter;

@Resource(name = "ReliquaryLevelExcelConfigData.json")
public final class ReliquaryLevelData implements GameResource {
    @Getter(onMethod = @__(@Override))
    private int id;

    private Int2FloatMap propMap;

    @Getter private int rank;
    @Getter private int level;
    @Getter private int exp;
    private List<RelicLevelProperty> addProps;

    /**
     * Fetches the value of a property.
     *
     * @param property The property to fetch.
     * @return The value of the property.
     */
    public float getValue(FightProperty property) {
        return this.getPropValue(property.getId());
    }

    /**
     * Fetches the value of a property.
     *
     * @param id The ID of the property to fetch.
     * @return The value of the property.
     */
    public float getPropValue(int id) {
        return this.propMap.getOrDefault(id, 0f);
    }

    @Override
    public void onLoad() {
        this.id = (rank << 8) + this.getLevel();
        this.propMap = new Int2FloatOpenHashMap();
        for (var property : this.addProps) {
            this.propMap.put(property.getProperty().getId(), property.getValue());
        }
    }
}
