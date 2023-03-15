package io.grasscutter.game.inventory;

import io.grasscutter.game.data.GameData;
import io.grasscutter.game.inventory.tab.CollectionItemTab;
import io.grasscutter.game.inventory.tab.InventoryTab;
import io.grasscutter.game.inventory.tab.StackedItemTab;
import io.grasscutter.network.packets.notify.inventory.ItemAddHint;
import io.grasscutter.player.Player;
import io.grasscutter.player.PlayerManager;
import io.grasscutter.utils.constants.Properties;
import io.grasscutter.utils.enums.game.ActionReason;
import io.grasscutter.utils.enums.game.ItemType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Iterator;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* Holds items for a player. */
@Getter
public final class PlayerInventory extends PlayerManager implements Iterable<Item> {
    private final Long2ObjectMap<Item> storage = new Long2ObjectOpenHashMap<>();
    private final Int2ObjectMap<InventoryTab> tabs = new Int2ObjectOpenHashMap<>();

    public PlayerInventory(Player player) {
        super(player);

        // Add the default inventory tabs.
        this.newTab(ItemType.ITEM_WEAPON, new CollectionItemTab(Properties.LIMITS().weapons));
        this.newTab(ItemType.ITEM_RELIQUARY, new CollectionItemTab(Properties.LIMITS().artifacts));
        this.newTab(ItemType.ITEM_MATERIAL, new StackedItemTab(Properties.LIMITS().materials));
        this.newTab(ItemType.ITEM_FURNITURE, new StackedItemTab(Properties.LIMITS().furniture));
    }

    @NotNull @Override
    public Iterator<Item> iterator() {
        return this.getStorage().values().iterator();
    }

    /**
     * Adds a new tab to the inventory.
     *
     * @param tabType The type of tab to add.
     * @param tab The tab to add.
     */
    private void newTab(ItemType tabType, InventoryTab tab) {
        this.getTabs().put(tabType.getValue(), tab);
    }

    /**
     * Fetches a tab from the inventory.
     *
     * @param tabType The type of tab to fetch.
     * @return The tab with the specified type.
     */
    private InventoryTab getTab(ItemType tabType) {
        return this.getTabs().get(tabType.getValue());
    }

    /**
     * Fetches an item from the inventory by its game ID.
     *
     * @param gameId The game ID of the item to fetch.
     * @return The item with the specified game ID.
     */
    public Item getItemById(long gameId) {
        return this.getStorage().get(gameId);
    }

    /**
     * Adds an item to the inventory. Uses the item's ID to get the item's data. Sets the count to 1.
     *
     * @param itemId The ID of the item to add.
     */
    public boolean addItem(int itemId) {
        return this.addItem(itemId, 1);
    }

    /**
     * Adds an item to the inventory. Uses the item's ID to get the item's data.
     *
     * @param itemId The ID of the item to add.
     * @param count The amount of the item to add.
     */
    public boolean addItem(int itemId, int count) {
        return this.addItem(itemId, count, null);
    }

    /**
     * Adds an item to the inventory. Uses the item's ID to get the item's data.
     *
     * @param itemId The ID of the item to add.
     * @param count The amount of the item to add.
     * @param reason The reason for adding the item.
     */
    public boolean addItem(int itemId, int count, @Nullable ActionReason reason) {
        // Get the item's data.
        var itemData = GameData.getItemDataMap().get(itemId);
        if (itemData == null) return false;

        // Create a new instance of the item.
        var item = new Item(itemData, count);
        // Add the item to the inventory.
        return this.addItem(item, reason);
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item The item to add.
     * @return Whether the item was added successfully.
     */
    public boolean addItem(Item item) {
        return this.addItem(item, null);
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item The item to add.
     * @param reason The reason for adding the item.
     * @return Whether the item was added successfully.
     */
    public boolean addItem(Item item, @Nullable ActionReason reason) {
        // Attempt to add the item to the inventory.
        var result = this.addToInventory(item);
        if (result != null && reason != null)
            this.getPlayer().getSession().send(new ItemAddHint(result, reason));

        return true;
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item The item to add.
     * @param reason The reason for adding the item.
     * @param force Whether to force the item to be added.
     * @return Whether the item was added successfully.
     */
    public boolean addItem(Item item, @Nullable ActionReason reason, boolean force) {
        // Attempt to add the item to the inventory.
        var result = this.addToInventory(item);
        if ((force || result != null) && reason != null)
            this.getPlayer().getSession().send(new ItemAddHint(result, reason));

        return true;
    }

    /**
     * Internal method to add an item to the inventory.
     *
     * @param item The item to add.
     */
    private synchronized Item addToInventory(Item item) {
        // Validate the item.
        var data = item.getItemData();
        if (data == null) return null;

        // Get the tab.
        var type = data.getItemType();
        var tab = this.getTab(type);

        // Check if the item can be added.
        if (type != ItemType.ITEM_VIRTUAL && (tab.getCapacity() >= tab.getMaxCapacity())) return null;

        switch (type) {
            case ITEM_RELIQUARY, ITEM_WEAPON -> {
                item.setCount(1); // Set the count to 1.
                this.addToInventory(item, tab); // Add the item to the inventory.
                item.save();
                return item; // Save the item and return it.
            }
            default -> {
                var existing = tab.getItemById(item.getItemId());
                if (existing == null) {
                    // Add the item to the inventory.
                    this.addToInventory(item, tab);
                    // Save the item.
                    item.save();
                    return item;
                } else {
                    // Increment the count of the existing item.
                    existing.setCount(Math.min(existing.getCount() + item.getCount(), data.getMaxStack()));
                    // Save the existing item.
                    existing.save();
                    return existing;
                }
            }
        }
    }

    /**
     * Internal method to add an item to a tab.
     *
     * @param item The item to add.
     * @param tab The tab to add the item to.
     */
    private synchronized void addToInventory(Item item, InventoryTab tab) {
        item.setOwner(this.getPlayer()); // Set the item's owner.
        this.getStorage().put(item.getItemGuid(), item); // Add the item to the storage.
        if (tab != null) tab.addItem(item); // Add the item to the tab.
    }
}
