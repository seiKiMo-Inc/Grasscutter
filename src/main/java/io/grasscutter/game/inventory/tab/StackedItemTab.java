package io.grasscutter.game.inventory.tab;

import io.grasscutter.game.inventory.Item;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/* An inventory tab implementation for stacked items. */
@RequiredArgsConstructor
public final class StackedItemTab implements InventoryTab {
    private final Int2ObjectMap<Item> items = new Int2ObjectOpenHashMap<>();
    @Getter private final int maxCapacity;

    @Override
    public Item getItemById(int itemId) {
        return this.items.get(itemId);
    }

    @Override
    public void addItem(Item item) {
        this.items.put(item.getItemId(), item);
    }

    @Override
    public void removeItem(Item item) {
        this.items.remove(item.getItemId());
    }

    @Override
    public int getCapacity() {
        return this.items.size();
    }
}
