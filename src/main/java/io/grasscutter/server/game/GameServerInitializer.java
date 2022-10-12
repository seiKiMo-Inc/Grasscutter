package io.grasscutter.server.game;

import io.grasscutter.network.NetworkSession;
import io.jpower.kcp.netty.UkcpChannel;
import io.netty.channel.ChannelInitializer;
import org.jetbrains.annotations.NotNull;

/**
 * spotless:off
 * Handles channel initialization for {@link GameServer}.
 * Creates {@link NetworkSession} on channel initialization.
 * spotless:on
 */
public final class GameServerInitializer extends ChannelInitializer<UkcpChannel> {
    @Override
    protected void initChannel(@NotNull UkcpChannel ch) {
        var pipeline = ch.pipeline(); // Get the channel's pipeline.
        pipeline.addLast(new NetworkSession()); // Add the session handler.
    }
}
