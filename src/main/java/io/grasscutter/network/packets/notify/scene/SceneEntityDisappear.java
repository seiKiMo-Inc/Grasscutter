package io.grasscutter.network.packets.notify.scene;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.SceneEntityDisappearNotifyOuterClass.SceneEntityDisappearNotify;
import io.grasscutter.proto.VisionTypeOuterClass.VisionType;
import io.grasscutter.world.entity.Entity;

import java.util.Collection;
import java.util.List;

/** Entity disappears in the scene. {@link PacketIds#SceneEntityDisappearNotify}. */
public final class SceneEntityDisappear extends BasePacket<Empty, SceneEntityDisappearNotify> {
    private final Collection<? extends Entity> entities;
    private final VisionType vision;

    /**
     * Constructor for an entity appearing in the scene.
     *
     * @param entity The entity that appears.
     */
    public SceneEntityDisappear(Entity entity) {
        this(entity, VisionType.VISION_TYPE_DIE);
    }

    /**
     * Constructor for an entity appearing in the scene.
     *
     * @param entity The entity that appears.
     */
    public SceneEntityDisappear(Entity entity, VisionType vision) {
        this.entities = List.of(entity);
        this.vision = vision;
    }

    /**
     * Constructor for a group of entities appearing in the scene.
     *
     * @param entities The entities that appear.
     */
    public SceneEntityDisappear(Collection<? extends Entity> entities, VisionType vision) {
        this.entities = entities;
        this.vision = vision;
    }

    @Override
    public SceneEntityDisappearNotify preparePacket() {
        var packet = SceneEntityDisappearNotify.newBuilder()
                .setDisappearType(this.vision);

        // Add entities.
        this.entities.forEach(entity ->
                packet.addEntityList(entity.getId()));

        return packet.build();
    }
}
