package io.grasscutter.game.inventory.tab;

import io.grasscutter.game.inventory.PlayerInventory;
import io.grasscutter.game.inventory.Item;

/* Categorize items. */
public interface InventoryTab {
    /**
     * Fetches an item in this tab by its ID.
     *
     * @param itemId The ID of the item to fetch.
     * @return The item, or null if it does not exist.
     */
    Item getItemById(int itemId);

    /**
     * Event handler for an item being added to this tab.
     * Note: The item's instance is still held in {@link PlayerInventory}.
     *
     * @param item The item that was added.
     */
    void addItem(Item item);

    /**
     * Event handler for an item being removed from this tab.
     *
     * @param item The item that was removed.
     */
    void removeItem(Item item);

    /**
     * Gets the number of items in this tab.
     *
     * @return The number of items.
     */
    int getCapacity();

    /**
     * Gets the maximum number of items that can be held in this tab.
     *
     * @return The maximum number of items.
     */
    int getMaxCapacity();
}
