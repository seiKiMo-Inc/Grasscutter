package io.grasscutter.utils.definitions.auth;

import com.google.gson.annotations.SerializedName;

public final class GranterLoginRequest {
    @SerializedName("app_id")
    public int application;

    @SerializedName("channel_id")
    public int channel;

    @SerializedName("data")
    public String rawLoginData;

    public LoginTokenData loginData;

    public String device;

    @SerializedName("sign")
    public String signature;
}
