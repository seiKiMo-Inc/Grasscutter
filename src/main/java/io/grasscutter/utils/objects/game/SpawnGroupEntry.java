package io.grasscutter.utils.objects.game;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/* Entry of an entity group spawn. */
@Getter public final class SpawnGroupEntry {
    private int sceneId;
    private int groupId;
    private int blockId;
    @Setter private List<SpawnDataEntry> spawns;
}
