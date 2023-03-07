package io.grasscutter.utils.definitions.auth;

import com.google.gson.annotations.SerializedName;
import io.grasscutter.utils.objects.JObject;
import lombok.Builder;

@Builder
public final class LoginData {
    @SerializedName("account_type")
    @Builder.Default public int type = 1;

    @Builder.Default public boolean heartbeat = false;

    @SerializedName("combo_id")
    public String comboId;

    @SerializedName("combo_token")
    public String token;

    @SerializedName("open_id")
    public String id;

    @Builder.Default public Object data = JObject.c().add("guest", false);

    @SerializedName("fatigue_remind")
    @Builder.Default public String timeRemind = null;
}
