package io.grasscutter.utils.objects.game;

import io.grasscutter.utils.constants.GameConstants;
import io.grasscutter.world.Position;
import lombok.Getter;
import lombok.Setter;

@Getter
public class SpawnDataEntry {
    @Setter private transient SpawnGroupEntry group;
    private int monsterId;
    private int gadgetId;
    private int configId;
    private int level;
    private int poseId;
    private int gatherItemId;
    private int gadgetState;
    private Position pos;
    private Position rot;

    /**
     * Gets the block ID based on the position.
     *
     * @return The block ID.
     */
    public GridBlock getBlockId() {
        var scale = GridBlock.getScale(gadgetId);
        return new GridBlock(
                group.getSceneId(),
                scale,
                (int) (pos.getX() / GameConstants.BLOCK_SIZE[scale]),
                (int) (pos.getZ() / GameConstants.BLOCK_SIZE[scale]));
    }
}
