package io.grasscutter.utils.definitions.game;

import com.google.gson.annotations.SerializedName;
import io.grasscutter.utils.enums.game.FightProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class WeaponProperty {
    @SerializedName("propType")
    private final FightProperty property;

    @SerializedName("initValue")
    private final float initial;

    private final String type;
}
