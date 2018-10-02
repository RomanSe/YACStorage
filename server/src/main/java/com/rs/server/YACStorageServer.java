package com.rs.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import com.rs.common.DefaultConfig;



public class YACStorageServer {
    //TODO add ssl
    private static final int MAX_OBJ_SIZE = 1024 * 1024 * 100; // 10 mb
    private static final int WAITING_CONNECTION_REQUESTS = 128;

    private int port;

    public YACStorageServer(int port) {
        this.port = port;
    }

    public void run() {
        EventLoopGroup serverGroup  = new NioEventLoopGroup();
        EventLoopGroup workerGroup  = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(serverGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new ObjectDecoder(MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new CommandInboundHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, WAITING_CONNECTION_REQUESTS)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            serverGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        };
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = DefaultConfig.PORT;
        }
        new YACStorageServer(port).run();
    }

}
