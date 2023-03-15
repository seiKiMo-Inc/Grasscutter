package io.grasscutter.game.inventory.use;

import io.grasscutter.game.data.common.ItemUseData;

/* Behavior handler for using an item. */
public class UseAction {
    /**
     * Fetches the correct use handler by the item's use data.
     *
     * @param data The item's use data.
     * @return The use handler, or the default one.
     */
    public static UseAction fetch(ItemUseData data) {
        return null;
    }

    /**
     * Invoked when the item is used.
     *
     * @param reason Data about the item's use.
     * @return True if the item was successfully consumed. False if the item was not consumed.
     */
    public boolean useItem(UseReason reason) {
        return false;
    }

    /**
     * Invoked after an item was consumed.
     *
     * @param reason Data about the item's use.
     * @return True if the action should continue.
     */
    public boolean afterUseItem(UseReason reason) {
        return false;
    }
}
