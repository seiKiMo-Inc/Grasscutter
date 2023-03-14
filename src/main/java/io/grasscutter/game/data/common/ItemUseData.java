package io.grasscutter.game.data.common;

import io.grasscutter.utils.enums.game.ItemUseReason;
import lombok.Getter;

@Getter public final class ItemUseData {
    private ItemUseReason useOp = ItemUseReason.ITEM_USE_NONE;
    private String[] useParam;
}
