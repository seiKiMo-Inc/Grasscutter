package io.grasscutter.utils.definitions.auth;

import com.google.gson.annotations.SerializedName;

/* A login request for the shield API. */
public final class ShieldLoginRequest {
    @SerializedName("account")
    public String username;
    public String password;
    @SerializedName("is_crypto")
    public boolean crypto;
}