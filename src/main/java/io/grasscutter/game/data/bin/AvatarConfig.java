package io.grasscutter.game.data.bin;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/* Avatar ability configuration. */
public final class AvatarConfig {
    @SerializedName(
            value = "abilities",
            alternate = {"targetAbilities"})
    public ArrayList<AvatarConfigAbility> abilities;
}
