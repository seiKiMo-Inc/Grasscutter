package io.grasscutter.utils.objects;

import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;

/**
 * Represents an arrangement of bytes in a stream.
 */
public final class Buffer {
    private final ByteArrayOutputStream stream;

    public Buffer(int size) {
        this.stream = new ByteArrayOutputStream(size);
    }

    public Buffer writeUint16(int i) {
        // Unsigned short
        this.stream.write((byte) ((i >>> 8) & 0xFF));
        this.stream.write((byte) (i & 0xFF));

        return this;
    }

    public Buffer writeUint32(int i) {
        // Unsigned int (long)
        this.stream.write((byte) ((i >>> 24) & 0xFF));
        this.stream.write((byte) ((i >>> 16) & 0xFF));
        this.stream.write((byte) ((i >>> 8) & 0xFF));
        this.stream.write((byte) (i & 0xFF));

        return this;
    }

    @SneakyThrows
    public Buffer writeBytes(byte[] bytes) {
        this.stream.write(bytes); return this;
    }

    public byte[] finish() {
        return this.stream.toByteArray();
    }
}