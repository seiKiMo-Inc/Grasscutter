package io.grasscutter.game.inventory.use;

import io.grasscutter.player.Avatar;
import io.grasscutter.player.Player;
import io.grasscutter.utils.enums.game.ItemUseTarget;
import lombok.RequiredArgsConstructor;

/* Usage data of an item. */
@RequiredArgsConstructor
public final class UseReason {
    private final Player player;
    private final ItemUseTarget target;
    private final Avatar avatar;
    private final int count;
    private final int optionId;
    private final boolean inDungeon;
    private final int itemId;

    /**
     * Constructor for a minimal use reason.
     *
     * @param player The player using the item.
     * @param target The target of the item use.
     */
    public UseReason(Player player, ItemUseTarget target) {
        this(player, target, null, 1, 0, false, 0);
    }
}
