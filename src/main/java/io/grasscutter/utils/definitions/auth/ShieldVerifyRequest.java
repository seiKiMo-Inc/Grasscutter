package io.grasscutter.utils.definitions.auth;

import com.google.gson.annotations.SerializedName;

public final class ShieldVerifyRequest {
    @SerializedName("uid")
    public String accountId;

    public String token;
}
