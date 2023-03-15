package io.grasscutter.player.store;

import io.grasscutter.game.inventory.Item;
import io.grasscutter.network.packets.notify.inventory.AvatarAdd;
import io.grasscutter.player.Avatar;
import io.grasscutter.player.Player;
import io.grasscutter.player.PlayerManager;
import io.grasscutter.utils.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.function.Consumer;
import lombok.Getter;

/* Storage container for player avatars. */
public final class AvatarStorage extends PlayerManager {
    @Getter private final Int2ObjectMap<Avatar> avatars = new Int2ObjectOpenHashMap<>();
    private final Long2ObjectMap<Avatar> avatarsGuid = new Long2ObjectOpenHashMap<>();

    /**
     * Creates a new avatar storage container.
     *
     * @param player The player to create the container for.
     */
    public AvatarStorage(Player player) {
        super(player);
    }

    /**
     * For each avatar, run the consumer.
     *
     * @param forEach The consumer to run.
     */
    public void forEach(Consumer<Avatar> forEach) {
        this.avatars.values().forEach(forEach);
    }

    /**
     * Gets the count of stored avatars.
     *
     * @return The amount of avatar objects.
     */
    public int count() {
        return this.avatars.size();
    }

    /**
     * Checks if the player already owns the avatar.
     *
     * @param avatar The avatar to check.
     * @return Whether the player owns the avatar.
     */
    public boolean contains(Avatar avatar) {
        return this.avatars.containsKey(avatar.getAvatarId());
    }

    /**
     * Adds the avatar to the player's storage. Does not automatically add to the player's team.
     *
     * @param avatar The avatar to add.
     */
    public void add(Avatar avatar) {
        this.add(avatar, false);
    }

    /**
     * Adds the avatar to the player's storage.
     *
     * @param avatar The avatar to add.
     * @param addToTeam Whether to add the avatar to the player's team.
     */
    public void add(Avatar avatar, boolean addToTeam) {
        // Check if the avatar can be added.
        Preconditions.addAvatar(avatar, this.getPlayer());

        // Set the avatar's data.
        avatar.setOwner(this.getPlayer());
        // Add the avatar to the storage.
        this.avatars.put(avatar.getAvatarId(), avatar);
        this.avatarsGuid.put(avatar.getGuid(), avatar);

        avatar.save(); // Save the avatar.

        // Add the starting weapon to the avatar.
        this.addStartingWeapon(avatar);

        // Send the avatar add notification.
        if (this.getPlayer().isLoggedIn()) {
            // Re-calculate the avatar's stats.
            avatar.recalculate();

            // Send the notification.
            this.getPlayer().getSession().send(new AvatarAdd(avatar, addToTeam));
        }
    }

    /**
     * Fetches the avatar from the storage.
     *
     * @param id The avatar's ID.
     * @return The avatar object.
     */
    public Avatar get(int id) {
        return this.avatars.get(id);
    }

    /*
     * Avatar manipulation.
     */

    /**
     * Adds the starting weapon to the avatar.
     *
     * @param avatar The avatar to add the weapon to.
     */
    private void addStartingWeapon(Avatar avatar) {
        // Check if the avatar is owned by the player.
        if (avatar.getOwner() != this.getPlayer()) return;

        // Create the item.
        var weapon = new Item(avatar.getData().getInitialWeapon());
        if (weapon.getItemData() != null) {
            this.getPlayer().getInventory().addItem(weapon);
            avatar.equipItem(weapon, true);
        }
    }
}
