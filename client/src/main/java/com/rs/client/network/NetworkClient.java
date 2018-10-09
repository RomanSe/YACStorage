package com.rs.client.network;

import com.rs.common.messages.Command;
import com.rs.common.messages.LoginCommand;
import com.rs.common.messages.Response;
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

import java.util.concurrent.ArrayBlockingQueue;

public class NetworkClient extends Thread{
    private static final int MAX_OBJ_SIZE = 1024 * 1024 * 100; // TODO вынести в properties
    private int port;
    private String host;
    private final ArrayBlockingQueue<Command> inbox = new ArrayBlockingQueue<>(DefaultConfig.NETWORK_QUEUE_SIZE);
    private final ArrayBlockingQueue<Response> outbox = new ArrayBlockingQueue<>(DefaultConfig.NETWORK_QUEUE_SIZE);

    private CommandHandler commandHandler;
    private boolean running;

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
            commandHandler = new CommandHandler(outbox);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new ObjectDecoder(DefaultConfig.MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null)),
                            new ObjectEncoder(),
                            //new ResponseDecoder(),
                            commandHandler
                    );
                }
            });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            running = true;
            while (running) {
                Command command = inbox.take();
                commandHandler.invoke(command);
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
        networkClient.invoke(new LoginCommand("user1", "123"));

    }


}
