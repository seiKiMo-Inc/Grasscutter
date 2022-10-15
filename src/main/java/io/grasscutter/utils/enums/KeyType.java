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

    OS(FileUtils.resource("keys/public/OS.der"), false),
    CN(FileUtils.resource("keys/public/CN.der"), false),
    SIGNING(FileUtils.resource("keys/private/SigningKey.der"), false);

    private final Object key;
    private final boolean isByte;

    @Getter private final ByteString protoKey;

    KeyType(Object key, boolean isByte) {
        this.key = key;
        this.isByte = isByte;
        this.protoKey = this.isByte ? ByteString.copyFrom(this.getKey()) : ByteString.EMPTY;
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
        return this.isByte ? (PublicKey) this.key : null;
    }

    /**
     * @return The key as a private key.
     */
    public PrivateKey getPrivateKey() {
        return this.isByte ? (PrivateKey) this.key : null;
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
    public Cipher cipher(String type) {
        try {
            // Create, initialize, and return the cipher.
            var cipher = Cipher.getInstance(type);
            cipher.init(Cipher.ENCRYPT_MODE, this.getPublicKey());
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
