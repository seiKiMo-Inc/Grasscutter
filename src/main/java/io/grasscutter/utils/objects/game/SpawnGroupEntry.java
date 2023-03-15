package io.grasscutter.utils.objects.game;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/* Entry of an entity group spawn. */
@Getter
public final class SpawnGroupEntry {
    private int sceneId;
    private int groupId;
    private int blockId;
    @Setter private List<SpawnDataEntry> spawns;
}
