package io.grasscutter.utils.enums;

import io.grasscutter.auth.AuthenticationHolder;

/** The login type. Used in {@link AuthenticationHolder#login()} */
public enum LoginType {
    LOGIN,
    TOKEN,
    COMBO
}
