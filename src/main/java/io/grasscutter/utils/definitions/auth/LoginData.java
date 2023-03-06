package io.grasscutter.utils.definitions.auth;

import com.google.gson.annotations.SerializedName;
import io.grasscutter.utils.objects.JObject;

public final class LoginData {
    @SerializedName("account_type")
    public int type = 1;

    public boolean heartbeat;

    @SerializedName("combo_id")
    public String id;

    @SerializedName("combo_token")
    public String token;

    @SerializedName("open_id")
    public String openId;

    public Object data = JObject.c().add("guest", false);

    @SerializedName("fatigue_remind")
    public String timeRemind = null; // ?
}
