package com.rs.client.network;

import com.rs.common.messages.Command;
import com.rs.common.messages.LoginCommand;
import com.rs.common.messages.ResponseMsg;
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

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class NetworkClient extends Thread{
    private static final int MAX_OBJ_SIZE = 1024 * 1024 * 100; // TODO вынести в properties
    private int port;
    private String host;
    private CountDownLatch startCountDown;
    private CommandHandler commandHandler;

    public NetworkClient(String host, int port) {
        this.port = port;
        this.host = host;
        startCountDown = new CountDownLatch(1);
    }

    @Override
    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            commandHandler = new CommandHandler();
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new ObjectDecoder(MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null)),
                            new ObjectEncoder(),
                            new ResponseDecoder(),
                            commandHandler
                    );
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            startCountDown.countDown();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            startCountDown.countDown();
            workerGroup.shutdownGracefully();
        }
    }

    void gentleStart() throws InterruptedException {
        start();
        startCountDown.await();
    }

    public ResponseMsg invoke(Command command) {
        ResponseMsg result = null;
        try {
            CountDownLatch waitingCountDown = commandHandler.invoke(command);
            if (waitingCountDown.await(10, TimeUnit.SECONDS)) {
                System.out.println(commandHandler.responseMsg.getResponseCode());
                result = commandHandler.responseMsg;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        int port = DefaultConfig.PORT;
        String host = DefaultConfig.HOST;
        if (args.length > 0) {
            host = args[0];
            if (args.length > 1) {
                port = Integer.parseInt(args[1]);
            }
        }
        NetworkClient networkClient = new NetworkClient(host, port);
        networkClient.gentleStart();
        System.out.println("running");
        networkClient.invoke(new LoginCommand("user", "123"));

    }


}
