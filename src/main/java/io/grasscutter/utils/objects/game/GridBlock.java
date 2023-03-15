package io.grasscutter.utils.objects.game;

import io.grasscutter.utils.constants.GameConstants;
import io.grasscutter.world.Position;
import lombok.Data;

/* A block of grids. */
@Data
public final class GridBlock {
    private final int sceneId, scale, x, z;

    /**
     * Generate a list of grids adjacent to the given position.
     *
     * @param sceneId The scene ID.
     * @param pos The center position.
     * @return The list of grids.
     */
    public static GridBlock[] getAdjacentGridBlockIds(int sceneId, Position pos) {
        var results = new GridBlock[5 * 5 * GameConstants.BLOCK_SIZE.length];
        var t = 0;
        for (var scale = 0; scale < GameConstants.BLOCK_SIZE.length; scale++) {
            var x = ((int) (pos.getX() / GameConstants.BLOCK_SIZE[scale]));
            var z = ((int) (pos.getZ() / GameConstants.BLOCK_SIZE[scale]));
            for (var i = x - 2; i < x + 3; i++) {
                for (var j = z - 2; j < z + 3; j++) {
                    results[t++] = new GridBlock(sceneId, scale, i, j);
                }
            }
        }

        return results;
    }

    /**
     * Fetches the scale of a grid for the gadget.
     *
     * @param gadgetId The gadget ID.
     * @return The scale.
     */
    public static int getScale(int gadgetId) {
        return 0; // Unknown implementation.
    }
}
