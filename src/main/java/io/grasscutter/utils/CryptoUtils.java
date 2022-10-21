package io.grasscutter.utils;

import io.grasscutter.utils.enums.KeyType;

import java.security.SecureRandom;
import java.util.Random;

/* Utility methods for cryptography. */
public interface CryptoUtils {
    /* Secure random instance. */
    Random random = new SecureRandom();
    
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

    /**
     * Generates a random byte array of the given length.
     * @param length The length of the array.
     * @return The random byte array.
     */
    static byte[] generateBytes(int length) {
        var bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }
}
