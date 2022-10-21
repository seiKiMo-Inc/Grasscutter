package io.grasscutter.utils.definitions.auth;

public final class LoginResultResponse {
    public String message = "OK";
    public int retcode = 0;
    public VerifyData data = new VerifyData();

    public static class VerifyData {
        public AccountData account = new AccountData();
        public boolean device_grant_required = false;
        public String realname_operation = "NONE";
        public boolean realperson_required = false;
        public boolean safe_mobile_required = false;
    }
}