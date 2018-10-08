package com.rs.client.network;

import com.rs.common.messages.Command;
import com.rs.common.messages.ResponseMsg;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;

public class CommandHandler extends ChannelDuplexHandler {

    private ChannelHandlerContext ctx;
    private CountDownLatch waitingCountDown;
    protected ResponseMsg responseMsg;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null)
                return;
            responseMsg = (ResponseMsg) msg;
        } finally {
            waitingCountDown.countDown();
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        this.ctx = ctx;
        super.connect(ctx, remoteAddress, localAddress, promise);
    }

    public CountDownLatch invoke(Command command) throws IOException {
        waitingCountDown = new CountDownLatch(1);
        responseMsg = null;
        ctx.writeAndFlush(command);
        return waitingCountDown;
    }

}
