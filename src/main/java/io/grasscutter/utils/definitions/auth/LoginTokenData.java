package io.grasscutter.utils.definitions.auth;

import com.google.gson.annotations.SerializedName;

public final class LoginTokenData {
    @SerializedName("uid")
    public String accountId;

    public String token;

    @SerializedName("guest")
    public boolean isGuest;
}
