package io.grasscutter.network.packets.notify.scene;

import com.google.protobuf.Empty;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.network.protocol.PacketIds;
import io.grasscutter.proto.SceneEntityAppearNotifyOuterClass.SceneEntityAppearNotify;
import io.grasscutter.proto.VisionTypeOuterClass.VisionType;
import io.grasscutter.world.entity.Entity;
import java.util.Collection;
import java.util.List;
import lombok.Setter;
import lombok.experimental.Accessors;

/** Entity appears in the scene. {@link PacketIds#SceneEntityAppearNotify}. */
@Accessors(chain = true)
public final class SceneEntityAppear extends BasePacket<Empty, SceneEntityAppearNotify> {
    private final Collection<? extends Entity> entities;
    private final VisionType vision;
    @Setter private int parameter = -1;

    /**
     * Constructor for an entity appearing in the scene.
     *
     * @param entity The entity that appears.
     */
    public SceneEntityAppear(Entity entity) {
        this(entity, VisionType.VISION_TYPE_BORN);
    }

    /**
     * Constructor for an entity appearing in the scene.
     *
     * @param entity The entity that appears.
     * @param vision The vision type.
     */
    public SceneEntityAppear(Entity entity, VisionType vision) {
        this(List.of(entity), vision);
    }

    /**
     * Constructor for a group of entities appearing in the scene.
     *
     * @param entities The entities that appear.
     */
    public SceneEntityAppear(Collection<? extends Entity> entities) {
        this(entities, VisionType.VISION_TYPE_BORN);
    }

    /**
     * Constructor for a group of entities appearing in the scene.
     *
     * @param entities The entities that appear.
     * @param vision The vision type.
     */
    public SceneEntityAppear(Collection<? extends Entity> entities, VisionType vision) {
        this.entities = entities;
        this.vision = vision;
    }

    @Override
    public SceneEntityAppearNotify preparePacket() {
        var packet = SceneEntityAppearNotify.newBuilder().setAppearType(this.vision);

        // Add entities.
        this.entities.forEach(entity -> packet.addEntityList(entity.toProto()));

        // Add parameter.
        if (this.parameter != -1) {
            packet.setParam(this.parameter);
        }

        return packet.build();
    }
}
