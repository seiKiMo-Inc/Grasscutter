package io.grasscutter.utils.enums;

import io.grasscutter.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** Types of encryption keys. Varies in XOR, RSA, etc. */
@Getter
@AllArgsConstructor
public enum KeyType {
    DISPATCH(FileUtils.resource("/keys/xor/dispatchKey.bin")),
    ENCRYPT(FileUtils.resource("/keys/xor/encryptKey.bin"));

    private final byte[] key;
}
