package io.grasscutter.game.data;

import io.grasscutter.game.data.excel.avatar.AvatarData;
import io.grasscutter.game.data.excel.avatar.AvatarSkillData;
import io.grasscutter.utils.PrimitiveUtils;
import io.grasscutter.utils.constants.Log;
import io.grasscutter.utils.objects.lang.TextContainer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

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
            var field = GameData.class.getDeclaredField(
                    PrimitiveUtils.lowerFirst(targetName) + "Map");

            // Set the field accessible and fetch the map.
            field.setAccessible(true);
            targetMap = (Int2ObjectMap) field.get(null);
            field.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
            Log.error(new TextContainer("server.dedicated.resources.map_error", targetName));
        }

        return targetMap;
    }

    /*
     * Excels
     */

    private static final Int2ObjectMap<AvatarData> avatarDataMap = new Int2ObjectOpenHashMap<>();
    private static final Int2ObjectMap<AvatarSkillData> avatarSkillDataMap = new Int2ObjectOpenHashMap<>();
}
