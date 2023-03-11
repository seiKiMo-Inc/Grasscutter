package io.grasscutter.utils.enums;

/** States for a {@link io.grasscutter.network.NetworkSession}. */
public enum PlayerState {
    INACTIVE,
    TOKEN, // Waiting for a token response from the server.
    LOGIN, // Waiting for a login response from the server.
    CHARACTER, // Picking a starting character.
    ACTIVE,
    BANNED // Account is banned.
}
