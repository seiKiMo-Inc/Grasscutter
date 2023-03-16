package io.grasscutter.world.entity;

import io.grasscutter.proto.MotionInfoOuterClass.MotionInfo;
import io.grasscutter.proto.MotionStateOuterClass.MotionState;
import io.grasscutter.proto.SceneEntityInfoOuterClass.SceneEntityInfo;
import io.grasscutter.proto.VectorOuterClass.Vector;
import io.grasscutter.utils.ServerUtils;
import io.grasscutter.utils.enums.game.FightProperty;
import io.grasscutter.utils.enums.game.LifeState;
import io.grasscutter.utils.objects.game.SpawnDataEntry;
import io.grasscutter.world.Position;
import io.grasscutter.world.Scene;
import io.grasscutter.world.World;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/* Base class of any game entity. */
@RequiredArgsConstructor
public abstract class Entity {
    @Getter protected int id;

    @Getter @Setter private int blockId;
    @Getter @Setter private int configId;
    @Getter @Setter private int groupId;

    @Getter private final Scene scene;
    @Getter @Setter private SpawnDataEntry spawnData;

    @Getter @Setter
    private MotionState motion = MotionState.MOTION_STATE_NONE; // The entity's motion state.

    @Getter @Setter private int sceneLastMove; // In scene time milliseconds.
    @Getter @Setter private int reliableLastMove; // In reliable sequence.

    @Getter @Setter private boolean invulnerable = false; // Prevents the entity from taking damage.

    /**
     * Gets the world.
     *
     * @return The world.
     */
    public final World getWorld() {
        return this.getScene().getWorld();
    }

    /**
     * Gets the entity type.
     *
     * @return The entity type.
     */
    public final int getEntityType() {
        return this.getId() >> 24;
    }

    /*
     * Entity data.
     */

    /**
     * @return Whether the entity is alive.
     */
    public boolean isAlive() {
        return true;
    }

    /**
     * Gets the life state.
     *
     * @return The life state.
     */
    public final LifeState getLifeState() {
        return this.isAlive() ? LifeState.LIFE_ALIVE : LifeState.LIFE_DEAD;
    }

    /**
     * @return The entity's position.
     */
    public abstract Position getPosition();

    /**
     * @return The entity's rotation.
     */
    public abstract Position getRotation();

    /**
     * @return The entity's data.
     */
    public abstract SceneEntityInfo toProto();

    /**
     * Gets the combat properties.
     *
     * @return A map of properties to values.
     */
    public abstract Int2FloatMap getCombatProperties();

    /**
     * Sets a combat property.
     *
     * @param id The property ID.
     * @param value The property value.
     */
    public final void set(int id, float value) {
        this.getCombatProperties().put(id, value);
    }

    /**
     * Sets a combat property.
     *
     * @param property The property.
     * @param value The property value.
     */
    public final void set(FightProperty property, float value) {
        this.set(property.getId(), value);
    }

    /**
     * Adds to a combat property.
     *
     * @param id The property ID.
     * @param value The value to add.
     */
    public final void add(int id, float value) {
        this.set(id, this.get(id) + value);
    }

    /**
     * Adds to a combat property.
     *
     * @param property The property.
     * @param value The value to add.
     */
    public final void add(FightProperty property, float value) {
        this.add(property.getId(), value);
    }

    /**
     * Gets a combat property.
     *
     * @param id The property ID.
     * @return The property value.
     */
    public final float get(int id) {
        return this.getCombatProperties().getOrDefault(id, 0);
    }

    /**
     * Gets a combat property.
     *
     * @param property The property.
     * @return The property value.
     */
    public final float get(FightProperty property) {
        return this.get(property.getId());
    }

    /**
     * Checks if the entity has a combat property.
     *
     * @param id The property ID.
     * @return Whether the entity has the property.
     */
    public final boolean has(int id) {
        return this.getCombatProperties().containsKey(id);
    }

    /**
     * Checks if the entity has a combat property.
     *
     * @param property The property.
     * @return Whether the entity has the property.
     */
    public final boolean has(FightProperty property) {
        return this.has(property.getId());
    }

    /**
     * Adds all combat properties to a builder.
     *
     * @param builder The builder.
     */
    public final void propsToBuilder(SceneEntityInfo.Builder builder) {
        this.getCombatProperties()
                .forEach((id, value) -> {
                    if (id != null) builder.addFightPropList(ServerUtils.combatProperty(id, value));
                });
    }

    /*
     * Protocol definition conversion.
     */

    /**
     * Converts the entity's positional data to a motion info.
     *
     * @return The motion info.
     */
    public final MotionInfo toMotionInfo() {
        return MotionInfo.newBuilder()
                .setPos(this.getPosition().toProto())
                .setRot(this.getRotation().toProto())
                .setSpeed(Vector.newBuilder())
                .setState(this.getMotion())
                .build();
    }

    /*
     * Entity events.
     */

    /** Invoked when the entity is added to a scene. */
    public void onCreate() {}

    /** Invoked when the entity is removed from a scene. */
    public void onDestroy() {}
}
