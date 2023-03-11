package io.grasscutter.network.kcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import kcp.highway.Ukcp;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.InetSocketAddress;

/** Wrapper for {@link Ukcp}. */
@AllArgsConstructor
public abstract class KcpTunnel {
    @Getter private final Ukcp handle;

    /**
     * Fetches the address of the client.
     *
     * @return An address instance.
     */
    public InetSocketAddress getAddress() {
        return this.handle.user().getRemoteAddress();
    }

    /**
     * Safe method to write bytes to the client.
     *
     * @param bytes The bytes to write.
     */
    public void writeData(byte[] bytes) {
        var buffer = Unpooled.wrappedBuffer(bytes);
        this.handle.write(buffer);
        buffer.release();
    }

    /**
     * Unsafe method to write bytes to the client.
     * Requires manual {@link ByteBuf#release()}.
     *
     * @param buffer The buffer to write.
     */
    public void unsafeWriteData(ByteBuf buffer) {
        this.handle.write(buffer);
    }

    /**
     * Closes the tunnel for this client.
     */
    public void close() {
        this.handle.close();
    }
}
