package com.rs.server;

import com.rs.common.messages.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class CommandInboundHandler extends ChannelInboundHandlerAdapter {
    private final ExecutorService pool;
    private SocketChannel channel;
    private Context context;
    private BlockingQueue<Job> queue;
    private Job job; //не буду создавать каждый раз. Пока на одного клиента один поток.

    public CommandInboundHandler(SocketChannel channel, ExecutorService pool, BlockingQueue<Job> queue) {
        this.channel = channel;
        this.pool = pool;
        this.queue = queue;
        context = new Context();
        job = Job.getInstance(this.channel, context);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null)
                return;
            if (job.isFree()) {
                job.setFree(false);
                job.setCommand((Command) msg);
                queue.add(job);
            } else {
                Response response = Response.getInstance();
                response.setResponseCode(ResponseCode.BUSY);
                channel.writeAndFlush(response);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //ctx.flush();
        ctx.close();
    }

}
