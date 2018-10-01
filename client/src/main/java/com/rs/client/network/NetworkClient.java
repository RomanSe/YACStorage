package com.rs.client.network;

import com.rs.common.model.message.AuthMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import com.rs.common.DefaultConfig;

import java.security.NoSuchAlgorithmException;

public class NetworkClient extends Thread {
    private static final int MAX_OBJ_SIZE = 1024 * 1024 * 100; // TODO вынести в properties
    private int port;
    private String host;

    public NetworkClient(String host, int port) {
        this.port = port;
        this.host = host;
    }

    @Override
    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new ObjectDecoder(MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null)),
                            new ObjectEncoder(),
                            new CommandEncoder(),
                            new CommandEncoder2()

                    );
                }
            });
            ChannelFuture f = bootstrap.connect(host, port).sync();
            try {
                f.channel().write(new AuthMsg("test","test"));
                f.channel().flush();
                System.out.println("send");

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = DefaultConfig.PORT;
        String host=DefaultConfig.HOST;
        if (args.length > 0) {
            host = args[0];
            if (args.length > 1) {
                port = Integer.parseInt(args[1]);
            }
        }
        new NetworkClient(host, port).run();
    }
}
