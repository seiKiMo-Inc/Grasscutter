package io.grasscutter.utils;

import io.grasscutter.utils.enums.KeyType;

/* Utility methods for cryptography. */
public interface CryptoUtils {
    /**
     * Performs an XOR bitwise operation using the provided key.
     *
     * @param buffer The buffer to encrypt.
     * @param keyType The key type to use.
     */
    static void performXor(byte[] buffer, KeyType keyType) {
        var key = keyType.getKey();
        CryptoUtils.performXor(buffer, key);
    }

    /**
     * Performs an XOR bitwise operation on the given byte array.
     *
     * @param buffer The buffer to XOR.
     * @param key The key to XOR with.
     */
    static void performXor(byte[] buffer, byte[] key) {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] ^= key[i % key.length];
        }
    }
}
