package io.grasscutter.utils.constants;

import com.google.protobuf.ByteString;
import java.util.concurrent.atomic.AtomicReference;

/* Constants seen in cryptography. */
public interface CryptoConstants {
    // The RSA encryption type.
    String ENCRYPTION_TYPE = "RSA/ECB/PKCS1Padding";
    String SIGNATURE_TYPE = "SHA256withRSA";

    // The server encryption seed.
    long ENCRYPT_SEED = Long.parseUnsignedLong("11468049314633205968");
    // The signature for the legacy patch.
    String LEGACY_SEED_SIGNATURE = "bm90aGluZyBoZXJl";
    // The server encryption seed as a byte array.
    AtomicReference<ByteString> ENCRYPT_SEED_BUFFER = new AtomicReference<>();
}
