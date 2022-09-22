package io.grasscutter.network.kcp;

import io.jpower.kcp.netty.ChannelOptionHelper;
import io.jpower.kcp.netty.UkcpChannelOption;
import io.jpower.kcp.netty.UkcpServerChannel;
import io.netty.bootstrap.UkcpServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;

/**
 * Acts as the network interface for KCP traffic.
 * Runs on a separate thread.
 */
public abstract class KcpServer extends Thread {
    private final EventLoopGroup loopGroup = new NioEventLoopGroup();
    @Setter private ChannelInitializer<?> serverInitializer;
    @Getter private final InetSocketAddress address;

    public KcpServer(InetSocketAddress address) {
        this.address = address;
        this.setName("KCP Server Thread");
    }

    @Override
    public void run() {
        // Create a new server initializer.
        if (this.serverInitializer == null) {
            this.setServerInitializer(new KcpServerInitializer());
        }

        try {
            // Create a server loop group & bootstrap.
            var bootstrap = new UkcpServerBootstrap();
            bootstrap.group(this.loopGroup)
                    .channel(UkcpServerChannel.class)
                    .childHandler(this.serverInitializer);

            // Set server options.
            ChannelOptionHelper
                    .nodelay(bootstrap, true, 20, 2, true)
                    .childOption(UkcpChannelOption.UKCP_MTU, 1200);

            // Bind to the address and start the server.
            var future = bootstrap.bind(this.address).sync();
            // Continue server execution until closed.
            future.channel().closeFuture().sync();
        } catch (InterruptedException ignored) {

        } finally {
            // Call finish method.
            this.finish();
        }
    }

    /**
     * Invoked when the server thread is about to die.
     */
    private void finish() {
        // Shutdown the loop group.
        this.loopGroup.shutdownGracefully();
    }
}