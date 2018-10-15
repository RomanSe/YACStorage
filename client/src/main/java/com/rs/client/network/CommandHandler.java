package com.rs.client.network;

import com.rs.common.messages.Command;
import com.rs.common.messages.Response;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class CommandHandler extends ChannelDuplexHandler {

    private BlockingQueue<Response> outbox;

    public CommandHandler(BlockingQueue<Response> outbox) {
        this.outbox = outbox;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null)
                return;
            outbox.put((Response) msg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

}
