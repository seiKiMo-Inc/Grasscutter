package io.grasscutter.utils;

import io.grasscutter.utils.enums.KeyType;
import org.jetbrains.annotations.Nullable;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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
    static int randomNumber(int min, int max) {
        return min + (int) (random.nextFloat() * (max - min));
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

    /**
     * Generates a random number.
     *
     * @param min The minimum value.
     * @param max The maximum value.
     * @return A random number.
     */
    static float randomNumber(float min, float max) {
        return min + (random.nextFloat() * (max - min));
    }

    /**
     * Generates a public key from the given bytes.
     *
     * @param keyBytes The key bytes.
     * @return The public key.
     */
    @Nullable static PublicKey generatePublicKey(byte[] keyBytes, String type) {
        try {
            return KeyFactory.getInstance(type)
                    .generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ignored) {
            return null;
        }
    }

    /**
     * Generates a private key from the given bytes.
     *
     * @param keyBytes The key bytes.
     * @return The private key.
     */
    @Nullable static PrivateKey generatePrivateKey(byte[] keyBytes, String type) {
        try {
            return KeyFactory.getInstance(type)
                    .generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ignored) {
            return null;
        }
    }
}
