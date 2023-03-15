package io.grasscutter.game.data;

import io.grasscutter.game.data.bin.AvatarConfig;
import io.grasscutter.game.data.excel.ItemData;
import io.grasscutter.game.data.excel.SceneData;
import io.grasscutter.game.data.excel.avatar.*;
import io.grasscutter.game.data.excel.item.ReliquaryLevelData;
import io.grasscutter.game.data.excel.item.ReliquaryMainPropData;
import io.grasscutter.utils.PrimitiveUtils;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.objects.WeightedList;
import io.grasscutter.utils.objects.lang.TextContainer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/* Collection of loaded game resources. */
public final class GameData {
    /**
     * Fetches the map for the resource in this class.
     *
     * @param target The resource class.
     * @return The map of resources.
     */
    public static Int2ObjectMap getTargetMap(Class<? extends GameResource> target) {
        Int2ObjectMap targetMap = null;
        var targetName = target.getSimpleName();

        try {
            // Fetch the map's static field.
            var field = GameData.class.getDeclaredField(PrimitiveUtils.lowerFirst(targetName) + "Map");

            // Set the field accessible and fetch the map.
            field.setAccessible(true);
            targetMap = (Int2ObjectMap) field.get(null);
            field.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
            Log.error(new TextContainer("server.dedicated.resources.map_error", targetName));
        }

        return targetMap;
    }

    /** Loads the data into the depot maps. */
    public static void loadDepots() {
        // Compute the artifact properties.
        for (var propData : GameData.getReliquaryMainPropDataMap().values()) {
            // Validate the property.
            if (propData.getWeight() <= 0 || propData.getPropDepotId() <= 0) continue;

            // Create a property entry.
            var list =
                    GameData.artifactProperties.computeIfAbsent(
                            propData.getPropDepotId(), k -> new WeightedList<>());
            list.add(propData.getWeight(), propData);
        }
    }

    /*
     * Additional game data.
     */

    @Getter private static final Map<String, AvatarConfig> playerAbilities = new HashMap<>();

    /*
     * Excels
     */

    @Getter
    private static final Int2ObjectMap<AvatarData> avatarDataMap = new Int2ObjectOpenHashMap<>();

    @Getter
    private static final Int2ObjectMap<AvatarSkillData> avatarSkillDataMap =
            new Int2ObjectOpenHashMap<>();

    @Getter
    private static final Int2ObjectMap<AvatarCurveData> avatarCurveDataMap =
            new Int2ObjectOpenHashMap<>();

    @Getter
    private static final Int2ObjectMap<AvatarTalentData> avatarTalentDataMap =
            new Int2ObjectOpenHashMap<>();

    @Getter
    private static final Int2ObjectMap<AvatarPromoteData> avatarPromoteDataMap =
            new Int2ObjectOpenHashMap<>();

    @Getter
    private static final Int2ObjectMap<AvatarSkillDepotData> avatarSkillDepotDataMap =
            new Int2ObjectOpenHashMap<>();

    @Getter
    private static final Int2ObjectMap<SceneData> sceneDataMap = new Int2ObjectOpenHashMap<>();

    @Getter private static final Int2ObjectMap<ItemData> itemDataMap = new Int2ObjectOpenHashMap<>();

    @Getter
    private static final Int2ObjectMap<ReliquaryLevelData> reliquaryLevelDataMap =
            new Int2ObjectOpenHashMap<>();

    @Getter
    private static final Int2ObjectMap<ReliquaryMainPropData> reliquaryMainPropDataMap =
            new Int2ObjectOpenHashMap<>();

    /*
     * Depots
     */

    private static final Int2ObjectMap<WeightedList<ReliquaryMainPropData>> artifactProperties =
            new Int2ObjectOpenHashMap<>();

    /*
     * Data generators.
     */

    /**
     * Gets the list of properties for the given depot.
     *
     * @param depot The depot ID.
     * @return The list of properties.
     */
    public static List<ReliquaryMainPropData> getPropertyList(int depot) {
        return GameData.artifactProperties.get(depot);
    }

    /**
     * Generates a random main property.
     *
     * @param depot The property depot ID.
     * @return The random property.
     */
    public static ReliquaryMainPropData getRandomProp(int depot) {
        // Get the depot's property list.
        var list = GameData.artifactProperties.get(depot);
        return list == null ? null : list.random();
    }

    /*
     * Specialized getters.
     */

    /**
     * Fetches the avatar data for the given ID.
     *
     * @param avatarId The avatar ID.
     * @param level The avatar level.
     * @return The avatar data.
     */
    public static AvatarPromoteData getAscensionData(int avatarId, int level) {
        return GameData.avatarPromoteDataMap.get((avatarId << 8) + level);
    }

    /**
     * Fetches the level data for the artifact based on level & rarity.
     *
     * @param rarity The rarity.
     * @param level The level.
     * @return The level data.
     */
    public static ReliquaryLevelData getArtifactLevelData(int rarity, int level) {
        return GameData.reliquaryLevelDataMap.get((rarity << 8) + level);
    }
}
