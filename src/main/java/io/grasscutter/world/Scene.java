package io.grasscutter.world;

import io.grasscutter.game.data.excel.SceneData;
import io.grasscutter.network.packets.notify.scene.SceneEntityAppear;
import io.grasscutter.network.packets.notify.scene.SceneEntityDisappear;
import io.grasscutter.network.protocol.BasePacket;
import io.grasscutter.player.Player;
import io.grasscutter.proto.VisionTypeOuterClass.VisionType;
import io.grasscutter.utils.enums.game.FightProperty;
import io.grasscutter.utils.enums.game.SceneType;
import io.grasscutter.world.entity.Entity;
import io.grasscutter.world.entity.EntityAvatar;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* A container of world data. */
public final class Scene {
    @Getter private final int id;
    @Getter private final World world;
    @Getter private final SceneData data;

    @Getter private final List<Player> players
            = new CopyOnWriteArrayList<>();
    @Getter private final Map<Integer, Entity> entities
            = new ConcurrentHashMap<>();

    @Getter private long creationTime;
    @Getter @Setter private int time;
    @Getter @Setter private int closeTime = -1;

    @Getter @Setter private int previous;
    @Getter @Setter private int previousPoint = -1;

    /**
     * Constructor for a new scene.
     *
     * @param world The world this scene belongs to.
     * @param data The scene data.
     */
    public Scene(World world, SceneData data) {
        this.id = data.getId();
        this.world = world;
        this.data = data;

        this.creationTime = System.currentTimeMillis();
        this.time = 8 * 60;

        this.previous = 3;
    }

    /**
     * Counts the number of players in the scene.
     *
     * @return The number of players in the scene.
     */
    public int count() {
        return this.getPlayers().size();
    }

    /**
     * Fetches the scene type.
     *
     * @return The scene type.
     */
    public SceneType getSceneType() {
        return this.getData().getSceneType();
    }

    /*
     * Time methods.
     */

    /**
     * Sets the scene game time.
     *
     * @param time The time to set.
     */
    public void changeTime(int time) {
        this.setTime(time % 1440);
    }

    /**
     * Gets the scene time in milliseconds.
     *
     * @return The scene time in milliseconds.
     */
    public int getSceneTime() {
        return (int) (System.currentTimeMillis() - this.creationTime);
    }

    /*
     * Entity methods.
     */

    /**
     * Checks if the scene contains an entity.
     *
     * @param entity The entity to check.
     * @return Whether the scene contains the entity.
     */
    public boolean contains(Entity entity) {
        return this.getEntities().containsKey(entity.getId());
    }

    /**
     * Gets an entity from the scene by its ID.
     *
     * @param id The entity's ID.
     * @return The entity.
     */
    @Nullable public Entity getEntity(int id) {
        return this.getEntities().get(id);
    }

    /**
     * Gets an entity from the scene by its config ID.
     *
     * @param configId The entity's config ID.
     * @return The entity.
     */
    @Nullable public Entity getEntityByConfig(int configId) {
        return this.getEntities().values().stream()
                .filter(entity -> entity.getConfigId() == configId)
                .findFirst().orElse(null);
    }

    /**
     * Adds an entity to the scene.
     * This method sends the entity to all players.
     *
     * @param entity The entity to add.
     */
    public void addEntity(Entity entity) {
        this.addEntity(entity, true);
    }

    /**
     * Adds an entity to the scene.
     * This method can send the entity to all players.
     *
     * @param entity The entity to add.
     * @param appear Whether the entity should appear.
     */
    public synchronized void addEntity(Entity entity, boolean appear) {
        this.getEntities().put(entity.getId(), entity); // Add the entity to the scene.
        entity.onCreate(); // Invoke the creation method.

        // Broadcast the entity appearance.
        if (appear) this.broadcastPacket(new SceneEntityAppear(entity));
    }

    /**
     * Adds an entity to the scene.
     * This method only sends the entity to one player.
     *
     * @param entity The entity to add.
     * @param player The player to send the entity appearance to.
     */
    public synchronized void addEntityTo(Entity entity, Player player) {
        this.addEntity(entity, false); // Add the entity to the scene.
        player.getSession().send(new SceneEntityAppear(entity)); // Send the entity appearance.
    }

    /**
     * Adds a group of entities to the scene.
     *
     * @param entities The entities to add.
     */
    public synchronized void addEntities(Collection<? extends Entity> entities) {
        if (entities == null || entities.isEmpty()) return;

        // Add all entities to the scene.
        entities.forEach(entity -> this.addEntity(entity, false));
        // Broadcast the entity appearance.
        this.broadcastPacket(new SceneEntityAppear(entities));
    }

    /**
     * Removes an entity from the scene.
     * This method sends the entity disappearance to all players.
     *
     * @param entity The entity to remove.
     */
    public synchronized Entity removeEntity(Entity entity) {
        return this.removeEntity(entity, true);
    }

    /**
     * Removes an entity from the scene.
     * This method can send the entity disappearance to all players.
     *
     * @param entity The entity to remove.
     * @param disappear Whether the entity should disappear.
     */
    public synchronized Entity removeEntity(Entity entity, boolean disappear) {
        // Remove the entity from the scene.
        var removed = this.getEntities().remove(entity.getId());
        // Invoke the removal method.
        if (removed != null) removed.onDestroy();

        // Broadcast the entity disappearance.
        if (disappear) this.broadcastPacket(new SceneEntityDisappear(entity));

        return removed;
    }

    /**
     * Removes a group of entities from the scene.
     * This method sends the entity disappearance to all players.
     *
     * @param entities The entities to remove.
     * @param type The vision type.
     */
    public synchronized void removeEntities(Collection<? extends Entity> entities, VisionType type) {
        // Remove all entities from the scene.
        var removed = entities.stream().map(entity ->
                        this.removeEntity(entity, false))
                .toList();
        if (removed.isEmpty()) return;

        // Broadcast the entity disappearance.
        this.broadcastPacket(new SceneEntityDisappear(removed, type));
    }

    /**
     * Replaces an entity in the scene.
     *
     * @param oldEntity The entity to replace.
     * @param newEntity The entity to replace with.
     */
    public synchronized void replaceEntity(Entity oldEntity, Entity newEntity) {
        // Remove the old entity from the scene.
        this.removeEntity(oldEntity, false);
        // Add the new entity to the scene.
        this.addEntity(newEntity, false);

        // Broadcast the entity disappearance.
        this.broadcastPacket(new SceneEntityDisappear(oldEntity, VisionType.VISION_TYPE_REPLACE));
        // Broadcast the entity appearance.
        this.broadcastPacket(new SceneEntityAppear(newEntity, VisionType.VISION_TYPE_REPLACE));
    }

    /**
     * Shows entities in this scene to the player.
     *
     * @param player The player to show the entities to.
     */
    public void showEntities(Player player) {
        // Get the other entities in the scene.
        var current = player.getTeams().getCurrentAvatar();
        var others = this.getEntities().values().stream()
                .filter(entity -> entity != current)
                .toList();

        // Send the entity appearance.
        player.getSession().send(new SceneEntityAppear(others, VisionType.VISION_TYPE_MEET));
    }

    /**
     * Spawns a player into the world.
     * This should be called only during login.
     *
     * @param player The player to spawn.
     */
    public void spawnPlayer(Player player) {
        var teams = player.getTeams();

        // Check if the player's avatar is in this scene.
        var entity = teams.getCurrentAvatar();
        if (this.contains(entity)) return;

        // Check the entity's health.
        if (entity.get(FightProperty.FIGHT_PROP_CUR_HP) <= 0f)
            // Set to 1 above 0.
            entity.set(FightProperty.FIGHT_PROP_CUR_HP, 1f);

        // Add the entity to this scene.
        this.addEntity(entity);

        // TODO: Send skill charges.
    }

    /*
     * Player methods.
     */

    /**
     * Adds a player to the scene.
     *
     * @param player The player to add.
     */
    public synchronized void addPlayer(Player player) {
        // Check if the player is already in the scene.
        if (this.getPlayers().contains(player)) return;

        // Remove the player from the previous scene.
        if (player.getScene() != null) {
            player.getScene().removePlayer(player);
        }

        // Add the player to the scene.
        this.getPlayers().add(player);
        player.setSceneId(this.getId());
        player.setScene(this);

        // Configure avatars for the player.
        this.setupPlayerAvatars(player);
    }

    /**
     * Removes a player from the scene.
     *
     * @param player The player to remove.
     */
    public synchronized void removePlayer(Player player) {
        // TODO: Remove challenge if needed.

        // Remove the player from the scene.
        this.getPlayers().remove(player);
        player.setScene(null);

        // TODO: Remove avatars from the scene.
        // TODO: Remove gadgets from the scene.
        // TODO: De-register the scene if needed.
    }

    /**
     * Configures the avatars for use in the scene.
     *
     * @param player The player to configure the avatars for.
     */
    private void setupPlayerAvatars(Player player) {
        var teams = player.getTeams();

        // Remove existing avatars.
        teams.getActiveAvatars().clear();

        // Add new entities to the team.
        var team = teams.getCurrentTeam();
        for (var avatarId : team.getAvatars()) {
            // Create the avatar entity.
            var avatar = new EntityAvatar(player.getScene(),
                    player.getAvatars().get(avatarId));
            // Add the avatar to the team.
            teams.getActiveAvatars().add(avatar);
        }
    }

    /*
     * Network methods.
     */

    /**
     * Broadcasts a packet to all players in the scene.
     *
     * @param packet The packet to broadcast.
     */
    public void broadcastPacket(BasePacket<?, ?> packet) {
        this.getPlayers().forEach(player ->
                player.getSession().send(packet));
    }
}
