package io.grasscutter.game.inventory.tab;

import io.grasscutter.game.inventory.Item;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/* An inventory tab implementation for un-stackable items. */
@RequiredArgsConstructor
public final class CollectionItemTab implements InventoryTab {
    private final Set<Item> items = new HashSet<>();
    @Getter private final int maxCapacity;

    @Override
    public Item getItemById(int itemId) {
        return null;
    }

    @Override
    public void addItem(Item item) {
        this.items.add(item);
    }

    @Override
    public void removeItem(Item item) {
        this.items.remove(item);
    }

    @Override
    public int getCapacity() {
        return this.items.size();
    }
}
