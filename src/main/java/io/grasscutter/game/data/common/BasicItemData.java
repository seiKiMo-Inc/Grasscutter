package io.grasscutter.game.data.common;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

/* Basic data about an item. Used as a parameter. */
@Getter
public final class BasicItemData {
    @SerializedName(
            value = "id",
            alternate = {"itemId"})
    private int id;

    @SerializedName(
            value = "count",
            alternate = {"itemCount"})
    private int count;
}
