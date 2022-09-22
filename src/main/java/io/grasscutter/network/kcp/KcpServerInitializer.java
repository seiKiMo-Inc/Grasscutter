package io.grasscutter.network.kcp;

import io.jpower.kcp.netty.UkcpChannel;
import io.netty.channel.ChannelInitializer;
import org.jetbrains.annotations.NotNull;

/**
 * A KCP server initializer.
 */
public final class KcpServerInitializer extends ChannelInitializer<UkcpChannel> {
    @Override protected void initChannel(@NotNull UkcpChannel ch) {
        ch.pipeline(); // What does this do??
    }
}