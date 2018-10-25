package com.rs.client.network;

import com.rs.common.messages.Command;
import com.rs.common.messages.LoginCommand;
import com.rs.common.messages.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import com.rs.common.DefaultConfig;

import java.util.concurrent.ArrayBlockingQueue;

public class NetworkClient extends Thread{
    private static NetworkClient networkClient;
    private static final int MAX_OBJ_SIZE = 1024 * 1024 * 100; // TODO вынести в properties
    private int port;
    private String host;
    private final ArrayBlockingQueue<Command> inbox = new ArrayBlockingQueue<>(DefaultConfig.NETWORK_QUEUE_SIZE);
    private final ArrayBlockingQueue<Response> outbox = new ArrayBlockingQueue<>(DefaultConfig.NETWORK_QUEUE_SIZE);

    private Channel channel;
    private boolean running;

    public static NetworkClient getInstance() {
        if (networkClient == null) {
            networkClient = new NetworkClient(DefaultConfig.HOST, DefaultConfig.PORT);
            networkClient.start();
        }
        return networkClient;
    }

    private NetworkClient(String host, int port) {
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
                            new ObjectDecoder(DefaultConfig.MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null)),
                            new ObjectEncoder(),
                            new CommandHandler(outbox)
                    );
                    channel = ch;
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            running = true;
            while (running) {
                Command command = inbox.take();
                channel.writeAndFlush(command);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void invoke(Command command) throws InterruptedException {
        inbox.put(command);
    }

    public Response getResponse() throws InterruptedException {
        return outbox.take();
    }



    //for testing
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
        networkClient.start();
        networkClient.invoke(new LoginCommand("user", "123"));
        Response response = networkClient.getResponse();
        System.out.println(response.getResponseCode());

    }


}
