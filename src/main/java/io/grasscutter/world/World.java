package io.grasscutter.world;

import io.grasscutter.game.data.GameData;
import io.grasscutter.player.Player;
import io.grasscutter.server.DedicatedServer;
import io.grasscutter.server.game.GameServer;
import io.grasscutter.utils.enums.game.EntityIdType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* A container of players. */
public final class World implements Iterable<Player> {
    @Getter private final GameServer server;
    @Getter private final Player owner;
    @Getter private final boolean multiplayer;

    @Getter private final List<Player> players
            = Collections.synchronizedList(new ArrayList<>());
    @Getter private final Int2ObjectMap<Scene> scenes
            = Int2ObjectMaps.synchronize(new Int2ObjectOpenHashMap<>());

    private int peerId = 0;
    private int entityId = 0;
    @Getter private int levelId = 0;
    @Getter private int worldLevel = 0;

    /**
     * Constructor for a new single-player world.
     *
     * @param owner The owner of the world.
     */
    public World(Player owner) {
        this(owner, false);
    }

    /**
     * Constructor for a new world.
     *
     * @param owner The owner of the world.
     * @param multiplayer Whether the world is multiplayer.
     */
    public World(Player owner, boolean multiplayer) {
        this.server = DedicatedServer.getGameServer();

        this.owner = owner;
        this.multiplayer = multiplayer;
    }

    /**
     * Get the scene by its ID.
     *
     * @param sceneId The ID of the scene.
     * @return The scene.
     */
    public Scene getScene(int sceneId) {
        // Try to fetch a loaded scene.
        var scene = this.getScenes().get(sceneId);
        if (scene != null) return scene;

        // Load the scene from scene data.
        var sceneData = GameData.getSceneDataMap().get(sceneId);
        if (sceneData != null) {
            // Create the scene.
            scene = new Scene(this, sceneData);
            // Register the scene.
            this.getScenes().put(sceneId, scene);
        }

        return scene;
    }

    /*
     * ID management.
     */

    /**
     * Gets the next entity ID.
     *
     * @return The next entity ID.
     */
    public int nextPeerId() {
        return ++this.peerId;
    }

    /**
     * Gets the next entity ID.
     *
     * @param idType The type of entity ID.
     * @return The next entity ID.
     */
    public int nextEntityId(EntityIdType idType) {
        return (idType.getId() << 24) + ++this.entityId;
    }

    /**
     * Gets the peer ID of the owner.
     *
     * @return The peer ID of the owner.
     */
    public int getAuthority() {
        return this.getOwner().getPeerId();
    }

    /*
     * Player management.
     */

    /**
     * Gets the count of players in this world.
     *
     * @return The amount of players.
     */
    public int count() {
        return this.getPlayers().size();
    }

    /**
     * Adds the player to this world.
     *
     * @param player The player.
     */
    public void addPlayer(Player player) {
        // Check if the player is in this world.
        if (this.getPlayers().contains(player)) {
            return;
        }

        // Remove player from their world.
        if (player.getWorld() != null) {
            player.getWorld().removePlayer(player);
        }

        // Set the player's world.
        player.setWorld(this);
        this.getPlayers().add(player);

        // Update the player.
        player.setPeerId(this.nextPeerId());
        player.getTeams().setEntityId(
                this.nextEntityId(EntityIdType.TEAM));

        // Set the player's scene.
        var scene = this.getScene(player.getSceneId());
        scene.addPlayer(player);
    }

    /**
     * Removes the player from this world.
     *
     * @param player The player.
     */
    public void removePlayer(Player player) {
        // TODO: Remove entities from the player's scene.

        // Remove the player from this world.
        this.getPlayers().remove(player);
        player.setWorld(null);

//        // Remove the player from their scene.
//        var scene = this.getScene(player.getScene());
//        scene.removePlayer(player);
    }

    /**
     * Iterator for the players.
     *
     * @return The iterator.
     */
    @NotNull
    @Override public Iterator<Player> iterator() {
        return this.getPlayers().iterator();
    }
}
