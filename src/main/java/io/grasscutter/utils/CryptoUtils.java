package io.grasscutter.utils;

import io.grasscutter.utils.enums.KeyType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
     * Performs an MD5 hash on the given input.
     *
     * @param input The input to hash.
     * @return The MD5 hash.
     */
    static String md5Hash(String input) {
        try {
            var md = MessageDigest.getInstance("MD5");
            var array = md.digest(input.getBytes());
            var sb = new StringBuilder();
            for (var anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100), 1, 3);
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException ignored) { }

        return null;
    }

    /**
     * Generates a random byte array of the given length.
     *
     * @param length The length of the array.
     * @return The random byte array.
     */
    static byte[] generateBytes(int length) {
        var bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * Generates a random number.
     *
     * @param min The minimum value.
     * @param max The maximum value.
     * @return A random number.
     */
    static long randomNumber(long min, long max) {
        return min + (long) (random.nextFloat() * (max - min));
    }
}
