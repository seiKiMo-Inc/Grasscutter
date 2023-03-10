package io.grasscutter.utils.enums;

import com.google.protobuf.ByteString;
import io.grasscutter.utils.CryptoUtils;
import io.grasscutter.utils.FileUtils;
import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import lombok.Getter;

/** Types of encryption keys. Varies in XOR, RSA, etc. */
public enum KeyType {
    DISPATCH(FileUtils.resource("keys/xor/dispatchKey.bin"), true),
    DISPATCH_SEED(FileUtils.resource("keys/xor/dispatchSeed.bin"), true),
    ENCRYPT(FileUtils.resource("keys/xor/encryptKey.bin"), true),

    PUBLIC_2(FileUtils.resource("keys/public/2.der"), true, "RSA"),
    PUBLIC_3(FileUtils.resource("keys/public/3.der"), true, "RSA"),
    PUBLIC_4(FileUtils.resource("keys/public/4.der"), true, "RSA"),
    PUBLIC_5(FileUtils.resource("keys/public/5.der"), true, "RSA"),

    SIGNING(FileUtils.resource("keys/private/SigningKey.der"), false, "RSA"),
    AUTH(FileUtils.resource("keys/private/AuthKey.der"), false, "RSA");

    private final Object key;
    private final boolean isByte;

    @Getter private final ByteString protoKey;

    KeyType(Object key, boolean isByte) {
        this.key = key;
        this.isByte = isByte;
        this.protoKey = this.isByte ? ByteString.copyFrom(this.getKey()) : ByteString.EMPTY;
    }

    KeyType(byte[] key, boolean isPublic, String type) {
        this.key = isPublic ?
                CryptoUtils.generatePublicKey(key, type) :
                CryptoUtils.generatePrivateKey(key, type);;
        this.isByte = false;
        this.protoKey = ByteString.EMPTY;
    }

    /**
     * @return The key as a byte array.
     */
    public byte[] getKey() {
        return this.isByte ? (byte[]) this.key : new byte[0];
    }

    /**
     * @return The key as a public key.
     */
    public PublicKey getPublicKey() {
        return this.isByte ? null : (PublicKey) this.key;
    }

    /**
     * @return The key as a private key.
     */
    public PrivateKey getPrivateKey() {
        return this.isByte ? null : (PrivateKey) this.key;
    }

    /**
     * Using the given key, performs an XOR bitwise operation on the given byte array.
     *
     * @param input The input buffer.
     * @return The XOR'd buffer.
     */
    public byte[] xor(byte[] input) {
        // XOR the input with the key.
        CryptoUtils.performXor(input, this.getKey());
        return input;
    }

    /**
     * Creates a cipher from the type.
     *
     * @param type The type of cipher.
     * @return The cipher.
     */
    public Cipher encrypt(String type) {
        return this.cipher(Cipher.ENCRYPT_MODE, type);
    }

    public Cipher decrypt(String type) {
        return this.cipher(Cipher.DECRYPT_MODE, type);
    }

    /**
     * Creates a cipher from the mode and type.
     *
     * @param mode The mode of the cipher.
     * @param type The type of cipher.
     * @return The cipher.
     */
    public Cipher cipher(int mode, String type) {
        try {
            // Create, initialize, and return the cipher.
            var cipher = Cipher.getInstance(type);
            cipher.init(
                    mode,
                    switch (mode) {
                        default -> throw new IllegalStateException("Unexpected value: " + mode);
                        case Cipher.ENCRYPT_MODE -> this.getPublicKey();
                        case Cipher.DECRYPT_MODE -> this.getPrivateKey();
                    });
            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ignored) {
            return null;
        }
    }

    /**
     * Creates a signature from the type.
     *
     * @param type The type of signature.
     * @return The signature.
     */
    public Signature signature(String type) {
        try {
            // Create, initialize, and return the signature.
            var signature = Signature.getInstance(type);
            signature.initSign(this.getPrivateKey());
            return signature;
        } catch (NoSuchAlgorithmException | InvalidKeyException ignored) {
            return null;
        }
    }
}
