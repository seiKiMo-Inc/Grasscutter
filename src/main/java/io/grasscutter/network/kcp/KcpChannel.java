package io.grasscutter.network.kcp;

import io.grasscutter.Grasscutter;
import io.grasscutter.utils.NetworkUtils;
import io.jpower.kcp.netty.UkcpChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * Handles data received from a KCP client.
 */
public abstract class KcpChannel extends ChannelInboundHandlerAdapter {
    @Getter private boolean active = false;
    @Getter private UkcpChannel kcpChannel;
    private ChannelHandlerContext context;

    /*
     * Abstract methods.
     */

    /**
     * Invoked when the server finishes processing a client connection.
     */
    protected abstract void onConnect();

    /**
     * Invoked when the server finishes processing a client disconnection.
     */
    protected abstract void onDisconnect();

    /**
     * Invoked when the server receives a message from a client.
     *
     * @param channel The channel.
     * @param data The decoded message data.
     */
    protected abstract void onMessage(KcpChannel channel, byte[] data);

    /*
     * Utility methods.
     */

    /**
     * Sends the specified data to the client.
     * @param data The data to send. Automatically converted into a {@link ByteBuf}.
     */
    protected void send(byte[] data) {
        if (!this.active)
            return;

        kcpChannel.writeAndFlush(NetworkUtils.toBuffer(data));
    }

    /**
     * Terminates the active connection with the client.
     */
    protected void close() {
        if (!this.active)
            return;

        this.active = false;
        this.kcpChannel.close();
    }

    /*
     * Handler methods.
     */

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) {
        this.active = true;
        this.kcpChannel = (UkcpChannel) ctx.channel();
        this.context = ctx;

        this.onConnect();
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) {
        this.active = false;
        this.onDisconnect();
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) {
        var data = NetworkUtils.toByteArray((ByteBuf) msg);
        this.onMessage(this, data);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush(); // Prevent memory leaks by flushing the buffer.
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Log the exception to the server console.
        Grasscutter.getLogger().warn("Exception caught during transit.", cause);
        // Close the connection.
        this.close();
    }
}