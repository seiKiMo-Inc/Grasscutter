package io.grasscutter.utils.definitions.auth;

import com.google.gson.annotations.SerializedName;

public final class VerifyData {
    public AccountData account = new AccountData();

    @SerializedName("device_grant_required")
    public boolean grant = false;

    @SerializedName("realname_operation")
    public String realName = "NONE";

    @SerializedName("realperson_required")
    public boolean needRealName = false;

    @SerializedName("safe_mobile_required")
    public boolean needMobileNumber = false;
}
